package com.team7.spliito_server;

import com.team7.spliito_server.dto.CreateGroupRequest;
import com.team7.spliito_server.dto.GroupMetaInfoRequest;
import com.team7.spliito_server.dto.GroupMetaInfoResponse;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.User;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.UserRepository;
import com.team7.spliito_server.service.GroupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest
//@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@Transactional
class GroupServiceTest extends IntegrationTestSupport {
    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

//    @BeforeEach
//    void setup() {
//        // 각 테스트 전에 모든 데이터 삭제
//        groupRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Creation of a new group")
    @Transactional
    void testCreateNewGroup() {
        // given
        CreateGroupRequest request = new CreateGroupRequest();
        request.setGroupName("Test Group");
        request.setMemberName(List.of("A", "B"));

        // when
        String groupUrl = groupService.createOrUpdateGroup(request);

        // then
        Optional<Group> createdGroup = groupRepository.findByName("Test Group");
        assertTrue(createdGroup.isPresent(), "그룹이 성공적으로 생성되어야 함");
        assertEquals("group/" + createdGroup.get().getId(), groupUrl);
        assertEquals(2, createdGroup.get().getMembers().size(), "멤버가 2명이어야 함");

        List<String> memberNames = createdGroup.get().getMembers().stream()
                .map(User::getName)
                .toList();
        assertTrue(memberNames.containsAll(List.of("A", "B")), "멤버 이름이 일치해야 함");
    }

    @Test
    @DisplayName("Add new members to an existing group")
    @Transactional
    void testAddNewMembersToExistingGroup() {
        // given
        CreateGroupRequest initialRequest = new CreateGroupRequest();
        initialRequest.setGroupName("Test Group");
        initialRequest.setMemberName(List.of("A"));

        groupService.createOrUpdateGroup(initialRequest); // 초기 그룹 생성

        CreateGroupRequest updateRequest = new CreateGroupRequest();
        updateRequest.setGroupName("Test Group");
        updateRequest.setMemberName(List.of("A", "B", "C"));

        // when
        String updatedGroupUrl = groupService.createOrUpdateGroup(updateRequest);

        // then
        Optional<Group> updatedGroup = groupRepository.findByName("Test Group");
        assertTrue(updatedGroup.isPresent(), "기존 그룹이 존재해야 함");
        assertEquals("group/" + updatedGroup.get().getId(), updatedGroupUrl);
        assertEquals(3, updatedGroup.get().getMembers().size(), "업데이트 후 멤버가 3명이어야 함");

        List<String> memberNames = updatedGroup.get().getMembers().stream()
                .map(User::getName)
                .toList();
        assertTrue(memberNames.containsAll(List.of("A", "B", "C")), "멤버 이름이 일치해야 함");
    }
    @DisplayName("그룹의 메타정보를 가져올 수 있다.")
    @Test
    void getAllMembersInGroup() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        Group g1 = new Group("g1", now);
        Group g2 = new Group("g2", now.plusDays(1));
        Group g3 = new Group("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User u3 = new User("u3", groups.get(1));
        User u4 = new User("u4", groups.get(1));
        User u5 = new User("u5", groups.get(2));
        User u6 = new User("u6", groups.get(2));

        userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6));

        GroupMetaInfoRequest request = new GroupMetaInfoRequest(groups.get(2).getId());

        // when
        GroupMetaInfoResponse result = groupService.getGroupMetaInfo(request);

        // then
        assertThat(result)
                .extracting("groupName", "createdDate")
                        .containsExactly("g3", "2024/10/29");

        assertThat(result.getMemberName()).hasSize(2)
                .containsExactlyInAnyOrder("u5", "u6");
    }
}

package com.team7.spliito_server;

import com.team7.spliito_server.dto.CreateGroupRequest;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.User;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.UserRepository;
import com.team7.spliito_server.service.GroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Transactional
class GroupServiceTest {
    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        // 각 테스트 전에 모든 데이터 삭제
        groupRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Creation of a new group")
    void testCreateNewGroup() {
        // given
        CreateGroupRequest request = new CreateGroupRequest();
        request.setGroupName("Test Group");
        request.setMemberNames(List.of("A", "B"));

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
    void testAddNewMembersToExistingGroup() {
        // given
        CreateGroupRequest initialRequest = new CreateGroupRequest();
        initialRequest.setGroupName("Test Group");
        initialRequest.setMemberNames(List.of("A"));

        groupService.createOrUpdateGroup(initialRequest); // 초기 그룹 생성

        CreateGroupRequest updateRequest = new CreateGroupRequest();
        updateRequest.setGroupName("Test Group");
        updateRequest.setMemberNames(List.of("A", "B", "C"));

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

}

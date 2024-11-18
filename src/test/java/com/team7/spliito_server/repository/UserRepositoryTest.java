package com.team7.spliito_server.repository;

import com.team7.spliito_server.IntegrationTestSupport;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
    }

    @DisplayName("유저의 이름과 그룹의 ID로 유저를 가져올 수 있다.")
    @Test
    void findByUsernameAndGroupId() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 28, 0, 0, 0);

        Group g1 = new Group("g1", now);
        Group g2 = new Group("g2", now.plusDays(1));
        Group g3 = new Group("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User duplicateU1 = new User("u1", groups.get(1));
        User duplicateU2 = new User("u2", groups.get(1));
        User u5 = new User("u3", groups.get(2));
        User u6 = new User("u4", groups.get(2));
        List<User> users = userRepository.saveAll(List.of(u1, u2, duplicateU1, duplicateU2, u5, u6));

        // when
        Optional<User> result = userRepository.findByUserNameAndGroupId("u1", groups.get(0).getId());

        // then
        assertThat(result).isPresent();

        assertThat(result.get())
                .extracting("id", "name")
                .containsExactlyInAnyOrder(
                        users.get(0).getId(),
                        users.get(0).getName()
                );

        assertThat(result.get().getGroup())
                .extracting("id", "name")
                .containsExactlyInAnyOrder(
                        groups.get(0).getId(),
                        groups.get(0).getName()
                );
    }

    @DisplayName("유저의 이름과 그룹의 ID로 유저를 가져오지 못할 수도 있다.")
    @Test
    void findByUsernameAndGroupId_EmptyDB() {
        // given

        // when
        Optional<User> result = userRepository.findByUserNameAndGroupId("u1", 1L);

        // then
        assertThat(result).isEmpty();
    }
}
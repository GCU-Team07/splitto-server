package com.team7.spliito_server.model;

import com.team7.spliito_server.dto.AllMemberInGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class GroupTest {

    @DisplayName("그룹 속한 멤버의 이름을 가져올 수 있다.")
    @Test
    void toAllMemberInGroupResponse() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        User u1 = new User("u1");
        User u2 = new User("u2");
        User u3 = new User("u3");

        Group g1 = makeGroup("g1", now, List.of(u1));
        Group g2 = makeGroup("g2", now.plusDays(1), List.of(u1, u2));
        Group g3 = makeGroup("g3", now.plusDays(2), List.of(u1, u2, u3));

        // when
        List<String> r1 = g1.toAllMemberInGroupResponse().getMemberName();
        List<String> r2 = g2.toAllMemberInGroupResponse().getMemberName();
        List<String> r3 = g3.toAllMemberInGroupResponse().getMemberName();

        // then
        assertThat(r1).hasSize(1)
                .containsExactly("u1");

        assertThat(r2).hasSize(2)
                .containsExactlyInAnyOrder("u1", "u2");

        assertThat(r3).hasSize(3)
                .containsExactlyInAnyOrder("u1", "u2", "u3");
    }

    public static Group makeGroup(String name, LocalDateTime createdDate, List<User> members) {
        return Group.builder()
                .name(name)
                .createdDate(createdDate)
                .members(members)
                .build();
    }
}
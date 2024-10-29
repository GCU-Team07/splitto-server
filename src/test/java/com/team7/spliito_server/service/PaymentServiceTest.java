package com.team7.spliito_server.service;

import com.team7.spliito_server.IntegrationTestSupport;
import com.team7.spliito_server.dto.payment.AddPaymentRequest;
import com.team7.spliito_server.dto.payment.AddPaymentResponse;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.PaymentDetail;
import com.team7.spliito_server.model.User;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.PaymentDetailRepository;
import com.team7.spliito_server.repository.PaymentRepository;
import com.team7.spliito_server.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentServiceTest extends IntegrationTestSupport {

    @Autowired
    PaymentService paymentService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentDetailRepository paymentDetailRepository;

    @AfterEach
    void tearDown() {
        paymentDetailRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        groupRepository.deleteAllInBatch();
    }

    @DisplayName("payment를 추가할 수 있다.")
    @Test
    void AddPayment() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        Group g1 = makeGroup("g1", now);
        Group g2 = makeGroup("g2", now.plusDays(1));
        Group g3 = makeGroup("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User u3 = new User("u3", groups.get(0));
        User u4 = new User("u4", groups.get(1));
        User u5 = new User("u5", groups.get(2));
        User u6 = new User("u6", groups.get(2));

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6));

        Long groupId = groups.get(0).getId();
        User paidMember = users.get(0);

        // u2, u3가 각각 2000원씩 u1에게 보내야 함
        AddPaymentRequest request = AddPaymentRequest.of(paidMember.getId(), "item", 6000, List.of("u1", "u2", "u3"));

        // when
        AddPaymentResponse addPaymentResponse = paymentService.addPayment(groupId, request, now);

        // then
        List<PaymentDetail> result = paymentDetailRepository.findAll();
        assertThat(result).hasSize(2)
                        .extracting("paidUser")
                        .extracting("Id")
                        .containsExactlyInAnyOrder(users.get(1).getId(), users.get(2).getId());

        assertThat(addPaymentResponse.getEachPrice()).isEqualTo(2000);
        assertThat(addPaymentResponse.getPayUsers()).hasSize(3)
                .containsExactlyInAnyOrder("u1", "u2", "u3");
    }

    @DisplayName("특정 멤버가 다른 멤버에게 사 주는 경우도 있다.")
    @Test
    void AddPayment_payMembersWithoutPaidMember() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        Group g1 = makeGroup("g1", now);
        Group g2 = makeGroup("g2", now.plusDays(1));
        Group g3 = makeGroup("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User u3 = new User("u3", groups.get(0));
        User u4 = new User("u4", groups.get(1));
        User u5 = new User("u5", groups.get(2));
        User u6 = new User("u6", groups.get(2));

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6));

        Long groupId = groups.get(0).getId();
        User paidMember = users.get(0);

        // u1이 u2, u3에게 사 준 경우 (각자 3000씩 u1에게 보내야 함)
        AddPaymentRequest request = AddPaymentRequest.of(paidMember.getId(), "item", 6000, List.of("u2", "u3"));

        // when
        AddPaymentResponse addPaymentResponse = paymentService.addPayment(groupId, request, now);

        // then
        List<PaymentDetail> result = paymentDetailRepository.findAll();
        assertThat(result).hasSize(2)
                .extracting("paidUser")
                .extracting("Id")
                .containsExactlyInAnyOrder(users.get(1).getId(), users.get(2).getId());

        assertThat(addPaymentResponse.getEachPrice()).isEqualTo(3000);
        assertThat(addPaymentResponse.getPayUsers()).hasSize(2)
                .containsExactlyInAnyOrder("u2", "u3");
    }

    @DisplayName("잘못된 그룹이 요청될 때도 있다.")
    @Test
    void AddPayment_InvalidGroupID() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        Group g1 = makeGroup("g1", now);
        Group g2 = makeGroup("g2", now.plusDays(1));
        Group g3 = makeGroup("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User u3 = new User("u3", groups.get(0));
        User u4 = new User("u4", groups.get(1));
        User u5 = new User("u5", groups.get(2));
        User u6 = new User("u6", groups.get(2));

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6));

        User paidMember = users.get(0);

        AddPaymentRequest request = AddPaymentRequest.of(paidMember.getId(), "item", 6000, List.of("u2", "u3"));

        // when // then
        assertThatThrownBy(() -> paymentService.addPayment(-1L, request, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 그룹입니다!");
    }

    @DisplayName("잘못된 paidMember가 요청될 때도 있다.")
    @Test
    void AddPayment_InvalidPaidMember() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        Group g1 = makeGroup("g1", now);
        Group g2 = makeGroup("g2", now.plusDays(1));
        Group g3 = makeGroup("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User u3 = new User("u3", groups.get(0));
        User u4 = new User("u4", groups.get(1));
        User u5 = new User("u5", groups.get(2));
        User u6 = new User("u6", groups.get(2));

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6));

        Long groupId = groups.get(0).getId();
        User paidMember = users.get(0);

        AddPaymentRequest request = AddPaymentRequest.of(-1L, "item", 6000, List.of("u2", "u3"));

        // when // then
        assertThatThrownBy(() -> paymentService.addPayment(groupId, request, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 유저입니다!");
    }

    @DisplayName("잘못된 그룹 구성원이 요청될 때도 있다.")
    @Test
    void AddPayment_InvalidGroupMember() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 10, 27, 0, 0, 0);

        Group g1 = makeGroup("g1", now);
        Group g2 = makeGroup("g2", now.plusDays(1));
        Group g3 = makeGroup("g3", now.plusDays(2));
        List<Group> groups = groupRepository.saveAll(List.of(g1, g2, g3));

        User u1 = new User("u1", groups.get(0));
        User u2 = new User("u2", groups.get(0));
        User u3 = new User("u3", groups.get(0));
        User u4 = new User("u4", groups.get(1));
        User u5 = new User("u5", groups.get(2));
        User u6 = new User("u6", groups.get(2));

        List<User> users = userRepository.saveAll(List.of(u1, u2, u3, u4, u5, u6));

        Long groupId = groups.get(0).getId();
        User paidMember = users.get(0);

        AddPaymentRequest request = AddPaymentRequest.of(paidMember.getId(), "item", 6000, List.of("u4", "u5"));

        // when // then
        assertThatThrownBy(() -> paymentService.addPayment(groupId, request, now))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹에 존재하지 않는 유저입니다!");
    }



    public static Group makeGroup(String name, LocalDateTime createdDate) {
        return Group.builder()
                .name(name)
                .createdDate(createdDate)
                .build();
    }
}
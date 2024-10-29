package com.team7.spliito_server.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PaymentDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이미 돈을 지불한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paid_user")
    private User paidUser;

    // 돈을 내야 하는 사람 (payUser가 paidUser에게 돈을 보내줘야 함)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_user")
    private User payUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    private int price;

    @Builder
    private PaymentDetail(User paidUser, User payUser, Payment payment, int price) {
        this.paidUser = paidUser;
        this.payUser = payUser;
        this.payment = payment;
        this.price = price;
    }

    public static PaymentDetail of(User paidUser, User payUser, Payment payment, int price) {
        return PaymentDetail.builder()
                .paidUser(paidUser)
                .payUser(payUser)
                .payment(payment)
                .price(price)
                .build();
    }
}

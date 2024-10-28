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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    private boolean isPaid;

    private int price;

    @Builder
    private PaymentDetail(User user, Payment payment, boolean isPaid, int price) {
        this.user = user;
        this.payment = payment;
        this.isPaid = isPaid;
        this.price = price;
    }

    public static PaymentDetail of(User user, Payment payment, boolean isPaid, int price) {
        return PaymentDetail.builder()
                .user(user)
                .payment(payment)
                .isPaid(isPaid)
                .price(price)
                .build();
    }
}

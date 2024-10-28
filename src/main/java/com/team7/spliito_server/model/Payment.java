package com.team7.spliito_server.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    private LocalDateTime createdDate;

    @Builder
    private Payment(String itemName, int totalPrice, Group group, LocalDateTime createdDate) {
        this.itemName = itemName;
        this.totalPrice = totalPrice;
        this.group = group;
        this.createdDate = createdDate;
    }

    public static Payment of(String itemName, int totalPrice, Group group, LocalDateTime createdDate) {
        return Payment.builder()
                .itemName(itemName)
                .totalPrice(totalPrice)
                .group(group)
                .createdDate(createdDate)
                .build();
    }
}

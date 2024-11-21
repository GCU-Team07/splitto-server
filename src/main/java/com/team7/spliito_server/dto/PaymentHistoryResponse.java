package com.team7.spliito_server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PaymentHistoryResponse {

    private String itemName;
    private int itemPrice;
    private String paidMemberName;
    private List<String> payMemberNames;

    public PaymentHistoryResponse(String itemName, int itemPrice, String paidMemberName, List<String> payMemberNames) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.paidMemberName = paidMemberName;
        this.payMemberNames = payMemberNames;
    }
}

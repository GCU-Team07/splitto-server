package com.team7.spliito_server.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SettlementResponse {

    private String payRelationShip;
    private int paymentPrice;

    public SettlementResponse(String payRelationShip, int paymentPrice) {
        this.payRelationShip = payRelationShip;
        this.paymentPrice = paymentPrice;
    }
}
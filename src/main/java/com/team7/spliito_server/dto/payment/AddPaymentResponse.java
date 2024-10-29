package com.team7.spliito_server.dto.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AddPaymentResponse {

    private Long paymentId;
    private int eachPrice;
    private List<String> payUsers;

    public AddPaymentResponse(Long paymentId, int eachPrice, List<String> payUsers) {
        this.paymentId = paymentId;
        this.eachPrice = eachPrice;
        this.payUsers = payUsers;
    }
}

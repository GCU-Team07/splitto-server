package com.team7.spliito_server.controller;

import com.team7.spliito_server.dto.PaymentHistoryResponse;
import com.team7.spliito_server.dto.SettlementResponse;
import com.team7.spliito_server.dto.payment.AddPaymentRequest;
import com.team7.spliito_server.dto.payment.AddPaymentResponse;
import com.team7.spliito_server.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{groupId}")
    public AddPaymentResponse addPayment(@Positive(message = "잘못된 그룹입니다!") @PathVariable Long groupId,
                                         @Valid @RequestBody AddPaymentRequest addPaymentRequest) {
        return paymentService.addPayment(groupId, addPaymentRequest, LocalDateTime.now());
    }

//    @GetMapping("/history/{groupId}")
//    public List<PaymentHistoryResponse> getPaymentHistory(@Positive(message = "잘못된 그룹입니다!") @PathVariable Long groupId) {
//        return paymentService.getPaymentHistory(groupId);
//    }

    @GetMapping("/settlement/{groupId}")
    public List<SettlementResponse> getSettlement(@Positive(message = "잘못된 그룹입니다!") @PathVariable Long groupId) {
        return paymentService.getSettlement(groupId);
    }

}

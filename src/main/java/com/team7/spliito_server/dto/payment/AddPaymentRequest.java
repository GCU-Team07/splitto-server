package com.team7.spliito_server.dto.payment;

import com.team7.spliito_server.model.Payment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AddPaymentRequest {

    // 돈을 낸 멤버의 PK
    @NotNull(message = "돈을 지불한 멤버는 필수입니다!")
    @Positive(message = "잘못된 멤버입니다!")
    private String paidMember;

    @NotBlank(message = "품목의 이름은 필수입니다!")
    private String itemName;

    @Positive(message = "품목 가격은 양수여야 합니다!")
    private int itemPrice;

    @NotEmpty(message = "멤버 이름은 필수입니다!")
    private List<String> payMemberName;

    @Builder
    private AddPaymentRequest(String paidMember, String itemName, int itemPrice, List<String> payMemberName) {
        this.paidMember = paidMember;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.payMemberName = payMemberName;
    }

    public static AddPaymentRequest of(String paidMember, String itemName, int itemPrice, List<String> payMemberName) {
        return AddPaymentRequest.builder()
                .paidMember(paidMember)
                .itemName(itemName)
                .itemPrice(itemPrice)
                .payMemberName(payMemberName)
                .build();
    }
}

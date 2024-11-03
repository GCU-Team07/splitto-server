package com.team7.spliito_server.controller;

import com.team7.spliito_server.ControllerTestSupport;
import com.team7.spliito_server.dto.payment.AddPaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest extends ControllerTestSupport {

    @DisplayName("결제 내역을 추가할 수 있다.")
    @Test
    void addPayment() throws Exception {
        // given
        Long groupId = 1L;
        AddPaymentRequest request = AddPaymentRequest.of("name", "item", 1000, List.of("a", "b"));

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("잘못된 그룹ID가 들어올 수 있다.")
    @Test
    void addPayment_invalidGroupID() throws Exception {
        // given
        Long groupId = -1L;
        AddPaymentRequest request = AddPaymentRequest.of("name", "item", 1000, List.of("a", "b"));

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 그룹입니다!"));
    }

    @DisplayName("잘못된 paidMember가 들어올 수 있다.")
    @Test
    void addPayment_nullPaidMember() throws Exception {
        // given
        Long groupId = 1L;
        AddPaymentRequest request = AddPaymentRequest.of(null, "item", 1000, List.of("a", "b"));

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value( "돈을 지불한 멤버는 필수입니다!"));
    }

    @DisplayName("잘못된 paidMember가 들어올 수 있다.")
    @Test
    void addPayment_invalidPaidMember() throws Exception {
        // given
        Long groupId = 1L;
        AddPaymentRequest request = AddPaymentRequest.of(null, "item", 1000, List.of("a", "b"));

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value( "돈을 지불한 멤버는 필수입니다!"));
    }

    @DisplayName("빈 품목 이름이 들어올 수 있다.")
    @Test
    void addPayment_invalidItemName() throws Exception {
        // given
        Long groupId = 1L;
        AddPaymentRequest request = AddPaymentRequest.of("name", null, 1000, List.of("a", "b"));

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value( "품목의 이름은 필수입니다!"));
    }

    @DisplayName("잘못된 품목 가격이 들어올 수 있다.")
    @Test
    void addPayment_invalidItemPrice() throws Exception {
        // given
        Long groupId = 1L;
        AddPaymentRequest request = AddPaymentRequest.of("name", "item", 0, List.of("a", "b"));

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value( "품목 가격은 양수여야 합니다!"));
    }

    @DisplayName("잘못된 payMember가 들어올 수 있다.")
    @Test
    void addPayment_invalidPayMember() throws Exception {
        // given
        Long groupId = 1L;
        AddPaymentRequest request = AddPaymentRequest.of("name", "item", 1000, List.of());

        // when // then
        mockMvc.perform(post("/payment/" + groupId)
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value( "멤버 이름은 필수입니다!"));
    }
}
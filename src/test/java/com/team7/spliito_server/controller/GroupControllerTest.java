package com.team7.spliito_server.controller;

import com.team7.spliito_server.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class GroupControllerTest extends ControllerTestSupport {

    @DisplayName("특정 그룹에 속한 멤버를 가져올 수 있다.")
    @Test
    void getAllMembersInGroup() throws Exception {
        // given
        Long groupId = 1L;

        // when // then
        mockMvc.perform(get("/group/" + groupId + "/members")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("잘못된 그룹이 요청될 때도 있다.")
    @Test
    void getAllMembersInGroup_invalidGroupId() throws Exception {
        // given
        Long groupId = -1L;

        // when // then
        mockMvc.perform(get("/group/" + groupId + "/members")
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("잘못된 값입니다!"));
    }
}
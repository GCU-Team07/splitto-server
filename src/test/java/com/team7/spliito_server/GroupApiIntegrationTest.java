package com.team7.spliito_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.spliito_server.dto.CreateGroupRequest;
import com.team7.spliito_server.model.Group;
import com.team7.spliito_server.model.User;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class GroupApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository; // 추가된 부분

    @Autowired
    private UserRepository userRepository; // 필요한 경우 추가

    @Test
    void testCreateGroupApi() throws Exception {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setGroupName("Integration Test Group");
        request.setMemberNames(List.of("A", "B"));

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.groupUrl").exists());
    }

    // 유호성 검증 테스트 (잘못된 입력값)
    @Test
    void testCreateGroupApiInvalidRequest() throws Exception {
        CreateGroupRequest invalidRequest = new CreateGroupRequest();
        invalidRequest.setGroupName("");  // empty group name
        invalidRequest.setMemberNames(List.of("A", "B"));

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

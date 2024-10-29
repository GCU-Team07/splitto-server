package com.team7.spliito_server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.spliito_server.dto.CreateGroupRequest;
import com.team7.spliito_server.dto.GroupResponse;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class GroupApiIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @DisplayName("새로운 그룹 생성 API - 성공적으로 그룹이 생성")
    @Test
    void testCreateGroupApi() throws Exception {
        CreateGroupRequest request = new CreateGroupRequest();
        request.setGroupName("Integration Test Group");
        request.setMemberNames(List.of("A", "B"));

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupUrl").exists());
    }

    @DisplayName("새로운 그룹 생성 API - 유효성 검증 실패로 그룹이 생성되지 않음")
    @Test
    void testCreateGroupApiInvalidRequest() throws Exception {
        CreateGroupRequest invalidRequest = new CreateGroupRequest();
        invalidRequest.setGroupName("");  // empty group name
        invalidRequest.setMemberNames(List.of("A", "B"));

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("전체 그룹 조회 API - 모든 그룹이 성공적으로 조회")
    @Test
    void testGetAllGroupsApi() throws Exception {
        CreateGroupRequest request1 = new CreateGroupRequest();
        request1.setGroupName("Group 1");
        request1.setMemberNames(List.of("A", "B"));

        CreateGroupRequest request2 = new CreateGroupRequest();
        request2.setGroupName("Group 2");
        request2.setMemberNames(List.of("C", "D"));

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/group/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<GroupResponse> groups = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        assertEquals(2, groups.size(), "응답에 2개의 그룹이 포함되어야 함");

        GroupResponse group1 = groups.stream().filter(g -> g.getGroupName().equals("Group 1")).findFirst().orElse(null);
        GroupResponse group2 = groups.stream().filter(g -> g.getGroupName().equals("Group 2")).findFirst().orElse(null);

        assertTrue(group1 != null && group1.getMembers().containsAll(List.of("A", "B")),
                "Group 1에는 멤버 A와 B가 있어야 함");
        assertTrue(group2 != null && group2.getMembers().containsAll(List.of("C", "D")),
                "Group 2에는 멤버 C와 D가 있어야 함");
    }
}

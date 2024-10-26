package com.team7.spliito_server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team7.spliito_server.dto.CreateGroupRequest;
import com.team7.spliito_server.dto.GroupResponse;
import com.team7.spliito_server.repository.GroupRepository;
import com.team7.spliito_server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class GroupApiIntegrationTest {

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
        // 테스트 실행 전 데이터베이스 초기화
        userRepository.deleteAll();
        groupRepository.deleteAll();
    }

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

    // 전체 그룹 조회 API 테스트
    @Test
    void testGetAllGroupsApi() throws Exception {
        // 그룹 두 개 생성
        CreateGroupRequest request1 = new CreateGroupRequest();
        request1.setGroupName("Group 1");
        request1.setMemberNames(List.of("A", "B"));

        CreateGroupRequest request2 = new CreateGroupRequest();
        request2.setGroupName("Group 2");
        request2.setMemberNames(List.of("C", "D"));

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(post("/group")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // /group/all 경로에 GET 요청
        MvcResult result = mockMvc.perform(get("/group/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 응답 JSON을 List<GroupResponse>로 변환
        String jsonResponse = result.getResponse().getContentAsString();
        List<GroupResponse> groups = objectMapper.readValue(jsonResponse, new TypeReference<>() {});

        // 검증: 생성한 두 그룹이 응답에 포함되어 있는지 확인
        assertEquals(2, groups.size(), "There should be 2 groups in the response");

        GroupResponse group1 = groups.stream().filter(g -> g.getGroupName().equals("Group 1")).findFirst().orElse(null);
        GroupResponse group2 = groups.stream().filter(g -> g.getGroupName().equals("Group 2")).findFirst().orElse(null);

        assertTrue(group1 != null && group1.getMembers().containsAll(List.of("A", "B")),
                "Group 1 should have members A and B");
        assertTrue(group2 != null && group2.getMembers().containsAll(List.of("C", "D")),
                "Group 2 should have members C and D");
    }
}

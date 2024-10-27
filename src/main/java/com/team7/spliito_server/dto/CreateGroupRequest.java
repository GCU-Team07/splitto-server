package com.team7.spliito_server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateGroupRequest {
    @NotBlank(message = "Group name cannot be blank")  // 그룹 이름이 비어 있거나 공백일 경우 오류 메시지를 반환
    private String groupName;

    @NotEmpty(message = "Member names cannot be empty")  // 리스트가 비어 있거나 null일 경우 오류 발생
    private List<String> memberNames;  // 멤버 이름 목록

    public CreateGroupRequest() {}

    public CreateGroupRequest(String groupName, List<String> memberNames) {
        this.groupName = groupName;
        this.memberNames = memberNames;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(List<String> memberNames) {
        this.memberNames = memberNames;
    }
}

package com.team7.spliito_server.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateGroupRequest {
    @NotBlank(message = "Group name cannot be blank")  // 그룹 이름이 비어 있거나 공백일 경우 오류 메시지를 반환
    private String groupName;

    @NotEmpty(message = "Member names cannot be empty")  // 리스트가 비어 있거나 null일 경우 오류 발생
    private List<String> memberName;  // 멤버 이름 목록

    public CreateGroupRequest() {}

    public CreateGroupRequest(String groupName, List<String> memberName) {
        this.groupName = groupName;
        this.memberName = memberName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMemberName() {
        return memberName;
    }

    public void setMemberName(List<String> memberName) {
        this.memberName = memberName;
    }
}

package com.team7.spliito_server.dto;

import java.util.List;

public class GroupResponse {
    private Long groupId;
    private String groupName;
    private String createdDate;
    private List<String> members;

    // Constructor
    public GroupResponse(Long groupId, String groupName, String createdDate, List<String> members) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.createdDate = createdDate;
        this.members = members;
    }

    // Getters and Setters
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}


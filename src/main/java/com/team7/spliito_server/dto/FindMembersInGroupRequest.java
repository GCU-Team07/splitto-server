package com.team7.spliito_server.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindMembersInGroupRequest {

    private Long groupId;

    public FindMembersInGroupRequest(Long groupId) {
        this.groupId = groupId;
    }
}

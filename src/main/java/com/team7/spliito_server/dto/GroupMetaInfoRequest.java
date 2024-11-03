package com.team7.spliito_server.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMetaInfoRequest {

    private Long groupId;

    public GroupMetaInfoRequest(Long groupId) {
        this.groupId = groupId;
    }
}

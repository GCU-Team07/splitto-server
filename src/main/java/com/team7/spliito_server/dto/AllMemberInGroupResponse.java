package com.team7.spliito_server.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllMemberInGroupResponse {
    private List<String> memberName;

    public AllMemberInGroupResponse(List<String> memberName) {
        this.memberName = memberName;
    }
}

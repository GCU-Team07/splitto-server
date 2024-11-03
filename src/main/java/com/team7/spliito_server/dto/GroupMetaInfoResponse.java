package com.team7.spliito_server.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMetaInfoResponse {
    private String groupName;
    private String createdDate;
    private List<String> memberName;

    public GroupMetaInfoResponse(String groupName, LocalDateTime createdDate, List<String> memberName) {
        this.groupName = groupName;
        this.createdDate = formatDate(createdDate);
        this.memberName = memberName;
    }

    private String formatDate(LocalDateTime createdDate) {
        return createdDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }
}

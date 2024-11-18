package com.team7.spliito_server.model;

import com.team7.spliito_server.dto.GroupMetaInfoResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`group`") // 예약어 충돌 방지
public class Group extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID 자동 생성
    private Long id;

    private String name; // 그룹 이름

    private LocalDateTime createdDate;

    // User 엔티티와의 일대다 관계 매핑
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();

    public Group(String name, LocalDateTime createdDate) {
        this.name = name;
        this.createdDate = createdDate;
    }
    
    public GroupMetaInfoResponse toGroupMetaInfoResponse(List<String> userNames) {
        return new GroupMetaInfoResponse(name, createdDate, userNames);
    }
}


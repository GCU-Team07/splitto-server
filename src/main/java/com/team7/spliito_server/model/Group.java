package com.team7.spliito_server.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`group`") // 예약어 충돌 방지
public class Group extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // ID 자동 생성
    private Long id;

    private String name; // 그룹 이름

    private LocalDateTime createdDate;

    // User 엔티티와의 일대다 관계 매핑
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();



    public Group(String name) {
        this.name = name;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}


package com.team7.spliito_server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "`user`") // MySQL 예약어 충돌 방지
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name; // 사용자 이름

    // Group 엔티티와의 다대일 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    // 기본 생성자
    public User() {}

    // 모든 필드를 포함하는 생성자
    public User(String name, Group group) {
        this.name = name;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}

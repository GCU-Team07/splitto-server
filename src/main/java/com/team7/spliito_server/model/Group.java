package com.team7.spliito_server.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`group`") // MySQL 예약어 충돌 방지
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // ID 자동 생성
    private Long id;

    private String name; // 그룹 이름

    // User 엔티티와의 일대다 관계 매핑
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> members = new ArrayList<>();

    // 기본 생성자
    public Group() {}

    // 모든 필드를 포함하는 생성자
    public Group(String name) {
        this.name = name;
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

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    // 멤버 추가 메서드
    public void addMember(User member) {
        members.add(member);
        member.setGroup(this); // 양방향 연관 관계 설정
    }

    // 멤버 삭제 메서드
    public void removeMember(User member) {
        members.remove(member);
        member.setGroup(null); // 양방향 연관 관계 해제
    }
}


package com.team7.spliito_server.repository;

import com.team7.spliito_server.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    // 그룹 이름으로 그룹을 검색하는 메소드
    Optional<Group> findByName(String name);

    // 그룹 이름이 존재하는지 여부 확인 메서드
    boolean existsByName(String name);
}

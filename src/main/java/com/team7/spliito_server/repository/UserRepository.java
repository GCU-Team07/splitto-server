package com.team7.spliito_server.repository;

import com.team7.spliito_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByGroupId(Long groupId);
}

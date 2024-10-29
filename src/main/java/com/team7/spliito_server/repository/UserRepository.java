package com.team7.spliito_server.repository;

import com.team7.spliito_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByGroupId(Long groupId);

    @Query("select u FROM User u " +
            "join fetch u.group g " +
            "WHERE u.name = :name and g.id = :id")
    Optional<User> findByUserNameAndGroupId(@Param("name") String userName,
                                            @Param("id") Long groupId);

}

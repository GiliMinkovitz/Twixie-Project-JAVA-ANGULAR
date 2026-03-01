package com.example.twixie.service.repository;

import com.example.twixie.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findUsersByUserName(String userName);
    boolean existsUsersByUserName(String userName);

    @Transactional
    @Modifying
    @Query("UPDATE Users u SET u.rate = u.rate + :delta WHERE u.userId = :userId")
    int incrementUsersRate(Long userId, int delta);

    Optional<Users> findByUserName(String userName);

    Users findByEmail(String email);

}

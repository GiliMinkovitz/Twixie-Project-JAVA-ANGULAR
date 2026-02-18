package com.example.twixie.service.repository;

import com.example.twixie.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    UserVerification findByEmail(String email);
}

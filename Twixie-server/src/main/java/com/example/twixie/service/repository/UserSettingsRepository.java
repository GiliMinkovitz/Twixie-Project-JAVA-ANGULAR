package com.example.twixie.service.repository;

import com.example.twixie.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
    UserSettings findByUser_UserId(Long userUserId);
}

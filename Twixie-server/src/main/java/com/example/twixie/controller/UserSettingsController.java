package com.example.twixie.controller;

import com.example.twixie.dto.UserSettingsDTO;
import com.example.twixie.model.UserSettings;
import com.example.twixie.security.CustomUserDetails;
import com.example.twixie.service.mapper.UserSettingsMapper;
import com.example.twixie.service.repository.UserSettingsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/settings")

public class UserSettingsController {
    private final UserSettingsRepository userSettingsRepository;
    private final UserSettingsMapper userSettingsMapper;

    public UserSettingsController(UserSettingsRepository userSettingsRepository, UserSettingsMapper userSettingsMapper) {
        this.userSettingsRepository = userSettingsRepository;
        this.userSettingsMapper = userSettingsMapper;
    }

    @GetMapping("/getUserSetting")
    public ResponseEntity<UserSettingsDTO> getUserSettings(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            Long id = ((CustomUserDetails) principal).getId();

            UserSettings settings = userSettingsRepository.findByUser_UserId(id);

            return new ResponseEntity<>(userSettingsMapper.userSettingsToUserSettingsDTO(settings), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateSettings")
    public ResponseEntity<UserSettingsDTO> updateUserSettings(@Valid @RequestBody UserSettingsDTO newSettings, Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            Long id = ((CustomUserDetails) principal).getId();

            UserSettings settings = userSettingsRepository.findByUser_UserId(id);
            settings.setFontSize(newSettings.getFontSize());
            settings.setAutocompleteEnabled(newSettings.isAutocompleteEnabled());
            settings.setDarkModeEnabled(newSettings.isDarkModeEnabled());

            UserSettings saved = userSettingsRepository.save(settings);
            return new ResponseEntity<>(userSettingsMapper.userSettingsToUserSettingsDTO(saved), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

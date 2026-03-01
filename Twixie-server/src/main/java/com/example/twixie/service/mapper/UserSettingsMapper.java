package com.example.twixie.service.mapper;

import com.example.twixie.dto.UserSettingsDTO;
import com.example.twixie.model.UserSettings;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface UserSettingsMapper {
    UserSettingsDTO userSettingsToUserSettingsDTO(UserSettings userSettings);
}

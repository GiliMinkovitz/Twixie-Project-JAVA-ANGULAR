package com.example.twixie.dto;

import javax.validation.constraints.NotNull;

public class UserSettingsDTO {
    @NotNull
    private boolean darkModeEnabled;
    @NotNull
    private String fontSize;
    @NotNull
    private boolean autocompleteEnabled;

    public UserSettingsDTO() {
    }

    public UserSettingsDTO(boolean darkModeEnabled, String fontSize, boolean autocompleteEnabled) {
        this.darkModeEnabled = darkModeEnabled;
        this.fontSize = fontSize;
        this.autocompleteEnabled = autocompleteEnabled;
    }

    public boolean isDarkModeEnabled() {
        return darkModeEnabled;
    }

    public void setDarkModeEnabled(boolean darkModeEnabled) {
        this.darkModeEnabled = darkModeEnabled;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isAutocompleteEnabled() {
        return autocompleteEnabled;
    }

    public void setAutocompleteEnabled(boolean autocompleteEnabled) {
        this.autocompleteEnabled = autocompleteEnabled;
    }
}

package com.example.twixie.model;

import jakarta.persistence.*;

@Entity
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean darkModeEnabled = false;
    private String fontSize = "Medium";
    private boolean autocompleteEnabled = true;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private Users user;

    public UserSettings() {
    }

    public UserSettings(Long id, boolean darkModeEnabled, String fontSize, boolean autocompleteEnabled, Users user) {
        this.id = id;
        this.darkModeEnabled = darkModeEnabled;
        this.fontSize = fontSize;
        this.autocompleteEnabled = autocompleteEnabled;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
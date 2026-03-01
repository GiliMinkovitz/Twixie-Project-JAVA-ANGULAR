package com.example.twixie.dto;

public class UsersDTO {

    private Long userId;
    private String userName;
    private String biography;
    private String picture;
    private UserSettingsDTO settings;

    public UsersDTO() {
    }

    public UsersDTO(Long userId, String userName, String biography, String picture, UserSettingsDTO settings) {
        this.userId = userId;
        this.userName = userName;
        this.biography = biography;
        this.picture = picture;
        this.settings = settings;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public UserSettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(UserSettingsDTO settings) {
        this.settings = settings;
    }
}

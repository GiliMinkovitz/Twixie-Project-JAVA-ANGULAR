package com.example.twixie.dto;

public class UserBasicInfoDto {
    private String userName;
    private String picture;

    public UserBasicInfoDto() {
    }

    public UserBasicInfoDto(String userName, String picture) {
        this.userName = userName;
        this.picture = picture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

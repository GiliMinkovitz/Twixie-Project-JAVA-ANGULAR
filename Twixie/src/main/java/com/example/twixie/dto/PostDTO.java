package com.example.twixie.dto;


import java.time.LocalDateTime;

public class PostDTO {

    private Long postId;
    private Long topicId;
    private String topicName;
    private String content;
    private String title;
    private LocalDateTime datePosted;
    private LocalDateTime lastUpdated;
    private UsersDTO poster;
    private String imagePath;

    public PostDTO(Long postId, Long topicId, String topicName, String content, String title, LocalDateTime datePosted, LocalDateTime lastUpdated, UsersDTO poster, String imagePath) {
        this.postId = postId;
        this.topicId = topicId;
        this.topicName = topicName;
        this.content = content;
        this.title = title;
        this.datePosted = datePosted;
        this.lastUpdated = lastUpdated;
        this.poster = poster;
        this.imagePath = imagePath;
    }

    public PostDTO() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public UsersDTO getPoster() {
        return poster;
    }

    public void setPoster(UsersDTO poster) {
        this.poster = poster;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

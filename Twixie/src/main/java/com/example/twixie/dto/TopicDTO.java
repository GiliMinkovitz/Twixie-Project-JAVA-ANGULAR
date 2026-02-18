package com.example.twixie.dto;

public class TopicDTO {
    private Long topicId;
    private String name;
    private String description;

    public TopicDTO(Long topicId, String name, String description) {
        this.topicId = topicId;
        this.name = name;
        this.description = description;
    }

    public TopicDTO() {
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

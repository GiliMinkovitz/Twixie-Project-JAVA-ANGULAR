package com.example.twixie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long topicId;
    private String name;
    private String description;
    private long rate;

    @OneToMany(mappedBy = "topic")
    private List<Post> posts;

    public Topic() {
    }

    public Topic(Long topicId, String name, String description, long rate, List<Post> posts) {
        this.topicId = topicId;
        this.name = name;
        this.description = description;
        this.rate = rate;
        this.posts = posts;
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

    public long getRate() {
        return rate;
    }

    public void setRate(long rank) {
        this.rate = rank;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}

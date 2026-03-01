package com.example.twixie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @NotNull(message = "You must select a user for the post")
    @ManyToOne
    private Users poster;

    @NotNull(message = "You must select a topic for the post")
    @ManyToOne
    private Topic topic;

    @Column(length = 10000)
    @NotBlank(message = "Content must not be blank")
    private String content;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotNull(message = "Post date must be specified")
    @PastOrPresent(message = "Post date cannot be in the future")
    private LocalDateTime datePosted;

    private long rate;

    private LocalDateTime lastUpdated;

    private String imagePath;


    @OneToMany(mappedBy = "parentPostResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses;

    @OneToMany(mappedBy = "parentPostLiked", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes;

    public Post() {
    }

    public Post(Long postId, Users poster, Topic topic, String content, String title, LocalDateTime datePosted, long rate, LocalDateTime lastUpdated, String imagePath, List<Response> responses, List<Likes> likes) {
        this.postId = postId;
        this.poster = poster;
        this.topic = topic;
        this.content = content;
        this.title = title;
        this.datePosted = datePosted;
        this.rate = rate;
        this.lastUpdated = lastUpdated;
        this.imagePath = imagePath;
        this.responses = responses;
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Likes> getLikes() {
        return likes;
    }

    public void setLikes(List<Likes> likes) {
        this.likes = likes;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rank) {
        this.rate = rank;
    }

    public LocalDateTime getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(LocalDateTime datePosted) {
        this.datePosted = datePosted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Users getPoster() {
        return poster;
    }

    public void setPoster(Users poster) {
        this.poster = poster;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

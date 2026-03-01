package com.example.twixie.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    private Long likeId;
    @ManyToOne
    private Users liker;
    @ManyToOne
    private Post parentPostLiked;
    private LocalDateTime dateLiked;
    private boolean type;

    public Likes() {
    }

    public Likes(Long likeId, Users liker, Post parentPostLiked, LocalDateTime dateLiked, boolean type) {
        this.likeId = likeId;
        this.liker = liker;
        this.parentPostLiked = parentPostLiked;
        this.dateLiked = dateLiked;
        this.type = type;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Users getLiker() {
        return liker;
    }

    public void setLiker(Users liker) {
        this.liker = liker;
    }

    public Post getParentPostLiked() {
        return parentPostLiked;
    }

    public void setParentPostLiked(Post parentPostLiked) {
        this.parentPostLiked = parentPostLiked;
    }

    public LocalDateTime getDateLiked() {
        return dateLiked;
    }

    public void setDateLiked(LocalDateTime dateLiked) {
        this.dateLiked = dateLiked;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}

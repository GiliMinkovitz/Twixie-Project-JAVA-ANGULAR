package com.example.twixie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    @JsonIgnore
    private Users follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    @JsonIgnore
    private Users followee;

    private LocalDateTime followStart;

    public Follow() {
    }

    public Follow(Users follower, Users followee, LocalDateTime followStart) {
        this.follower = follower;
        this.followee = followee;
        this.followStart = followStart;
    }

    public Follow(Long followId, Users follower, Users followee, LocalDateTime followStart) {
        this.followId = followId;
        this.follower = follower;
        this.followee = followee;
        this.followStart = followStart;
    }

    public LocalDateTime getFollowStart() {
        return followStart;
    }

    public void setFollowStart(LocalDateTime followingStart) {
        this.followStart = followingStart;
    }

    public Users getFollowee() {
        return followee;
    }

    public void setFollowee(Users followee) {
        this.followee = followee;
    }

    public Users getFollower() {
        return follower;
    }

    public void setFollower(Users follower) {
        this.follower = follower;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followingId) {
        this.followId = followingId;
    }
}

package com.example.twixie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username cannot be empty")
    private String userName;

    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Image path cannot be empty")
    private String imagePath;

    private long rate;

    @Column(length = 10000)
    @NotBlank(message = "Biography cannot be empty")
    private String biography;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserSettings settings;

    @JsonIgnore
    @ManyToMany
    private Set<Role> roles = new HashSet<>();

    //מערך של הפוסטים האחרונים שצפיתי בהם
    @OneToMany(mappedBy = "poster")
    private List<Post> lastPosts;


    //מערך של הפוסטים האחרונים שהעליתי
    @OneToMany(mappedBy = "poster")
    private List<Post> myPosts;

    // רשימה של מזהה כל האנשים שהיוזר עוקב אחריהם
    @OneToMany(mappedBy = "follower")
    private List<Follow> followees;

    @OneToMany(mappedBy = "followee")
    private List<Follow> followers;

    @OneToMany(mappedBy = "responser")
    private List<Response> myResponses;

    @OneToMany(mappedBy = "liker")
    private List<Likes> myLikes;


    public Users() {
    }

    public Users(Long userId, String userName, String email, String password, String imagePath, long rate, String biography, UserSettings settings, Set<Role> roles, List<Post> lastPosts, List<Post> myPosts, List<Follow> followees, List<Follow> followers, List<Response> myResponses, List<Likes> myLikes, String verificationCode) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.imagePath = imagePath;
        this.rate = rate;
        this.biography = biography;
        this.settings = settings;
        this.roles = roles;
        this.lastPosts = lastPosts;
        this.myPosts = myPosts;
        this.followees = followees;
        this.followers = followers;
        this.myResponses = myResponses;
        this.myLikes = myLikes;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public UserSettings getSettings() {
        return settings;
    }

    public void setSettings(UserSettings settings) {
        this.settings = settings;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Post> getLastPosts() {
        return lastPosts;
    }

    public void setLastPosts(List<Post> lastPosts) {
        this.lastPosts = lastPosts;
    }

    public List<Post> getMyPosts() {
        return myPosts;
    }

    public void setMyPosts(List<Post> myPosts) {
        this.myPosts = myPosts;
    }

    public List<Follow> getFollowees() {
        return followees;
    }

    public void setFollowees(List<Follow> followees) {
        this.followees = followees;
    }

    public List<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }

    public List<Response> getMyResponses() {
        return myResponses;
    }

    public void setMyResponses(List<Response> myResponses) {
        this.myResponses = myResponses;
    }

    public List<Likes> getMyLikes() {
        return myLikes;
    }

    public void setMyLikes(List<Likes> myLikes) {
        this.myLikes = myLikes;
    }

}

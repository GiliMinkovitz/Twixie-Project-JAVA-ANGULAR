package com.example.twixie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @ManyToOne
    private Users responser;
    @ManyToOne
    private Post parentPostResponse;

    @NotNull(message = "Parent response ID must be specified")
    private Long parentResponseID;

    @NotBlank(message = "Content cannot be empty")
    private String content;

    @NotNull(message = "Post date must be specified")
    @PastOrPresent(message = "Post date cannot be in the future")
    private LocalDateTime dateCreated;


    private boolean isDeleted = false;

    public Response() {
    }

    public Response(Long responseId, Users responser, Post parentPostResponse, Long parentResponseID, String content, LocalDateTime dateCreated, boolean isDeleted) {
        this.responseId = responseId;
        this.responser = responser;
        this.parentPostResponse = parentPostResponse;
        this.parentResponseID = parentResponseID;
        this.content = content;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public Users getResponser() {
        return responser;
    }

    public void setResponser(Users responser) {
        this.responser = responser;
    }

    public Post getParentPostResponse() {
        return parentPostResponse;
    }

    public void setParentPostResponse(Post parentPostResponse) {
        this.parentPostResponse = parentPostResponse;
    }

    public Long getParentResponseID() {
        return parentResponseID;
    }

    public void setParentResponseID(Long parentResponseID) {
        this.parentResponseID = parentResponseID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

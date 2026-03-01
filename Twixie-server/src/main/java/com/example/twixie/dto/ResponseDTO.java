package com.example.twixie.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResponseDTO {
    private Long responseId;
    private UsersDTO responser;
    private Long parentPostResponseId;
    private Long parentResponseID;
    private String content;
    private LocalDateTime dateCreated;
    private boolean isDeleted;

    private List<ResponseDTO> children = new ArrayList<>();

    public ResponseDTO() {
    }

    public ResponseDTO(Long responseId, UsersDTO responser, Long parentPostResponseId, Long parentResponseID, String content, LocalDateTime dateCreated, boolean isDeleted, List<ResponseDTO> children) {

        this.responseId = responseId;
        this.responser = responser;
        this.parentPostResponseId = parentPostResponseId;
        this.parentResponseID = parentResponseID;
        this.content = content;
        this.dateCreated = dateCreated;
        this.isDeleted = isDeleted;
        this.children = children;
    }

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public UsersDTO getResponser() {
        return responser;
    }

    public void setResponser(UsersDTO responser) {
        this.responser = responser;
    }

    public Long getParentPostResponseId() {
        return parentPostResponseId;
    }

    public void setParentPostResponseId(Long parentPostResponseId) {
        this.parentPostResponseId = parentPostResponseId;
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

    public List<ResponseDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ResponseDTO> children) {
        this.children = children;
    }
}



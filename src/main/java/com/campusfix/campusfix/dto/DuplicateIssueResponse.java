package com.campusfix.campusfix.dto;

public class DuplicateIssueResponse {

    private String message;
    private Long existingIssueId;

    public DuplicateIssueResponse() {
    }

    public DuplicateIssueResponse(String message, Long existingIssueId) {
        this.message = message;
        this.existingIssueId = existingIssueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getExistingIssueId() {
        return existingIssueId;
    }

    public void setExistingIssueId(Long existingIssueId) {
        this.existingIssueId = existingIssueId;
    }
}
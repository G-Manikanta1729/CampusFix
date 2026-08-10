package com.campusfix.campusfix.dto;

public class IssueCreateResponse {

    private String message;
    private IssueResponse issue;
    private Long existingIssueId;

    public IssueCreateResponse() {
    }

    public IssueCreateResponse(
            String message,
            IssueResponse issue,
            Long existingIssueId) {

        this.message = message;
        this.issue = issue;
        this.existingIssueId = existingIssueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public IssueResponse getIssue() {
        return issue;
    }

    public void setIssue(IssueResponse issue) {
        this.issue = issue;
    }

    public Long getExistingIssueId() {
        return existingIssueId;
    }

    public void setExistingIssueId(Long existingIssueId) {
        this.existingIssueId = existingIssueId;
    }
}
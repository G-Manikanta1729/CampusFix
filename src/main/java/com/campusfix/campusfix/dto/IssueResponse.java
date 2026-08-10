package com.campusfix.campusfix.dto;

public class IssueResponse {

    private Long id;
    private String title;
    private String location;
    private String status;

    public IssueResponse() {
    }

    public IssueResponse(Long id, String title, String location, String status) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
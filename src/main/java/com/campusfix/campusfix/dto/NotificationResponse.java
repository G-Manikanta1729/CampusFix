package com.campusfix.campusfix.dto;

public class NotificationResponse {

    private Long id;
    private String message;
    private String type;
    private boolean read;

    public NotificationResponse() {
    }

    public NotificationResponse(
            Long id,
            String message,
            String type,
            boolean read) {

        this.id = id;
        this.message = message;
        this.type = type;
        this.read = read;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public boolean isRead() {
        return read;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
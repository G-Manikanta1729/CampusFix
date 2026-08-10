package com.campusfix.campusfix.exception;

public class IssueNotFoundException extends RuntimeException {

    public IssueNotFoundException(String message) {
        super(message);
    }
}
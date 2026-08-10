package com.campusfix.campusfix.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .toList();

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<Object> handleIssueNotFound(
            IssueNotFoundException ex) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(
                body,
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Object> handleNotificationNotFound(
            NotificationNotFoundException ex) {

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("status", 404);
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(
                body,
                HttpStatus.NOT_FOUND);
    }
}
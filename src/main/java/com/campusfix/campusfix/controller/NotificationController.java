package com.campusfix.campusfix.controller;

import com.campusfix.campusfix.dto.NotificationResponse;
import com.campusfix.campusfix.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @GetMapping("/student")
    public List<NotificationResponse> getStudentNotifications(
            Authentication authentication) {

        String studentUsername =
                authentication.getName();

        return notificationService
                .getStudentNotifications(studentUsername);
    }

    @GetMapping("/admin")
    public List<NotificationResponse> getAdminNotifications() {

        return notificationService
                .getAdminNotifications();
    }

    @PutMapping("/{id}/read")
    public NotificationResponse markAsRead(
            @PathVariable Long id) {

        return notificationService.markAsRead(id);
    }
}
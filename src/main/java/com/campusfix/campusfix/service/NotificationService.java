package com.campusfix.campusfix.service;

import com.campusfix.campusfix.dto.NotificationResponse;
import com.campusfix.campusfix.exception.NotificationNotFoundException;
import com.campusfix.campusfix.model.Notification;
import com.campusfix.campusfix.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {

        this.notificationRepository = notificationRepository;
    }

    public void createDuplicateNotification(
            String studentUsername) {

        Notification studentNotification =
                new Notification(
                        studentUsername,
                        "This issue has already been reported.",
                        "DUPLICATE_ISSUE",
                        false
                );

        Notification adminNotification =
                new Notification(
                        "ADMIN",
                        "A student reported an already existing issue.",
                        "DUPLICATE_ISSUE",
                        false
                );

        notificationRepository.save(studentNotification);
        notificationRepository.save(adminNotification);
    }

    /*
     * Returns notifications belonging to a particular student.
     */
    public List<NotificationResponse> getStudentNotifications(
            String studentUsername) {

        return notificationRepository
                .findByRecipient(studentUsername)
                .stream()
                .map(notification ->
                        new NotificationResponse(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getType(),
                                notification.isRead()
                        )
                )
                .toList();
    }

    /*
     * Returns all notifications addressed to ADMIN.
     */
    public List<NotificationResponse> getAdminNotifications() {

        return notificationRepository
                .findByRecipient("ADMIN")
                .stream()
                .map(notification ->
                        new NotificationResponse(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getType(),
                                notification.isRead()
                        )
                )
                .toList();
    }

    public NotificationResponse markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with id: " + id
                                )
                        );

        notification.setRead(true);

        Notification savedNotification =
                notificationRepository.save(notification);

        return new NotificationResponse(
                savedNotification.getId(),
                savedNotification.getMessage(),
                savedNotification.getType(),
                savedNotification.isRead()
        );
    }
}
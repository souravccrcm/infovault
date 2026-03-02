package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.NotificationResponse;
import com.ccrcm.infovault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get-notifications")
    public ResponseEntity<List<NotificationResponse>> getLatestNotifications() {

        return ResponseEntity.ok(
                notificationService.getLatestNotifications()
        );
    }

    @PutMapping("/notifications/{id}/mark-read")
    public ResponseEntity<String> markNotificationAsRead(@PathVariable Long id) {

        notificationService.markAsRead(id);

        return ResponseEntity.ok("Notification marked as read successfully");
    }
}

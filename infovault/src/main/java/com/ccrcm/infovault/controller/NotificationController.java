package com.ccrcm.infovault.controller;

import com.ccrcm.infovault.dto.response.NotificationResponse;
import com.ccrcm.infovault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

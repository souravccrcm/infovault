package com.ccrcm.infovault.service.impl;

import com.ccrcm.infovault.dto.response.NotificationResponse;
import com.ccrcm.infovault.repository.NotificationRepository;
import com.ccrcm.infovault.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> getLatestNotifications() {
        return notificationRepository
                .findByActiveTrueOrderByCreatedAtDesc()
                .stream()
                .map(notification -> new NotificationResponse(
                        notification.getId(),
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getIsRead(),
                        notification.getCreatedAt()
                ))
                .toList();
    }
}

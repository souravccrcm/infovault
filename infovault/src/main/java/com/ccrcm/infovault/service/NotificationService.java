package com.ccrcm.infovault.service;

import com.ccrcm.infovault.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> getLatestNotifications();
}

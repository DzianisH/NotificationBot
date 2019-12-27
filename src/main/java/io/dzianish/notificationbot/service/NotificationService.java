package io.dzianish.notificationbot.service;

import io.dzianish.notificationbot.dto.NotificationConfig;

public interface NotificationService {
    void createNotification(NotificationConfig config);
}

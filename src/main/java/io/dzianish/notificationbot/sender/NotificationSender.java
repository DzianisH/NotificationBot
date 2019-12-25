package io.dzianish.notificationbot.sender;

import io.dzianish.notificationbot.dto.Notification;

public interface NotificationSender {
    void sendNotification(Notification notification);
}

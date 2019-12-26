package io.dzianish.notificationbot.sender.impl;

import io.dzianish.notificationbot.dto.Notification;
import io.dzianish.notificationbot.sender.NotificationSender;
import io.dzianish.notificationbot.sender.TgMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class NotificationSenderTgImpl implements NotificationSender {
    private final TgMessageSender messageSender;

    @Autowired
    public NotificationSenderTgImpl(TgMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void sendNotification(Notification notification) {
        var msg = new SendMessage(notification.getChatId(), notification.getMessage());
        messageSender.send(msg);
    }
}

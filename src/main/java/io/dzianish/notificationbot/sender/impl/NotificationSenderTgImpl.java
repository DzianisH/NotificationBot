package io.dzianish.notificationbot.sender.impl;

import io.dzianish.notificationbot.dto.Notification;
import io.dzianish.notificationbot.sender.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class NotificationSenderTgImpl implements NotificationSender {
    private final DefaultAbsSender absSender;

    @Autowired
    public NotificationSenderTgImpl(DefaultAbsSender absSender) {
        this.absSender = absSender;
    }

    @Override
    public void sendNotification(Notification notification) {
        var msg = new SendMessage(notification.getChatId(), notification.getMessage());
        send(msg);
    }

    private void send(SendMessage msg) {
        try {
            absSender.execute(msg);
        } catch (TelegramApiException e) {
            log.error("Can't send message in chat" + msg.getChatId(), e);
        }
    }
}

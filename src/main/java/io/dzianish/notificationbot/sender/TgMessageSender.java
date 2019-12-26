package io.dzianish.notificationbot.sender;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface TgMessageSender {
    void send(SendMessage message);
}

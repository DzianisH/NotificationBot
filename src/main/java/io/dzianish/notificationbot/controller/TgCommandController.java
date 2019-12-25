package io.dzianish.notificationbot.controller;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface TgCommandController {
    String getCommand();

    default boolean canHandle(Message message) {
        return getCommand().equals(message.getText());
    }

    void handle(Message message);
}

package io.dzianish.notificationbot.controller;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface TgMessageController {
    void handle(Message message);

    boolean canHandle(Message message);
}

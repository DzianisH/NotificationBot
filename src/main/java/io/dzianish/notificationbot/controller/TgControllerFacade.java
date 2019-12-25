package io.dzianish.notificationbot.controller;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface TgControllerFacade {
    void handleUpdate(Update update);
}

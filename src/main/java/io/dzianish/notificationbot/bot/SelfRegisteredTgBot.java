package io.dzianish.notificationbot.bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public interface SelfRegisteredTgBot {
    void register(TelegramBotsApi api) throws TelegramApiRequestException;
}
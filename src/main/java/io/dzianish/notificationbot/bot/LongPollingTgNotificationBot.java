package io.dzianish.notificationbot.bot;

import io.dzianish.notificationbot.config.TgBotConfig;
import io.dzianish.notificationbot.controller.TgControllerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

@Component
public class LongPollingTgNotificationBot extends TelegramLongPollingBot implements SelfRegistered {
    private final TgBotConfig.TgBotCredentials credentials;
    private final TgControllerFacade controllerFacade;

    @Autowired
    public LongPollingTgNotificationBot(DefaultBotOptions options, TgBotConfig.TgBotCredentials credentials, TgControllerFacade controllerFacade) {
        super(options);
        this.credentials = credentials;
        this.controllerFacade = controllerFacade;
    }

    @Override
    public void onUpdateReceived(Update update) {
        controllerFacade.handleUpdate(update);
    }

    @Override
    public String getBotUsername() {
        return credentials.getName();
    }

    @Override
    public String getBotToken() {
        return credentials.getToken();
    }

    @Override
    public void register(TelegramBotsApi api) throws TelegramApiRequestException {
        api.registerBot(this);
    }
}
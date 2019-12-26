package io.dzianish.notificationbot.sender.impl;

import io.dzianish.notificationbot.sender.TgMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TgMessageSenderImpl implements TgMessageSender {
    private final DefaultAbsSender absSender;

    @Autowired
    public TgMessageSenderImpl(DefaultAbsSender absSender) {
        this.absSender = absSender;
    }

    @Override
    public void send(SendMessage message) {
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            log.error("Can't send message in chat" + message.getChatId(), e);
        }
    }
}

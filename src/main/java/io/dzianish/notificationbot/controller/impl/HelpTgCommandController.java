package io.dzianish.notificationbot.controller.impl;

import io.dzianish.notificationbot.controller.TgCommandController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;

@Service
public class HelpTgCommandController implements TgCommandController {
    public static final String COMMAND_NAME = "/help";

    private final AbsSender absSender;

    @Autowired
    public HelpTgCommandController(AbsSender absSender) {
        this.absSender = absSender;
    }

    @Override
    public void handle(Message message) {
        var resp = new SendMessage(message.getChatId(), "help message here");

        resp.setReplyToMessageId(message.getMessageId());
        resp.setReplyMarkup(createReplyKeyboardMarkup());

        try {
            absSender.execute(resp);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        var replyMarkup = new ReplyKeyboardMarkup();

        replyMarkup.setOneTimeKeyboard(true);
        replyMarkup.setResizeKeyboard(true);
        replyMarkup.setSelective(true);
        replyMarkup.setKeyboard(createKeyBoard());

        return replyMarkup;
    }

    private List<KeyboardRow> createKeyBoard() {
        KeyboardRow row = new KeyboardRow();
        row.add("/help");
        return Collections.singletonList(row);
    }


    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }
}

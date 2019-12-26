package io.dzianish.notificationbot.controller.impl;

import io.dzianish.notificationbot.controller.TgCommandController;
import io.dzianish.notificationbot.sender.TgMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;

@Service
public class HelpController implements TgCommandController {
    private static final String COMMAND_NAME = "/help";

    private final TgMessageSender messageSender;

    @Autowired
    public HelpController(TgMessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void handle(Message message) {
        var response = new SendMessage(message.getChatId(), "help message here");

        response.setReplyToMessageId(message.getMessageId());
        response.setReplyMarkup(createReplyKeyboardMarkup());

        messageSender.send(response);
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
        row.add(getCommand());
        return Collections.singletonList(row);
    }


    @Override
    public String getCommand() {
        return COMMAND_NAME;
    }
}

package io.dzianish.notificationbot.controller.impl;

import io.dzianish.notificationbot.agent.Agent;
import io.dzianish.notificationbot.controller.TgConversationController;
import io.dzianish.notificationbot.sender.TgMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

@Service
public class ConversationControllerImpl implements TgConversationController {
    private static final String MENTION_ENTITY_TYPE = "mention";

    private final TgMessageSender messageSender;
    private final Agent agent;

    @Autowired
    public ConversationControllerImpl(TgMessageSender messageSender, Agent agent) {
        this.messageSender = messageSender;
        this.agent = agent;
    }

    @Override
    public void handle(Message message) {
        var cleanMessage = extractCleanMessage(message);
        var answer = agent.talk(cleanMessage);
        SendMessage response = buildResponse(message, answer);
        messageSender.send(response);
    }

    @Override
    public boolean canHandle(Message message) {
        boolean isGroup = isGroupMessage(message);
        if (!isGroup) {
            return true;
        }
        return message.isReply() || extractMention(message) != null;
    }


    private SendMessage buildResponse(Message request, String responseMessage) {
        var response = new SendMessage(request.getChatId(), responseMessage);
        if (isGroupMessage(request)) {
            response.setReplyToMessageId(request.getMessageId());
        }
        return response;
    }

    private boolean isGroupMessage(Message message) {
        return message.isSuperGroupMessage() || message.isChannelMessage() || message.isGroupMessage();
    }

    private String extractCleanMessage(Message message) {
        String text = message.getText();

        if (isGroupMessage(message)) {
            String mention = extractMention(message);
            if (mention != null) {
                text = text.replaceAll(mention, "");
            }
        }
        return text.trim();
    }

    private String extractMention(Message message) {
        if (message.getEntities() == null) {
            return null;
        }

        return message.getEntities().stream()
                .filter(entity -> MENTION_ENTITY_TYPE.equals(entity.getType()))
                .map(MessageEntity::getText)
                .findAny()
                .orElse(null);
    }
}

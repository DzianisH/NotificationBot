package io.dzianish.notificationbot.controller.impl;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.dzianish.notificationbot.agent.Agent;
import io.dzianish.notificationbot.agent.UserAction;
import io.dzianish.notificationbot.controller.TgConversationController;
import io.dzianish.notificationbot.dto.AgentRequest;
import io.dzianish.notificationbot.dto.AgentResponse;
import io.dzianish.notificationbot.dto.NotificationConfig;
import io.dzianish.notificationbot.sender.TgMessageSender;
import io.dzianish.notificationbot.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class ConversationControllerImpl implements TgConversationController {
    private static final String DF_DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ssXXX";
    private static final String MENTION_ENTITY_TYPE = "mention";
    private static final String CREATE_ALERT_DATE_FIELD = "date";
    private static final String CREATE_ALERT_TIME_FIELD = "time";
    private static final String CREATE_ALERT_MESSAGE_FIELD = "message";

    private final NotificationService notificationService;
    private final TgMessageSender messageSender;
    private final Agent agent;

    @Autowired
    public ConversationControllerImpl(NotificationService notificationService, TgMessageSender messageSender, Agent agent) {
        this.notificationService = notificationService;
        this.messageSender = messageSender;
        this.agent = agent;
    }

    @Override
    public void handle(Message message) {
        var agentRequest = buildAgentRequest(message);
        var agentResponse = agent.talk(agentRequest);
        if (UserAction.CREATE_ALERT == agentResponse.getUserAction()) {
            createNotification(agentResponse.getParams(), message.getChatId());
        }
        SendMessage response = buildResponse(message, agentResponse);
        messageSender.send(response);
    }

    @Override
    public boolean canHandle(Message message) {
        String cleanMessage = extractCleanMessage(message);
        if (cleanMessage == null) {
            return false;
        }

        boolean isGroup = isGroupMessage(message);
        if (!isGroup) {
            return true;
        }
        return message.isReply() || extractMention(message) != null;
    }

    private void createNotification(Struct params, Long chatId) {
        Map<String, Value> fields = params.getFieldsMap();
        NotificationConfig config = NotificationConfig.builder()
                .date(extractDate(fields))
                .message(fields.get(CREATE_ALERT_MESSAGE_FIELD).getStringValue())
                .chatId(chatId)
                .build();

        notificationService.createNotification(config);
    }

    private Date extractDate(Map<String, Value> fields) {
        var dateFormat = new SimpleDateFormat(DF_DATE_FORMAT);
        try {
            Calendar date = buildCalendar(
                    dateFormat.parse(fields.get(CREATE_ALERT_DATE_FIELD).getStringValue()));
            Calendar time = buildCalendar(
                    dateFormat.parse(fields.get(CREATE_ALERT_TIME_FIELD).getStringValue()));

            date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            date.set(Calendar.SECOND, time.get(Calendar.SECOND));
            date.set(Calendar.MILLISECOND, time.get(Calendar.MILLISECOND));
            date.set(Calendar.ZONE_OFFSET, time.get(Calendar.ZONE_OFFSET));
            date.set(Calendar.AM_PM, time.get(Calendar.AM_PM));

            return date.getTime();
        } catch (ParseException pe) {
            throw new RuntimeException("stub " + pe.getMessage(), pe);
        }
    }

    private Calendar buildCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private AgentRequest buildAgentRequest(Message message) {
        var cleanMessage = extractCleanMessage(message);

        return AgentRequest.builder()
                .text(cleanMessage)
                .languageCode(message.getFrom().getLanguageCode())
                .sessionId(createAgentSessionId(message))
                .build();
    }

    private SendMessage buildResponse(Message request, AgentResponse agentResponse) {
        float confidence = agentResponse.getConfidence() * 100;
        var userActionMsg = String.format("\n\n`user action: %s`", agentResponse.getUserAction().getName());
        var confidenceMsg = String.format("\n`confidence: %.2f%%`", confidence);

        String responseText = agentResponse.getText() + userActionMsg + confidenceMsg;
        var response = new SendMessage(request.getChatId(), responseText);
        response.enableMarkdown(true);
        if (isGroupMessage(request)) {
            response.setReplyToMessageId(request.getMessageId());
        }
        return response;
    }

    private String createAgentSessionId(Message message) {
        return message.getChatId() + ":" + message.getFrom().getId();
    }

    private boolean isGroupMessage(Message message) {
        return message.isSuperGroupMessage() || message.isChannelMessage() || message.isGroupMessage();
    }

    private String extractCleanMessage(Message message) {
        String text = message.getText();

        if (text == null) {
            return null;
        }

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

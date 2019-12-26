package io.dzianish.notificationbot.ai.impl;

import io.dzianish.notificationbot.ai.ConversationBot;
import org.springframework.stereotype.Service;

@Service
public class DialogFlowConversationBot implements ConversationBot {
    @Override
    public String talk(String phrase) {
        return "DialogFlow: " + phrase;
    }
}

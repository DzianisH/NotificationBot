package io.dzianish.notificationbot.agent.dialogflow;

import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import io.dzianish.notificationbot.dto.AgentRequest;

import java.io.IOException;

public interface SessionFactory {
    SessionsClient getSessionsClient() throws IOException;

    SessionName buildSessionName(AgentRequest request);
}

package io.dzianish.notificationbot.agent.dialogflow.impl;

import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import io.dzianish.notificationbot.agent.dialogflow.SessionFactory;
import io.dzianish.notificationbot.dto.AgentRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


//TODO: that is naive impl of non thread-safe SessionsClient pool with 1 alive connection.
@Service
public class SessionFactoryImpl implements SessionFactory {
    private final String projectId;
    private volatile SessionsClient sessionsClient;

    public SessionFactoryImpl(@Value("${agent.df.project-id}") String projectId) throws IOException {
        this.projectId = projectId;
        this.sessionsClient = buildSessionsClient();
    }

    @Override
    public SessionsClient getSessionsClient() throws IOException {
        if (sessionsClient.isTerminated() || sessionsClient.isShutdown()) {
            sessionsClient = buildSessionsClient();
        }
        return sessionsClient;
    }

    @Override
    public SessionName buildSessionName(AgentRequest request) {
        return SessionName.of(projectId, request.getSessionId());
    }

    @PreDestroy
    public void shutdownSessionClient() throws InterruptedException {
        sessionsClient.shutdownNow();
        sessionsClient.close();
        sessionsClient.awaitTermination(3L, TimeUnit.SECONDS);
    }

    private SessionsClient buildSessionsClient() throws IOException {
        return SessionsClient.create();
    }
}

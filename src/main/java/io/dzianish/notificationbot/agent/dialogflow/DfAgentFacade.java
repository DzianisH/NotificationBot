package io.dzianish.notificationbot.agent.dialogflow;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.TextInput;
import io.dzianish.notificationbot.agent.Agent;
import io.dzianish.notificationbot.agent.UserAction;
import io.dzianish.notificationbot.dto.AgentRequest;
import io.dzianish.notificationbot.dto.AgentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static io.dzianish.notificationbot.agent.UserAction.defaultAction;
import static io.dzianish.notificationbot.agent.UserAction.fromName;

@Slf4j
@Service
public class DfAgentFacade implements Agent {
    private final SessionFactory sessionFactory;

    @Autowired
    public DfAgentFacade(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public AgentResponse talk(AgentRequest request) {
        log.debug("DF REQ '{}' from {}", request.getText(), request.getSessionId());
        QueryResult queryResult = callDf(request).getQueryResult();

        AgentResponse agentResponse = buildAgentResult(request, queryResult);
        log.debug("DF RES '{}' with {}%", agentResponse.getText(), agentResponse.getConfidence() * 100);
        return agentResponse;
    }

    private DetectIntentResponse callDf(AgentRequest request) {
        QueryInput queryInput = buildQueryInput(request);

        try {
            SessionsClient sessionsClient = sessionFactory.getSessionsClient();
            return sessionsClient.detectIntent(sessionFactory.buildSessionName(request), queryInput);
        } catch (IOException ioe) {
            throw new RuntimeException("stub " + ioe.getMessage(), ioe);
        }
    }

    private AgentResponse buildAgentResult(AgentRequest request, QueryResult queryResult) {
        UserAction userAction = queryResult.getAllRequiredParamsPresent()
                ? fromName(queryResult.getAction())
                : defaultAction();

        return AgentResponse.builder()
                .text(queryResult.getFulfillmentText())
                .sessionId(request.getSessionId())
                .userAction(userAction)
                .params(queryResult.getParameters())
                .confidence(queryResult.getIntentDetectionConfidence())
                .build();
    }

    private QueryInput buildQueryInput(AgentRequest request) {
        TextInput textInput = TextInput.newBuilder()
                .setText(request.getText())
                .setLanguageCode(request.getLanguageCode())
                .build();

        return QueryInput.newBuilder()
                .setText(textInput)
                .build();
    }
}

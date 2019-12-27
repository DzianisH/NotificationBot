package io.dzianish.notificationbot.agent;

import io.dzianish.notificationbot.dto.AgentRequest;
import io.dzianish.notificationbot.dto.AgentResponse;

public interface Agent {
    AgentResponse talk(AgentRequest request);
}

package io.dzianish.notificationbot.dto;

import com.google.protobuf.Struct;
import io.dzianish.notificationbot.agent.UserAction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AgentResponse {
    private String sessionId;
    private String text;
    private UserAction userAction;
    private Struct params;
    private float confidence;
}

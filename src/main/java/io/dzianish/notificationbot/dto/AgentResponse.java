package io.dzianish.notificationbot.dto;

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
    private float confidence;
}

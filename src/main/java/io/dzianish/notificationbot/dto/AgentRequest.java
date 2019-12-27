package io.dzianish.notificationbot.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AgentRequest {
    private String sessionId;
    private String text;
    private String languageCode;

}

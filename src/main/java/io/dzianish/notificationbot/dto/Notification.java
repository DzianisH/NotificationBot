package io.dzianish.notificationbot.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Notification {
    private String message;
    private Long chatId;
}

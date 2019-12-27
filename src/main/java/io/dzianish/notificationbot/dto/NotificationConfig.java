package io.dzianish.notificationbot.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class NotificationConfig {
    private Date date;
    private String message;
    private Long chatId;
}

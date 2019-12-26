package io.dzianish.notificationbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

// TODO: make me unmodifiable
@Data
@ConfigurationProperties("tg.creds")
public class TgBotCredentials {
    private String name;
    private String token;
}

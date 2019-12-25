package io.dzianish.notificationbot;

import io.dzianish.notificationbot.config.TgBotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TgBotConfig.TgBotCredentials.class)
public class NotificationBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationBotApplication.class, args);
    }

}

package io.dzianish.notificationbot;

import io.dzianish.notificationbot.config.TgBotCredentials;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableConfigurationProperties(TgBotCredentials.class)
public class NotificationBotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(NotificationBotApplication.class, args);
    }

}

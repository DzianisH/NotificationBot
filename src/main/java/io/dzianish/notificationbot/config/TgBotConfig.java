package io.dzianish.notificationbot.config;

import io.dzianish.notificationbot.bot.SelfRegistered;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Configuration
public class TgBotConfig {
    @PostConstruct
    public void init() {
        //this should happen before any usage of telegram lib
        ApiContextInitializer.init();
    }

    @Bean
    public DefaultBotOptions createDefaultOptions() {
        return ApiContext.getInstance(DefaultBotOptions.class);
    }

    @Bean
    public TelegramBotsApi createApi(Optional<SelfRegistered> bot) throws TelegramApiRequestException {
        var api = new TelegramBotsApi();
        if (bot.isPresent()) {
            bot.get().register(api);
        }
        return api;
    }

    @Data
    @ConfigurationProperties("tg.creds")
    public static class TgBotCredentials {
        private String name;
        private String token;
    }
}

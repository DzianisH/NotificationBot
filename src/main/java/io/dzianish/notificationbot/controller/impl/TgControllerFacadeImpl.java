package io.dzianish.notificationbot.controller.impl;

import io.dzianish.notificationbot.controller.TgCommandController;
import io.dzianish.notificationbot.controller.TgControllerFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
public class TgControllerFacadeImpl implements TgControllerFacade {
    private final ApplicationContext applicationContext;

    @Autowired
    public TgControllerFacadeImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().isCommand()) {
            handleCommand(update.getMessage());
        }
    }

    private void handleCommand(Message message) {
        applicationContext.getBeansOfType(TgCommandController.class)
                .values().stream()
                .filter(bean -> bean.canHandle(message))
                .forEach(bean -> bean.handle(message));
    }
}

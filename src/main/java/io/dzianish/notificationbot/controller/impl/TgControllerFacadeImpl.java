package io.dzianish.notificationbot.controller.impl;

import io.dzianish.notificationbot.controller.TgCommandController;
import io.dzianish.notificationbot.controller.TgControllerFacade;
import io.dzianish.notificationbot.controller.TgConversationController;
import io.dzianish.notificationbot.controller.TgMessageController;
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
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.isCommand()) {
                handleMessageOfType(message, TgCommandController.class);
            } else if (message.isUserMessage()) {
                handleMessageOfType(message, TgConversationController.class);
            } else {
                handleMessage(message);
            }
        }
    }


    private void handleMessage(Message message) {
        handleMessageOfType(message, TgMessageController.class);
    }

    private void handleMessageOfType(Message message, Class<? extends TgMessageController> type) {
        applicationContext.getBeansOfType(type)
                .values().stream()
                .filter(bean -> bean.canHandle(message))
                .forEach(bean -> bean.handle(message));
    }

}

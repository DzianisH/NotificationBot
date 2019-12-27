package io.dzianish.notificationbot.agent.impl;

import io.dzianish.notificationbot.agent.Agent;
import org.springframework.stereotype.Service;

@Service
public class DfAgent implements Agent {
    @Override
    public String talk(String phrase) {
        return "DialogFlow: " + phrase;
    }
}

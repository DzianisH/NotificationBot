package io.dzianish.notificationbot.agent;

import java.util.stream.Stream;

public enum UserAction {
    CREATE_ALERT("input.create-notification"), // spring have to create notification
    DEFAULT("input.default"); // no actions from spring required

    private String name;

    UserAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static UserAction fromName(String name) {
        return Stream.of(UserAction.values())
                .filter(action -> action.name.equals(name))
                .findAny()
                .orElse(defaultAction());
    }

    public static UserAction defaultAction() {
        return DEFAULT;
    }
}

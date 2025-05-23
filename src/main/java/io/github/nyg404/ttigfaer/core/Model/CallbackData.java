package io.github.nyg404.ttigfaer.core.Model;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
@Getter
public class CallbackData {
    private final String action;

    public static CallbackData of(String action) {
        return new CallbackData(action);
    }

    private CallbackData(String action) {
        this.action = action;
    }

    public String toString() {
        if (action.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new IllegalArgumentException("CallbackData exceeds 64 bytes: " + action);
        }
        return action;
    }

    public static CallbackData fromString(String data) {
        return new CallbackData(data);
    }

}
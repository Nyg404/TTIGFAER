package io.github.nyg404.ttigfaer.core.Model;

import lombok.Getter;

import java.nio.charset.StandardCharsets;

/**
 * Модель данных для callback-запросов Telegram с ограничением длины action до 64 байт UTF-8.
 *
 * <p>Содержит строку action, которая используется как идентификатор действия при обработке callback.</p>
 */
@Getter
public class CallbackData {
    private final String action;

    /**
     * Создаёт новый объект CallbackData с указанным действием.
     *
     * @param action строка действия, ограниченная 64 байтами в UTF-8
     * @return новый объект CallbackData
     * @throws IllegalArgumentException если action превышает 64 байта в UTF-8
     */
    public static CallbackData of(String action) {
        return new CallbackData(action);
    }

    private CallbackData(String action) {
        this.action = action;
    }

    /**
     * Возвращает строковое представление action.
     * При этом проверяется, что длина в байтах не превышает 64.
     *
     * @return строка action
     * @throws IllegalArgumentException если длина action в UTF-8 больше 64 байт
     */
    public String toString() {
        if (action.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new IllegalArgumentException("CallbackData exceeds 64 bytes: " + action);
        }
        return action;
    }

    /**
     * Создаёт объект CallbackData из строки без проверки длины.
     * Предполагается, что входная строка корректна.
     *
     * @param data строка с данными callback
     * @return объект CallbackData
     */
    public static CallbackData fromString(String data) {
        return new CallbackData(data);
    }
}

package io.github.nyg404.ttigfaer.message.Utils;

import io.github.nyg404.ttigfaer.core.Manager.CallbackManager;
import io.github.nyg404.ttigfaer.core.Model.CallbackData;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class InlineKeyboardBuilder {
    private final List<InlineKeyboardRow> rows = new ArrayList<>();
    private final List<InlineKeyboardButton> currentButtons = new ArrayList<>();
    private final CallbackManager callbackManager;

    public InlineKeyboardBuilder(CallbackManager callbackManager) {
        this.callbackManager = callbackManager;
    }

    /** Начинаем новый ряд, сохраняем текущий */
    public InlineKeyboardBuilder row() {
        if (!currentButtons.isEmpty()) {
            rows.add(new InlineKeyboardRow(new ArrayList<>(currentButtons)));
            currentButtons.clear();
        }
        return this;
    }

    /** Добавляем кнопку в текущий ряд */
    public InlineKeyboardBuilder button(String text, CallbackData callbackData) {
        String data = callbackData.toString();
        if (data.getBytes(StandardCharsets.UTF_8).length > 64) {
            throw new IllegalArgumentException("CallbackData exceeds 64 bytes: " + data);
        }
        if (!callbackManager.hasHandler(callbackData.getAction())) {
            throw new IllegalArgumentException("No handler for action: " + callbackData.getAction());
        }
        InlineKeyboardButton button = InlineKeyboardButton.builder()
                .text(text)
                .callbackData(data)
                .build();

        currentButtons.add(button);
        return this;
    }

    /** Собираем клавиатуру */
    public InlineKeyboardMarkup build() {
        // Добавляем последний ряд, если есть кнопки
        if (!currentButtons.isEmpty()) {
            rows.add(new InlineKeyboardRow(new ArrayList<>(currentButtons)));
            currentButtons.clear();
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);
        markup.setKeyboard(rows);
        return markup;
    }
}


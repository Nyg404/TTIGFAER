package io.github.nyg404.ttigfaer.message.Utils;

import io.github.nyg404.ttigfaer.core.Commands.CommandExecutor;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.core.Model.CallbackData;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineKeyboardBuilder {
    private final List<InlineKeyboardRow> rows = new ArrayList<>();
    private final List<InlineKeyboardButton> currentButtons = new ArrayList<>();
    private final CommandManager commandManager;

    public InlineKeyboardBuilder(CommandManager commandManager) {
        this.commandManager = commandManager;
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
        if (!hasHandler(callbackData.getAction())) {
            throw new IllegalArgumentException("Нету обработчика для: " + callbackData.getAction());
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
        if (!currentButtons.isEmpty()) {
            rows.add(new InlineKeyboardRow(new ArrayList<>(currentButtons)));
            currentButtons.clear();
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);
        markup.setKeyboard(rows);
        return markup;
    }

    /** Проверяем наличие обработчика для callback */
    private boolean hasHandler(String action) {
        Map<String, CommandExecutor> callbackHandlers = commandManager.getHandlersByType().get(HandlerType.ON_CALLBACK_QUERY);
        return callbackHandlers != null && callbackHandlers.containsKey(action);
    }
}
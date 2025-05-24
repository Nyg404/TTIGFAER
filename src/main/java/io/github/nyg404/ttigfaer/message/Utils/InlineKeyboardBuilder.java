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

/**
 * Утилитный класс для удобного построения inline-клавиатур Telegram с проверкой callback-данных.
 *
 * <p>Позволяет создавать клавиатуру с несколькими рядами кнопок, автоматически проверяя длину
 * callbackData и наличие обработчиков для заданных действий.</p>
 *
 * <p>Использует {@link CommandManager} для проверки наличия обработчиков callback-событий.</p>
 */
public class InlineKeyboardBuilder {
    private final List<InlineKeyboardRow> rows = new ArrayList<>();
    private final List<InlineKeyboardButton> currentButtons = new ArrayList<>();
    private final CommandManager commandManager;

    /**
     * Конструктор.
     *
     * @param commandManager менеджер команд для проверки обработчиков callback-событий
     */
    public InlineKeyboardBuilder(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Начинает новый ряд кнопок. Если в текущем ряду есть кнопки, они будут добавлены в итоговую клавиатуру.
     *
     * @return текущий объект {@code InlineKeyboardBuilder} для цепочного вызова методов
     */
    public InlineKeyboardBuilder row() {
        if (!currentButtons.isEmpty()) {
            rows.add(new InlineKeyboardRow(new ArrayList<>(currentButtons)));
            currentButtons.clear();
        }
        return this;
    }

    /**
     * Добавляет кнопку в текущий ряд клавиатуры.
     *
     * <p>Проверяется, что длина callbackData в байтах не превышает 64 байта, и что для действия существует обработчик.</p>
     *
     * @param text         текст кнопки, отображаемый пользователю
     * @param callbackData данные callback, которые будут отправлены при нажатии кнопки
     * @return текущий объект {@code InlineKeyboardBuilder} для цепочного вызова методов
     * @throws IllegalArgumentException если callbackData превышает 64 байта или нет обработчика для действия
     */
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

    /**
     * Собирает и возвращает итоговую inline-клавиатуру с добавленными рядами и кнопками.
     *
     * @return объект {@link InlineKeyboardMarkup} с построенной клавиатурой
     */
    public InlineKeyboardMarkup build() {
        if (!currentButtons.isEmpty()) {
            rows.add(new InlineKeyboardRow(new ArrayList<>(currentButtons)));
            currentButtons.clear();
        }
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(rows);
        markup.setKeyboard(rows);
        return markup;
    }

    /**
     * Проверяет наличие обработчика для указанного действия callback.
     *
     * @param action действие callback (строка)
     * @return {@code true}, если обработчик для данного действия существует, иначе {@code false}
     */
    private boolean hasHandler(String action) {
        Map<String, CommandExecutor> callbackHandlers = commandManager.getHandlersByType().get(HandlerType.ON_CALLBACK_QUERY);
        return callbackHandlers != null && callbackHandlers.containsKey(action);
    }
}

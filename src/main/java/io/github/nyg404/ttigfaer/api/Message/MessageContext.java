package io.github.nyg404.ttigfaer.api.Message;

import io.github.nyg404.ttigfaer.core.Model.CallbackData;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Контекст входящего Telegram-сообщения, содержащий удобные методы и поля
 * для получения данных из {@link Update}.
 * <p>
 * Обрабатывает как обычные сообщения, так и callback'и.
 * Также предоставляет методы для получения команды, аргументов и данных callback.
 */
@Slf4j
@Getter
public class MessageContext {
    /** ID пользователя, отправившего сообщение или callback */
    private final Long userId;

    /** ID чата, откуда пришло сообщение или callback */
    private final Long chatId;

    /** Объект {@link Message}, представляющий исходное сообщение */
    private final Message message;

    /** Текст сообщения (для сообщений) или null (для callback) */
    private final String messageText;

    /** ID сообщения */
    private final Integer messageId;

    /** Аргументы команды, переданные после неё (разделённые пробелами) */
    private final List<String> messageArgs;

    /** ID сообщения, на которое был дан ответ, если есть */
    private final Integer replyToMessageId;

    /** Флаг, указывающий, содержит ли сообщение ответ на другое */
    private final boolean hasReplies;

    /** Исходный {@link Update}, полученный от Telegram API */
    private final Update rawUpdate;

    /** Префикс команды, например "/" или "!" */
    private final String prefix;

    /** Флаг, указывающий, является ли контекст callback'ом */
    private final boolean isCallback;

    /** Данные callback (action и payload), если это callback */
    private final CallbackData callbackData;


    /**
     * Создаёт новый {@link MessageContext} из {@link Update} и префикса команды.
     *
     * @param update объект обновления от Telegram
     * @param prefix префикс, с которого начинаются команды (например, "/")
     */
    public MessageContext(Update update, String prefix) {
        this.rawUpdate = update;
        this.prefix = prefix;
        this.isCallback = update.hasCallbackQuery();

        if (isCallback) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            this.message = (Message) callbackQuery.getMessage();
            this.callbackData = CallbackData.fromString(callbackQuery.getData());
            this.messageText = null; // Callback не имеет текста сообщения
            this.messageArgs = Collections.emptyList();
        } else {
            this.message = update.getMessage();
            this.callbackData = null;
            this.messageText = message.getText() != null ? message.getText() : "";
            this.messageArgs = parseArgs(messageText);
        }

        this.userId = isCallback ? update.getCallbackQuery().getFrom().getId() : message.getFrom().getId();
        this.chatId = message.getChatId();
        this.messageId = message.getMessageId();
        this.replyToMessageId = message.getReplyToMessage() != null ? message.getReplyToMessage().getMessageId() : null;
        this.hasReplies = message.getReplyToMessage() != null;
    }

    /**
     * Разбивает текст сообщения на аргументы, начиная с 1 слова после команды.
     *
     * @param text полный текст сообщения
     * @return список аргументов или пустой список, если нет аргументов
     */
    private List<String> parseArgs(String text) {
        if (text == null || text.isEmpty() || !text.startsWith(prefix)) {
            return Collections.emptyList();
        }
        String[] split = text.substring(prefix.length()).trim().split("\\s+");
        return split.length > 1 ? Arrays.asList(split).subList(1, split.length) : Collections.emptyList();
    }

    /**
     * Возвращает команду из текста сообщения (без префикса).
     *
     * @return команда в нижнем регистре или пустая строка, если команда не найдена
     */
    public String getCommand() {
        if (!isCallback && messageText != null && !messageText.isEmpty() && messageText.startsWith(prefix)) {
            String command = messageText.substring(prefix.length()).split("\\s+")[0];
            return command.toLowerCase();
        }
        return "";
    }

    /**
     * Возвращает сообщение, на которое был дан ответ (если есть).
     *
     * @return {@link Message}, на которое ответили, или null
     */
    public Message getRepliedMessage() {
        return message.getReplyToMessage();
    }

    /**
     * Возвращает действие (action) из callbackData, если это callback.
     *
     * @return действие или null, если это не callback
     */
    public String getAction() {
        return isCallback ? callbackData.getAction() : null;
    }

}
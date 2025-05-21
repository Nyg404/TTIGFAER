package io.github.nyg404.ttigfaer.api.Message;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
 * Также предоставляет методы для получения команды и аргументов.
 */
@Slf4j
@Getter
public class MessageContext {

    /** ID пользователя, отправившего сообщение */
    private final Long userId;

    /** ID чата, откуда пришло сообщение */
    private final Long chatId;

    /** Объект {@link Message}, представляющий исходное сообщение */
    private final Message message;

    /** Текст сообщения */
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

    /**
     * Создаёт новый {@link MessageContext} из {@link Update} и префикса команды.
     *
     * @param update объект обновления от Telegram
     * @param prefix префикс, с которого начинаются команды (например, "/")
     */
    public MessageContext(Update update, String prefix) {
        this.rawUpdate = update;
        this.prefix = prefix;

        Message message = extractMessage(update);
        this.userId = message.getFrom().getId();
        this.chatId = message.getChatId();
        this.message = message;
        this.messageText = message.getText() != null ? message.getText() : "";
        this.messageId = message.getMessageId();
        this.replyToMessageId = message.getReplyToMessage() != null ? message.getReplyToMessage().getMessageId() : null;
        this.hasReplies = message.getReplyToMessage() != null;
        this.messageArgs = parseArgs(messageText);
    }

    /**
     * Извлекает объект {@link Message} из {@link Update}, поддерживает сообщения и callback'и.
     *
     * @param update объект обновления
     * @return объект {@link Message}
     * @throws IllegalArgumentException если тип обновления неизвестен
     */
    private Message extractMessage(Update update) {
        if (update.hasMessage()) {
            return update.getMessage();
        } else if (update.hasCallbackQuery()) {
            return (Message) update.getCallbackQuery().getMessage();
        }
        throw new IllegalArgumentException("Неизвестный тип Update: " + update);
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
        if (messageText == null || messageText.isEmpty() || !messageText.startsWith(prefix)) {
            return "";
        }
        String command = messageText.substring(prefix.length()).split("\\s+")[0];
        return command.toLowerCase(); // Нормализация команды
    }

    /**
     * Возвращает сообщение, на которое был дан ответ (если есть).
     *
     * @return {@link Message}, на которое ответили, или null
     */
    public Message getRepliedMessage() {
        return message.getReplyToMessage();
    }
}

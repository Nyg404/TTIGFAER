package io.github.nyg404.ttigfaer.api.Message;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Getter
public class MessageContext {
    private final Long userId;
    private final Long chatId;
    private final Message message;
    private final String messageText;
    private final Integer messageId;
    private final List<String> messageArgs;
    private final Integer replyToMessageId;
    private final boolean hasReplies;
    private final Update rawUpdate;
    private final String prefix;

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

    private Message extractMessage(Update update) {
        if (update.hasMessage()) {
            return update.getMessage();
        } else if (update.hasCallbackQuery()) {
            return (Message) update.getCallbackQuery().getMessage();
        }
        throw new IllegalArgumentException("Неизвестный тип Update: " + update);
    }

    private List<String> parseArgs(String text) {
        if (text == null || text.isEmpty() || !text.startsWith(prefix)) {
            return Collections.emptyList();
        }
        String[] split = text.substring(prefix.length()).trim().split("\\s+");
        return split.length > 1 ? Arrays.asList(split).subList(1, split.length) : Collections.emptyList();
    }

    public String getCommand() {
        if (messageText == null || messageText.isEmpty() || !messageText.startsWith(prefix)) {
            return "";
        }
        String command = messageText.substring(prefix.length()).split("\\s+")[0];
        return command.toLowerCase(); // Нормализация команды
    }

    public Message getRepliedMessage() {
        return message.getReplyToMessage();
    }
}
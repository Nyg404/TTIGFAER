package io.github.nyg404.ttigfaer.message;



import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageService implements MessageServicIn {
    private final TelegramClient client;

    @Override
    public void sendMessage(MessageContext context, String text) {
        long chatId = context.getChatId();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public void sendMessage(long chatId, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(String.valueOf(chatId))  // chatId
                .text(text)  // Текст сообщения
                .build();

        try {
            client.execute(sendMessage);
            log.info("Сообщение отправлено в чат с ID: " + chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public void sendReplayMessage(MessageContext context, String text) {
        long chatId = context.getChatId();
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(context.getReplyToMessageId())
                .text(text)
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public Message sendForwardMessage(MessageContext context, long targetChatId, Message message) {

        ForwardMessage forwardMessage = ForwardMessage.builder()
                .chatId(String.valueOf(targetChatId))
                .fromChatId(String.valueOf(message.getChatId()))
                .messageId(message.getMessageId())
                .build();

        try {
            Message forwardedMessage = client.execute(forwardMessage);
            return forwardedMessage;
        } catch (TelegramApiException e) {
            return null;
        }
    }



}
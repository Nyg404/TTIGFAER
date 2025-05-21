package io.github.nyg404.ttigfaer.message;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.*;
import io.github.nyg404.ttigfaer.message.Utils.MessageOptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Сервис для отправки сообщений и мультимедиа в Telegram.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageService implements MessageServicIn {
    private final TelegramClient client;

    @Override
    public void sendMessage(MessageContext context, String text) {
        sendMessage(context.getChatId(), text, null);
    }

    @Override
    public void sendMessage(MessageContext context, String text, MessageOptions options) {
        sendMessage(context.getChatId(), text, options);
    }

    @Override
    public void sendMessage(long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    @Override
    public void sendMessage(long chatId, String text, MessageOptions options) {
        SendMessage.SendMessageBuilder builder = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text);

        MessageOptionUtils.applyMessageOptions(builder, options);

        try {
            client.execute(builder.build());
            log.info("Сообщение отправлено в чат {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public void sendReplayMessage(MessageContext context, String text) {
        sendMessage(context.getChatId(), text, MessageOptions.builder()
                .replyToMessageId(context.getReplyToMessageId())
                .build());
    }

    @Override
    public Message sendForwardMessage(MessageContext context, long targetChatId, Message message) {
        ForwardMessage forwardMessage = ForwardMessage.builder()
                .chatId(String.valueOf(targetChatId))
                .fromChatId(String.valueOf(message.getChatId()))
                .messageId(message.getMessageId())
                .protectContent(true)
                .build();

        try {
            return client.execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при пересылке сообщения: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void sendAudio(MessageContext ctx, InputFile file) {
        sendAudio(ctx.getChatId(), file, null);
    }

    @Override
    public void sendAudio(MessageContext ctx, InputFile file, AudioOptions options) {
        sendAudio(ctx.getChatId(), file, options);
    }

    @Override
    public void sendAudio(long chatId, InputFile file) {
        sendAudio(chatId, file, null);
    }

    @Override
    public void sendAudio(long chatId, InputFile file, AudioOptions options) {
        SendAudio.SendAudioBuilder builder = SendAudio.builder()
                .chatId(String.valueOf(chatId))
                .audio(file);

        MessageOptionUtils.applyAudioOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке аудио в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    public void sendPhoto(MessageContext ctx, InputFile file) {
        sendPhoto(ctx.getChatId(), file, null);
    }

    @Override
    public void sendPhoto(MessageContext ctx, InputFile file, PhotoOptions options) {
        sendPhoto(ctx.getChatId(), file, options);
    }

    @Override
    public void sendPhoto(long chatId, InputFile file) {
        sendPhoto(chatId, file, null);
    }

    @Override
    public void sendPhoto(long chatId, InputFile file, PhotoOptions options) {
        SendPhoto.SendPhotoBuilder builder = SendPhoto.builder()
                .chatId(chatId)
                .photo(file);

        MessageOptionUtils.applyPhotoOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки фотографии: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendAnimation(MessageContext ctx, InputFile file) {
        sendAnimation(ctx.getChatId(), file, null);
    }

    @Override
    public void sendAnimation(MessageContext ctx, InputFile file, AnimationOptions options) {
        sendAnimation(ctx.getChatId(), file, options);
    }

    @Override
    public void sendAnimation(long chatId, InputFile file) {
        sendAnimation(chatId, file, null);
    }


    @Override
    public void sendAnimation(long chatId, InputFile file, AnimationOptions options) {
        SendAnimation.SendAnimationBuilder builder = SendAnimation.builder()
                .chatId(chatId)
                .animation(file);

        MessageOptionUtils.applyAnimationOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке анимации: {}", e.getMessage(), e);
        }
    }

}

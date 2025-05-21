package io.github.nyg404.ttigfaer.message;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.AudioOptions;
import io.github.nyg404.ttigfaer.message.Options.MessageOptions;
import io.github.nyg404.ttigfaer.message.Options.PhotoOptions;
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

import java.awt.*;

/**
 * Сервис для отправки сообщений и мультимедиа в Telegram.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MessageService implements MessageServicIn {
    private final TelegramClient client;

    /**
     * Отправка текстового сообщения по контексту.
     */
    @Override
    public void sendMessage(MessageContext context, String text) {
        sendMessage(context.getChatId(), text, null);
    }

    /**
     * Отправка текстового сообщения по контексту с дополнительными опциями.
     */
    @Override
    public void sendMessage(MessageContext context, String text, MessageOptions options) {
        sendMessage(context.getChatId(), text, options);
    }

    /**
     * Отправка простого текстового сообщения по chatId.
     */
    @Override
    public void sendMessage(long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    /**
     * Отправка текстового сообщения по chatId с дополнительными опциями.
     */
    @Override
    public void sendMessage(long chatId, String text, MessageOptions options) {
        SendMessage.SendMessageBuilder builder = SendMessage.builder()
                .chatId(String.valueOf(chatId))
                .text(text);

        if (options != null) {
            builder.parseMode(options.getParseMode());
            builder.disableWebPagePreview(options.getDisableWebPagePreview());
            builder.disableNotification(options.getDisableNotification());
            builder.replyToMessageId(options.getReplyToMessageId());
            builder.replyMarkup(options.getReplyMarkup());
            builder.entities(options.getEntities());
            builder.allowSendingWithoutReply(options.getAllowSendingWithoutReply());
            builder.protectContent(options.getProtectContent());
            builder.linkPreviewOptions(options.getLinkPreviewOptions());
            builder.replyParameters(options.getReplyParameters());
            builder.businessConnectionId(options.getBusinessConnectionId());
            builder.messageEffectId(options.getMessageEffectId());
            builder.allowPaidBroadcast(options.getAllowPaidBroadcast());
        }

        try {
            client.execute(builder.build());
            log.info("Сообщение отправлено в чат {}", chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    /**
     * Отправка ответа на сообщение.
     */
    @Override
    public void sendReplayMessage(MessageContext context, String text) {
        sendMessage(context.getChatId(), text, MessageOptions.builder()
                .replyToMessageId(context.getReplyToMessageId())
                .build());
    }

    /**
     * Пересылка сообщения от одного пользователя другому.
     */
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

    /**
     * Отправка аудио по контексту.
     */
    @Override
    public void sendAudio(MessageContext ctx, InputFile file) {
        sendAudio(ctx.getChatId(), file, null);
    }

    /**
     * Отправка аудио по контексту с дополнительными опциями.
     */
    @Override
    public void sendAudio(MessageContext ctx, InputFile file, AudioOptions options) {
        sendAudio(ctx.getChatId(), file, options);
    }

    /**
     * Отправка аудио по chatId.
     */
    @Override
    public void sendAudio(long chatId, InputFile file) {
        sendAudio(chatId, file, null);
    }

    /**
     * Отправка аудио по chatId с дополнительными опциями.
     */
    @Override
    public void sendAudio(long chatId, InputFile file, AudioOptions options) {
        SendAudio.SendAudioBuilder builder = SendAudio.builder()
                .chatId(String.valueOf(chatId))
                .audio(file);

        if (options != null) {
            builder.messageThreadId(options.getMessageThreadId());
            builder.replyToMessageId(options.getReplyToMessageId());
            builder.disableNotification(options.getDisableNotification());
            builder.replyMarkup(options.getReplyMarkup());
            builder.performer(options.getPerformer());
            builder.title(options.getTitle());
            builder.caption(options.getCaption());
            builder.parseMode(options.getParseMode());
            builder.duration(options.getDuration());
            builder.thumbnail(options.getThumbnail());
            builder.captionEntities(options.getCaptionEntities());
            builder.allowSendingWithoutReply(options.getAllowSendingWithoutReply());
            builder.protectContent(options.getProtectContent());
            builder.replyParameters(options.getReplyParameters());
            builder.businessConnectionId(options.getBusinessConnectionId());
            builder.messageEffectId(options.getMessageEffectId());
            builder.allowPaidBroadcast(options.getAllowPaidBroadcast());
        }

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

        if (options != null) {
            builder.caption(options.getCaption());
            builder.parseMode(options.getParseMode());
            builder.captionEntities(options.getCaptionEntities());
            builder.disableNotification(options.getDisableNotification());
            builder.replyToMessageId(options.getReplyToMessageId());
            builder.replyMarkup(options.getReplyMarkup());
            builder.showCaptionAboveMedia(options.getShowCaptionAboveMedia());
            builder.protectContent(options.getProtectContent());
            builder.hasSpoiler(options.getHasSpoiler());
        }


        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки фотографии: {}", e.getMessage(), e);
        }
    }


    /**
     * Отправка анимации (GIF/видео) в чат.
     */
    @Override
    public void sendAnimation(MessageContext ctx, InputFile file) {
        SendAnimation animation = SendAnimation.builder()
                .chatId(String.valueOf(ctx.getChatId()))
                .animation(file)
                .build();

        try {
            client.execute(animation);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке анимации: {}", e.getMessage(), e);
        }
    }

}

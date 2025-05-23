package io.github.nyg404.ttigfaer.message;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.*;
import io.github.nyg404.ttigfaer.message.Utils.MessageOptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
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

//    @Override
//    public void sendMessage(MessageContext context, String text) {
//        sendMessage(context.getChatId(), text, null);
//    }
//
//    @Override
//    public void sendMessage(MessageContext context, String text, MessageOptions options) {
//        sendMessage(context.getChatId(), text, options);
//    }

    @Override
    public void sendMessage(long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    @Override
    @SuppressWarnings("all")

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
                .build();

        try {
            return client.execute(forwardMessage);
        } catch (TelegramApiException e) {
            log.error("Ошибка при пересылке сообщения: {}", e.getMessage(), e);
            return null;
        }
    }

//    @Override
//    public void sendAudio(MessageContext ctx, InputFile file) {
//        sendAudio(ctx.getChatId(), file, null);
//    }
//
//    @Override
//    public void sendAudio(MessageContext ctx, InputFile file, AudioOptions options) {
//        sendAudio(ctx.getChatId(), file, options);
//    }

    @Override
    public void sendAudio(long chatId, InputFile file) {
        sendAudio(chatId, file, null);
    }

    @Override
    @SuppressWarnings("all")
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

//    @Override
//    public void sendPhoto(MessageContext ctx, InputFile file) {
//        sendPhoto(ctx.getChatId(), file, null);
//    }
//
//    @Override
//    public void sendPhoto(MessageContext ctx, InputFile file, PhotoOptions options) {
//        sendPhoto(ctx.getChatId(), file, options);
//    }

    @Override
    public void sendPhoto(long chatId, InputFile file) {
        sendPhoto(chatId, file, null);
    }

    @Override
    @SuppressWarnings("all")
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

//    @Override
//    public void sendAnimation(MessageContext ctx, InputFile file) {
//        sendAnimation(ctx.getChatId(), file, null);
//    }
//
//    @Override
//    public void sendAnimation(MessageContext ctx, InputFile file, AnimationOptions options) {
//        sendAnimation(ctx.getChatId(), file, options);
//    }

    @Override
    public void sendAnimation(long chatId, InputFile file) {
        sendAnimation(chatId, file, null);
    }


    @Override
    @SuppressWarnings("all")
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

//    @Override
//    public void sendVideo(MessageContext ctx, InputFile file) {
//        sendVideo(ctx.getChatId(), file, null);
//    }
//
//    @Override
//    public void sendVideo(MessageContext ctx, InputFile file, VideoOptions options) {
//        sendVideo(ctx.getChatId(), file, options);
//    }

    @Override
    public void sendVideo(long chatId, InputFile file) {
        sendVideo(chatId, file, null);
    }

    @Override
    @SuppressWarnings("all")
    public void sendVideo(long chatId, InputFile file, VideoOptions options) {
        SendVideo.SendVideoBuilder builder = SendVideo.builder()
                .chatId(chatId)
                .video(file);

        MessageOptionUtils.applyVideoOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке видео: {}", e.getMessage(), e);
        }
    }

//    @Override
//    public void sendDocument(MessageContext ctx, InputFile file) {
//        sendDocument(ctx.getChatId(), file, null);
//    }
//
//    @Override
//    public void sendDocument(MessageContext ctx, InputFile file, DocumentOptions options) {
//        sendDocument(ctx.getChatId(), file, options);
//    }

    @Override
    public void sendDocument(long chatId, InputFile file) {
        sendDocument(chatId, file, null);
    }

    @Override
    @SuppressWarnings("all")
    public void sendDocument(long chatId, InputFile file, DocumentOptions options) {
        SendDocument.SendDocumentBuilder builder = SendDocument.builder()
                .chatId(chatId)
                .document(file);

        MessageOptionUtils.applyDocumentOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке документа: {}", e.getMessage(), e);
        }
    }

//    @Override
//    public void sendVoice(MessageContext ctx, InputFile file) {
//        sendVoice(ctx.getChatId(), file, null);
//    }
//
//    @Override
//    public void sendVoice(MessageContext ctx, InputFile file, VoiceOptions options) {
//        sendVoice(ctx.getChatId(), file, options);
//    }

    @Override
    public void sendVoice(long chatId, InputFile file) {
        sendVoice(chatId, file, null);
    }

    @Override
    @SuppressWarnings("all")
    public void sendVoice(long chatId, InputFile file, VoiceOptions options) {
        SendVoice.SendVoiceBuilder builder = SendVoice.builder()
                .chatId(String.valueOf(chatId))
                .voice(file);

        MessageOptionUtils.applyVoiceOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке голосового сообщения в чат {}: {}", chatId, e.getMessage(), e);
        }
    }


    // Location
//    @Override
//    public void sendLocation(MessageContext ctx, double latitude, double longitude) {
//        sendLocation(ctx.getChatId(), latitude, longitude);
//    }
//
//    @Override
//    public void sendLocation(long chatId, double latitude, double longitude) {
//        SendLocation sendLocation = SendLocation.builder()
//                .chatId(String.valueOf(chatId))
//                .latitude(latitude)
//                .longitude(longitude)
//                .build();
//
//        try {
//            client.execute(sendLocation);
//        } catch (TelegramApiException e) {
//            log.error("Ошибка при отправке геолокации в чат {}: {}", chatId, e.getMessage(), e);
//        }
//    }

//    @Override
//    public void sendStiker(MessageContext ctx, InputFile file) {
//        sendStiker(ctx.getChatId(), file);
//    }

    @Override
    public void sendStiker(long chatID, InputFile file) {
        SendSticker sendSticker = SendSticker.builder()
                .chatId(chatID)
                .sticker(file)
                .build();
        try {
            client.execute(sendSticker);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке геолокации в чат {}: {}", chatID, e.getMessage(), e);
        }
    }

    @Override
    public void editText(long chatId, int messageId, String text) {
        editText(chatId, messageId, text, null);
    }

    @Override
    @SuppressWarnings("all")
    public void editText(long chatId, int messageId, String text, EditTextOptions options) {
        EditMessageText.EditMessageTextBuilder builder = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text);
        MessageOptionUtils.applyEditTextOptions(builder, options);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при изменения сообщения {}: {}", chatId, e.getMessage(), e);
        }
    }

    @Override
    @SuppressWarnings("all")
    public void editMedia(long chatId, int messageID, InputMedia file) {
        editMedia(chatId, messageID, file, null);
    }

    @Override
    @SuppressWarnings("all")
    public void editMedia(long chadId, int messageID, InputMedia file, EditMediaOptions options) {
        EditMessageMedia.EditMessageMediaBuilder builder = EditMessageMedia.builder()
                .chatId(chadId)
                .media(file)
                .messageId(messageID);
        MessageOptionUtils.applyEditMediaOptions(builder, options);
        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при изменения сообщения {}: {}", chadId, e.getMessage(), e);
        }
    }

}

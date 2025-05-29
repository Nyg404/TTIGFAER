package io.github.nyg404.ttigfaer.message.Manager;

import io.github.nyg404.ttigfaer.api.Interface.async.MessageServiceAsync;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.api.Interface.MessageService;
import io.github.nyg404.ttigfaer.message.Options.*;
import io.github.nyg404.ttigfaer.message.Utils.MessageOptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Сервис для отправки и редактирования сообщений, а также мультимедийного контента
 * в Telegram через Telegram Bot API.
 *
 * <p>Поддерживает отправку текстовых сообщений, аудио, фото, видео, документов,
 * голосовых сообщений, стикеров и анимаций. Также реализует методы пересылки сообщений
 * и редактирования уже отправленных сообщений (текста и медиа).</p>
 *
 * <p>Использует {@link TelegramClient} для выполнения запросов к Telegram API и
 * обрабатывает возможные исключения {@link TelegramApiException} с логированием ошибок.</p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MessageManager implements MessageService, MessageServiceAsync {
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
    /**
     * Отправляет текстовое сообщение в указанный чат.
     * @param chatId ID чата
     * @param text текст сообщения
     */
    @Override
    public void sendMessage(long chatId, String text) {
        sendMessage(chatId, text, null);
    }

    /**
     * Отправляет текстовое сообщение в указанный чат с дополнительными опциями.
     * @param chatId ID чата
     * @param text текст сообщения
     * @param options дополнительные опции для сообщения
     */
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

    /**
     * Отправляет ответное сообщение (reply) в чат из контекста.
     * @param context контекст сообщения
     * @param text текст ответа
     */
    @Override
    public void sendReplayMessage(MessageContext context, String text) {
        sendMessage(context.getChatId(), text, MessageOptions.builder()
                .replyToMessageId(context.getReplyToMessageId())
                .build());
    }

    /**
     * Пересылает сообщение в другой чат.
     * @param context контекст исходного сообщения
     * @param targetChatId ID целевого чата
     * @param message сообщение для пересылки
     * @return пересланное сообщение или null в случае ошибки
     */
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

    /**
     * Отправляет аудиофайл в указанный чат.
     * @param chatId ID чата
     * @param file аудиофайл
     */
    @Override
    public void sendAudio(long chatId, InputFile file) {
        sendAudio(chatId, file, null);
    }

    /**
     * Отправляет аудиофайл в указанный чат с опциями.
     * @param chatId ID чата
     * @param file аудиофайл
     * @param options дополнительные опции для аудио
     */
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

    /**
     * Отправляет фотографию в указанный чат.
     * @param chatId ID чата
     * @param file файл фотографии
     */
    @Override
    public void sendPhoto(long chatId, InputFile file) {
        sendPhoto(chatId, file, null);
    }

    /**
     * Отправляет фотографию в указанный чат с опциями.
     * @param chatId ID чата
     * @param file файл фотографии
     * @param options дополнительные опции для фотографии
     */
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

    /**
     * Отправляет анимацию в указанный чат.
     * @param chatId ID чата
     * @param file файл анимации
     */
    @Override
    public void sendAnimation(long chatId, InputFile file) {
        sendAnimation(chatId, file, null);
    }

    /**
     * Отправляет анимацию в указанный чат с опциями.
     * @param chatId ID чата
     * @param file файл анимации
     * @param options дополнительные опции для анимации
     */
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

    /**
     * Отправляет видео в указанный чат.
     * @param chatId ID чата
     * @param file видеофайл
     */
    @Override
    public void sendVideo(long chatId, InputFile file) {
        sendVideo(chatId, file, null);
    }

    /**
     * Отправляет видео в указанный чат с опциями.
     * @param chatId ID чата
     * @param file видеофайл
     * @param options дополнительные опции для видео
     */
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

    /**
     * Отправляет документ в указанный чат.
     * @param chatId ID чата
     * @param file файл документа
     */
    @Override
    public void sendDocument(long chatId, InputFile file) {
        sendDocument(chatId, file, null);
    }

    /**
     * Отправляет документ в указанный чат с опциями.
     * @param chatId ID чата
     * @param file файл документа
     * @param options дополнительные опции для документа
     */
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
    /**
     * Отправляет голосовое сообщение в указанный чат.
     * @param chatId ID чата
     * @param file аудиофайл голосового сообщения
     */
    @Override
    public void sendVoice(long chatId, InputFile file) {
        sendVoice(chatId, file, null);
    }

    /**
     * Отправляет голосовое сообщение в указанный чат с опциями.
     * @param chatId ID чата
     * @param file аудиофайл голосового сообщения
     * @param options дополнительные опции для голосового сообщения
     */
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

    /**
     * Отправляет в чат стикер.
     * @param chatId Id чата
     * @param file file
     */
    @Override
    public void sendSticker(long chatId, InputFile file){
        sendSticker(chatId, file, null);
    }

    /**
     * Отправляет в чат стикер с опциями.
     * @param chatId Id чата
     * @param file file
     * @param options опции
     */
    @Override
    @SuppressWarnings("all")
    public void sendSticker(long chatId, InputFile file, StickerOptions options) {
        SendSticker.SendStickerBuilder builder = SendSticker.builder()
                .chatId(chatId)
                .sticker(file);
        MessageOptionUtils.applyStickerOptions(builder, options, file);

        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке геолокации в чат {}: {}", chatId, e.getMessage(), e);
        }
    }

    /**
     * Супер-метод позволяющий отправлять медя в группу. 10 изображений, документов и т.д в одном сообщении
     * @param chatId Id чата куда
     * @param groupMedia Группа типа
     */
    @Override
    public void sendMediaGroup(long chatId, Collection<? extends InputMedia> groupMedia){
        sendMediaGroup(chatId, groupMedia, null);
    }

    /**
     * Супер-метод позволяющий отправлять медя в группу. 10 изображений, документов и т.д в одном сообщении
     * @param chatId Id чата куда
     * @param groupMedia Группа типа
     * @param options опции по желанию.
     */
    @Override
    @SuppressWarnings("all")
    public void sendMediaGroup(long chatId, Collection<? extends InputMedia> groupMedia, MediaOptions options) {
        if (groupMedia == null || groupMedia.isEmpty()) {
            log.warn("Попытка отправки пустой медиа-группы в чат {}", chatId);
            return;
        }

        SendMediaGroup.SendMediaGroupBuilder builder = SendMediaGroup.builder()
                .chatId(String.valueOf(chatId))
                .medias(new ArrayList<>(groupMedia));

        MessageOptionUtils.applyMediaOptions(builder, options);

        try {
            client.execute(builder.build());
            log.info("Медиа-группа ({} элементов) отправлена в чат {}", groupMedia.size(), chatId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке медиа-группы в чат {}: {}", chatId, e.getMessage(), e);
            throw new RuntimeException("Не удалось отправить медиа-группу в чат " + chatId, e);
        }
    }

    /**
     * Редактирует текст сообщения.
     * @param chatId ID чата
     * @param messageId ID сообщения для редактирования
     * @param text новый текст сообщения
     */
    @Override
    public void editText(long chatId, int messageId, String text) {
        editText(chatId, messageId, text, null);
    }

    /**
     * Редактирует текст сообщения.
     * @param chatId ID чата
     * @param messageId ID сообщения для редактирования
     * @param text новый текст сообщения
     * @param options опции.
     */
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

    /**
     * Редактирует медиа сообщения.
     * @param chatId ID чата
     * @param messageId ID сообщения для редактирования
     * @param file новое медиа
     */
    @Override
    @SuppressWarnings("all")
    public void editMedia(long chatId, int messageId, InputMedia file) {
        editMedia(chatId, messageId, file, null);
    }

    /**
     * Изменяет мультимедийное сообщение в чате по заданному идентификатору сообщения.
     *
     * @param chatId  Идентификатор чата, в котором находится сообщение.
     * @param messageId Идентификатор сообщения, которое нужно изменить.
     * @param file Новый объект мультимедиа (InputMedia) для замены текущего содержимого.
     * @param options Дополнительные опции для редактирования сообщения.
     */
    @Override
    @SuppressWarnings("all")
    public void editMedia(long chatId, int messageId, InputMedia file, EditMediaOptions options) {
        EditMessageMedia.EditMessageMediaBuilder builder = EditMessageMedia.builder()
                .chatId(String.valueOf(chatId))
                .messageId(messageId)
                .media(file);
        MessageOptionUtils.applyEditMediaOptions(builder, options);
        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при изменении сообщения {}: {}", chatId, e.getMessage(), e);
        }
    }

    /**
     * Удаляет сообщение в чате по заданому идентификатору сообщения.
     * @param chatId  Идентификатор чата, в котором находися сообщение
     * @param messageID  Идентификатор сообщение, которое нужно изменить
     */
    @Override
    @SuppressWarnings("all")
    public void deleteMessage(long chatId, int messageID){
        DeleteMessage.DeleteMessageBuilder builder = DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageID);
        try {
            client.execute(builder.build());
        } catch (TelegramApiException e) {
            log.error("Ошибка при удалении сообщения {}: {}", chatId, e.getMessage(), e);
        }
    }

    /**
     * Асинхронно отправить текстовое сообщение в чат.
     *
     * @param chatId ID чата
     * @param text   Текст сообщения
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendMessageAsync(long chatId, String text) {
        return sendMessageAsync(chatId, text, null);
    }
    /**
     * Асинхронно отправить текстовое сообщение в чат с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param text    Текст сообщения
     * @param options Опции сообщения
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendMessageAsync(long chatId, String text, MessageOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendMessage.SendMessageBuilder builder = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(text);

            MessageOptionUtils.applyMessageOptions(builder, options);

            try {
                client.execute(builder.build());
                log.info("Асинхронно отправлено сообщение в чат {}", chatId);
            } catch (TelegramApiException e) {
                log.error("Ошибка при асинхронной отправке сообщения в чат {}: {}", chatId, e.getMessage(), e);
                throw new RuntimeException(e); // Проброс ошибки в CompletableFuture
            }
        });
    }


    /**
     * Асинхронно отправить ответ на сообщение в контексте.
     *
     * @param context Контекст сообщения
     * @param text    Текст ответа
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendReplayMessageAsync(MessageContext context, String text) {
        return CompletableFuture.runAsync(() -> sendMessage(context.getChatId(), text, MessageOptions.builder()
                .replyToMessageId(context.getReplyToMessageId())
                .build()));
    }

    /**
     * Асинхронно переслать сообщение из одного чата в другой.
     *
     * @param context     Контекст исходного сообщения
     * @param targetChatId ID целевого чата
     * @param message     Сообщение для пересылки
     * @return CompletableFuture с пересланным сообщением
     */
    @Override
    public CompletableFuture<Message> sendForwardMessageAsync(MessageContext context, long targetChatId, Message message) {
        return CompletableFuture.supplyAsync(() -> {
            ForwardMessage forwardMessage = ForwardMessage.builder()
                    .chatId(String.valueOf(targetChatId))
                    .fromChatId(String.valueOf(message.getChatId()))
                    .messageId(message.getMessageId())
                    .build();

            try {
                Message sentMessage = client.execute(forwardMessage);
                log.info("Сообщение переслано в чат {}", targetChatId);
                return sentMessage;
            } catch (TelegramApiException e) {
                log.error("Ошибка при пересылке сообщения в чат {}: {}", targetChatId, e.getMessage(), e);
                throw new RuntimeException("Ошибка при пересылке сообщения", e);
            }
        });
    }


    /**
     * Асинхронно отправить аудио файл.
     *
     * @param chatId ID чата
     * @param file   Файл аудио
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendAudioAsync(long chatId, InputFile file) {
        return sendAudioAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить аудио файл с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл аудио
     * @param options Опции аудио
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendAudioAsync(long chatId, InputFile file, AudioOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendAudio.SendAudioBuilder builder = SendAudio.builder()
                    .chatId(String.valueOf(chatId))
                    .audio(file);

            MessageOptionUtils.applyAudioOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке аудио в чат {}: {}", chatId, e.getMessage(), e);
                throw new RuntimeException("Ошибка при пересылке сообщения", e);
            }
        });
    }

    /**
     * Асинхронно отправить фото.
     *
     * @param chatId ID чата
     * @param file   Файл фото
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendPhotoAsync(long chatId, InputFile file) {
        return sendPhotoAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить фото с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл фото
     * @param options Опции фото
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendPhotoAsync(long chatId, InputFile file, PhotoOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendPhoto.SendPhotoBuilder builder = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(file);

            MessageOptionUtils.applyPhotoOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка отправки фотографии: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно отправить анимацию.
     *
     * @param chatId ID чата
     * @param file   Файл анимации
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendAnimationAsync(long chatId, InputFile file) {
        return sendAnimationAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить анимацию с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл анимации
     * @param options Опции анимации
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendAnimationAsync(long chatId, InputFile file, AnimationOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendAnimation.SendAnimationBuilder builder = SendAnimation.builder()
                    .chatId(chatId)
                    .animation(file);

            MessageOptionUtils.applyAnimationOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке анимации: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно отправить видео.
     *
     * @param chatId ID чата
     * @param file   Файл видео
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendVideoAsync(long chatId, InputFile file) {
        return sendVideoAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить видео с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл видео
     * @param options Опции видео
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendVideoAsync(long chatId, InputFile file, VideoOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendVideo.SendVideoBuilder builder = SendVideo.builder()
                    .chatId(chatId)
                    .video(file);

            MessageOptionUtils.applyVideoOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке видео: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно отправить документ.
     *
     * @param chatId ID чата
     * @param file   Файл документа
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendDocumentAsync(long chatId, InputFile file) {
        return sendDocumentAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить документ с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл документа
     * @param options Опции документа
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendDocumentAsync(long chatId, InputFile file, DocumentOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendDocument.SendDocumentBuilder builder = SendDocument.builder()
                    .chatId(chatId)
                    .document(file);

            MessageOptionUtils.applyDocumentOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке документа: {}", e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно отправить голосовое сообщение.
     *
     * @param chatId ID чата
     * @param file   Файл голоса
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendVoiceAsync(long chatId, InputFile file) {
        return sendVoiceAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить голосовое сообщение с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл голоса
     * @param options Опции голосового сообщения
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendVoiceAsync(long chatId, InputFile file, VoiceOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendVoice.SendVoiceBuilder builder = SendVoice.builder()
                    .chatId(String.valueOf(chatId))
                    .voice(file);

            MessageOptionUtils.applyVoiceOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке голосового сообщения в чат {}: {}", chatId, e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно отправить стикер.
     *
     * @param chatId ID чата
     * @param file   Файл стикера
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendStickerAsync(long chatId, InputFile file) {
        return sendStickerAsync(chatId, file, null);
    }

    /**
     * Асинхронно отправить стикер с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл стикера
     * @param options Опции стикера
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendStickerAsync(long chatId, InputFile file, StickerOptions options) {
        return CompletableFuture.runAsync(() -> {
            SendSticker.SendStickerBuilder builder = SendSticker.builder()
                    .chatId(chatId)
                    .sticker(file);
            MessageOptionUtils.applyStickerOptions(builder, options, file);

            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке геолокации в чат {}: {}", chatId, e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно отправить группу медиа (несколько медиа файлов в одном сообщении).
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов для отправки
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    public CompletableFuture<Void> sendMediaGroupAsync(long chatId, Collection<? extends InputMedia> groupMedia) {
        return sendMediaGroupAsync(chatId, groupMedia, null);
    }

    /**
     * Асинхронно отправить группу медиа с дополнительными опциями.
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов
     * @param options    Опции медиа группы
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> sendMediaGroupAsync(long chatId, Collection<? extends InputMedia> groupMedia, MediaOptions options) {
        return CompletableFuture.runAsync(() -> {
            if (groupMedia == null || groupMedia.isEmpty()) {
                log.warn("Попытка отправки пустой медиа-группы в чат {}", chatId);
                return;
            }

            SendMediaGroup.SendMediaGroupBuilder builder = SendMediaGroup.builder()
                    .chatId(String.valueOf(chatId))
                    .medias(new ArrayList<>(groupMedia));

            MessageOptionUtils.applyMediaOptions(builder, options);

            try {
                client.execute(builder.build());
                log.info("Медиа-группа ({} элементов) отправлена в чат {}", groupMedia.size(), chatId);
            } catch (TelegramApiException e) {
                log.error("Ошибка при отправке медиа-группы в чат {}: {}", chatId, e.getMessage(), e);
                throw new RuntimeException("Не удалось отправить медиа-группу в чат " + chatId, e);
            }
        });
    }

    /**
     * Асинхронно отредактировать текст сообщения.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    @Override
    public CompletableFuture<Void> editTextAsync(long chatId, int messageId, String text) {
        return editTextAsync(chatId, messageId, text, null);
    }

    /**
     * Асинхронно отредактировать текст сообщения с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     * @param options   Опции редактирования текста
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> editTextAsync(long chatId, int messageId, String text, EditTextOptions options) {
        return CompletableFuture.runAsync(() -> {
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
        });
    }


    /**
     * Асинхронно отредактировать медиа сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    @Override
    public CompletableFuture<Void> editMediaAsync(long chatId, int messageId, InputMedia file) {
        return editMediaAsync(chatId, messageId, file, null);
    }

    /**
     * Асинхронно отредактировать медиа сообщение с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     * @param options   Опции редактирования медиа
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> editMediaAsync(long chatId, int messageId, InputMedia file, EditMediaOptions options) {
        return CompletableFuture.runAsync(() -> {
            EditMessageMedia.EditMessageMediaBuilder builder = EditMessageMedia.builder()
                    .chatId(String.valueOf(chatId))
                    .messageId(messageId)
                    .media(file);
            MessageOptionUtils.applyEditMediaOptions(builder, options);
            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при изменении сообщения {}: {}", chatId, e.getMessage(), e);
            }
        });
    }

    /**
     * Асинхронно удалить сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @return CompletableFuture, завершающийся по окончании удаления
     */
    @Override
    @SuppressWarnings("all")
    public CompletableFuture<Void> deleteMessageAsync(long chatId, int messageId) {
        return CompletableFuture.runAsync(() -> {
            DeleteMessage.DeleteMessageBuilder builder = DeleteMessage.builder()
                    .chatId(chatId)
                    .messageId(messageId);
            try {
                client.execute(builder.build());
            } catch (TelegramApiException e) {
                log.error("Ошибка при удалении сообщения {}: {}", chatId, e.getMessage(), e);
            }
        });
    }
}

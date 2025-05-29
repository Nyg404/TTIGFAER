package io.github.nyg404.ttigfaer.api.Interface.async;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Асинхронный интерфейс для отправки и управления сообщениями в Telegram.
 * Все методы возвращают CompletableFuture для неблокирующей работы.
 */
@SuppressWarnings("all")
public interface MessageServiceAsync {

    /**
     * Асинхронно отправить текстовое сообщение в чат.
     *
     * @param chatId ID чата
     * @param text   Текст сообщения
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendMessageAsync(long chatId, String text);

    /**
     * Асинхронно отправить текстовое сообщение в чат с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param text    Текст сообщения
     * @param options Опции сообщения
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendMessageAsync(long chatId, String text, MessageOptions options);

    /**
     * Асинхронно отправить ответ на сообщение в контексте.
     *
     * @param context Контекст сообщения
     * @param text    Текст ответа
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendReplayMessageAsync(MessageContext context, String text);

    /**
     * Асинхронно переслать сообщение из одного чата в другой.
     *
     * @param context     Контекст исходного сообщения
     * @param targetChaId ID целевого чата
     * @param message     Сообщение для пересылки
     * @return CompletableFuture с пересланным сообщением
     */
    CompletableFuture<Message> sendForwardMessageAsync(MessageContext context, long targetChaId, Message message);

    /**
     * Асинхронно отправить аудио файл.
     *
     * @param chatId ID чата
     * @param file   Файл аудио
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendAudioAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить аудио файл с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл аудио
     * @param options Опции аудио
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendAudioAsync(long chatId, InputFile file, AudioOptions options);

    /**
     * Асинхронно отправить фото.
     *
     * @param chatId ID чата
     * @param file   Файл фото
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendPhotoAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить фото с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл фото
     * @param options Опции фото
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendPhotoAsync(long chatId, InputFile file, PhotoOptions options);

    /**
     * Асинхронно отправить анимацию.
     *
     * @param chatId ID чата
     * @param file   Файл анимации
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendAnimationAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить анимацию с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл анимации
     * @param options Опции анимации
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendAnimationAsync(long chatId, InputFile file, AnimationOptions options);

    /**
     * Асинхронно отправить видео.
     *
     * @param chatId ID чата
     * @param file   Файл видео
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendVideoAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить видео с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл видео
     * @param options Опции видео
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendVideoAsync(long chatId, InputFile file, VideoOptions options);

    /**
     * Асинхронно отправить документ.
     *
     * @param chatId ID чата
     * @param file   Файл документа
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendDocumentAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить документ с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл документа
     * @param options Опции документа
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendDocumentAsync(long chatId, InputFile file, DocumentOptions options);

    /**
     * Асинхронно отправить голосовое сообщение.
     *
     * @param chatId ID чата
     * @param file   Файл голоса
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendVoiceAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить голосовое сообщение с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл голоса
     * @param options Опции голосового сообщения
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendVoiceAsync(long chatId, InputFile file, VoiceOptions options);

    /**
     * Асинхронно отправить стикер.
     *
     * @param chatId ID чата
     * @param file   Файл стикера
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendStickerAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить стикер с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл стикера
     * @param options Опции стикера
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendStickerAsync(long chatId, InputFile file, StickerOptions options);

    /**
     * Асинхронно отправить группу медиа (несколько медиа файлов в одном сообщении).
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов для отправки
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendMediaGroupAsync(long chatId, Collection<? extends InputMedia> groupMedia);

    /**
     * Асинхронно отправить группу медиа с дополнительными опциями.
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов
     * @param options    Опции медиа группы
     * @return CompletableFuture, завершающийся по окончании отправки
     */
    CompletableFuture<Void> sendMediaGroupAsync(long chatId, Collection<? extends InputMedia> groupMedia, MediaOptions options);

    /**
     * Асинхронно отредактировать текст сообщения.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    CompletableFuture<Void> editTextAsync(long chatId, int messageId, String text);

    /**
     * Асинхронно отредактировать текст сообщения с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     * @param options   Опции редактирования текста
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    CompletableFuture<Void> editTextAsync(long chatId, int messageId, String text, EditTextOptions options);

    /**
     * Асинхронно отредактировать медиа сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     * @return CompletableFuture, завершающийся по окончании редактирования
     */
    CompletableFuture<Void> editMediaAsync(long chatId, int messageId, InputMedia file);

    /**
     * Асинхронно отредактировать медиа сообщение с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     * @param options   Опции редактирования медиа
     * @return CompletableFuture, завершающийся по окончании редактирования
     */


    CompletableFuture<Void> editMediaAsync(long chatId, int messageId, InputMedia file, EditMediaOptions options);

    /**
     * Асинхронно удалить сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @return CompletableFuture, завершающийся по окончании удаления
     */
    CompletableFuture<Void> deleteMessageAsync(long chatId, int messageId);

}

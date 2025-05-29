package io.github.nyg404.ttigfaer.api.Interface.async;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * Асинхронный интерфейс для отправки и управления сообщениями в Telegram.
 * Все методы используют ExecutorService для асинхронного выполнения.
 */
@SuppressWarnings("all")
public interface MessageServiceAsync {

    /**
     * Асинхронно отправить текстовое сообщение в чат.
     *
     * @param chatId ID чата
     * @param text   Текст сообщения
     */
    void sendMessageAsync(long chatId, String text);

    /**
     * Асинхронно отправить текстовое сообщение в чат с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param text    Текст сообщения
     * @param options Опции сообщения
     */
    void sendMessageAsync(long chatId, String text, MessageOptions options);

    /**
     * Асинхронно отправить ответ на сообщение в контексте.
     *
     * @param context Контекст сообщения
     * @param text    Текст ответа
     */
    void sendReplayMessageAsync(MessageContext context, String text);

    /**
     * Асинхронно переслать сообщение из одного чата в другой.
     *
     * @param context     Контекст исходного сообщения
     * @param targetChatId ID целевого чата
     * @param message     Сообщение для пересылки
     * @return Future с пересланным сообщением
     */
    Future<Message> sendForwardMessageAsync(MessageContext context, long targetChatId, Message message);

    /**
     * Асинхронно отправить аудио файл.
     *
     * @param chatId ID чата
     * @param file   Файл аудио
     */
    void sendAudioAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить аудио файл с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл аудио
     * @param options Опции аудио
     */
    void sendAudioAsync(long chatId, InputFile file, AudioOptions options);

    /**
     * Асинхронно отправить фото.
     *
     * @param chatId ID чата
     * @param file   Файл фото
     */
    void sendPhotoAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить фото с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл фото
     * @param options Опции фото
     */
    void sendPhotoAsync(long chatId, InputFile file, PhotoOptions options);

    /**
     * Асинхронно отправить анимацию.
     *
     * @param chatId ID чата
     * @param file   Файл анимации
     */
    void sendAnimationAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить анимацию с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл анимации
     * @param options Опции анимации
     */
    void sendAnimationAsync(long chatId, InputFile file, AnimationOptions options);

    /**
     * Асинхронно отправить видео.
     *
     * @param chatId ID чата
     * @param file   Файл видео
     */
    void sendVideoAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить видео с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл видео
     * @param options Опции видео
     */
    void sendVideoAsync(long chatId, InputFile file, VideoOptions options);

    /**
     * Асинхронно отправить документ.
     *
     * @param chatId ID чата
     * @param file   Файл документа
     */
    void sendDocumentAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить документ с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл документа
     * @param options Опции документа
     */
    void sendDocumentAsync(long chatId, InputFile file, DocumentOptions options);

    /**
     * Асинхронно отправить голосовое сообщение.
     *
     * @param chatId ID чата
     * @param file   Файл голоса
     */
    void sendVoiceAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить голосовое сообщение с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл голоса
     * @param options Опции голосового сообщения
     */
    void sendVoiceAsync(long chatId, InputFile file, VoiceOptions options);

    /**
     * Асинхронно отправить стикер.
     *
     * @param chatId ID чата
     * @param file   Файл стикера
     */
    void sendStickerAsync(long chatId, InputFile file);

    /**
     * Асинхронно отправить стикер с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param file    Файл стикера
     * @param options Опции стикера
     */
    void sendStickerAsync(long chatId, InputFile file, StickerOptions options);

    /**
     * Асинхронно отправить группу медиа (несколько медиа файлов в одном сообщении).
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов для отправки
     */
    void sendMediaGroupAsync(long chatId, Collection<? extends InputMedia> groupMedia);

    /**
     * Асинхронно отправить группу медиа с дополнительными опциями.
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов
     * @param options    Опции медиа группы
     */
    void sendMediaGroupAsync(long chatId, Collection<? extends InputMedia> groupMedia, MediaOptions options);

    /**
     * Асинхронно отредактировать текст сообщения.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     */
    void editTextAsync(long chatId, int messageId, String text);

    /**
     * Асинхронно отредактировать текст сообщения с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     * @param options   Опции редактирования текста
     */
    void editTextAsync(long chatId, int messageId, String text, EditTextOptions options);

    /**
     * Асинхронно отредактировать медиа сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     */
    void editMediaAsync(long chatId, int messageId, InputMedia file);

    /**
     * Асинхронно отредактировать медиа сообщение с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     * @param options   Опции редактирования медиа
     */
    void editMediaAsync(long chatId, int messageId, InputMedia file, EditMediaOptions options);

    /**
     * Асинхронно удалить сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     */
    void deleteMessageAsync(long chatId, int messageId);
}
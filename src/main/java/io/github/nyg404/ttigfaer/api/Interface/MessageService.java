package io.github.nyg404.ttigfaer.api.Interface;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Collection;

/**
 * Интерфейс для отправки и управления сообщениями в Telegram.
 * Поддерживает различные типы сообщений и операции редактирования/удаления.
 */
@SuppressWarnings("all")
public interface MessageService {

    /**
     * Отправить текстовое сообщение в чат.
     *
     * @param chatId ID чата
     * @param text   Текст сообщения
     */
    void sendMessage(long chatId, String text);

    /**
     * Отправить текстовое сообщение в чат с дополнительными опциями.
     *
     * @param chatId  ID чата
     * @param text    Текст сообщения
     * @param options Опции сообщения
     */
    void sendMessage(long chatId, String text, MessageOptions options);

    /**
     * Отправить ответ на сообщение в контексте.
     *
     * @param context Контекст сообщения
     * @param text    Текст ответа
     */
    void sendReplayMessage(MessageContext context, String text);

    /**
     * Переслать сообщение из одного чата в другой.
     *
     * @param context     Контекст исходного сообщения
     * @param targetChaId ID целевого чата
     * @param message     Сообщение для пересылки
     * @return Пересланное сообщение
     */
    Message sendForwardMessage(MessageContext context, long targetChaId, Message message);

    /**
     * Отправить аудио файл.
     *
     * @param chatId ID чата
     * @param file   Файл аудио
     */
    void sendAudio(long chatId, InputFile file);

    /**
     * Отправить аудио файл с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл аудио
     * @param options Опции аудио
     */
    void sendAudio(long chatId, InputFile file, AudioOptions options);

    /**
     * Отправить фото.
     *
     * @param chatId ID чата
     * @param file   Файл фото
     */
    void sendPhoto(long chatId, InputFile file);

    /**
     * Отправить фото с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл фото
     * @param options Опции фото
     */
    void sendPhoto(long chatId, InputFile file, PhotoOptions options);

    /**
     * Отправить анимацию.
     *
     * @param chatId ID чата
     * @param file   Файл анимации
     */
    void sendAnimation(long chatId, InputFile file);

    /**
     * Отправить анимацию с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл анимации
     * @param options Опции анимации
     */
    void sendAnimation(long chatId, InputFile file, AnimationOptions options);

    /**
     * Отправить видео.
     *
     * @param chatId ID чата
     * @param file   Файл видео
     */
    void sendVideo(long chatId, InputFile file);

    /**
     * Отправить видео с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл видео
     * @param options Опции видео
     */
    void sendVideo(long chatId, InputFile file, VideoOptions options);

    /**
     * Отправить документ.
     *
     * @param chatId ID чата
     * @param file   Файл документа
     */
    void sendDocument(long chatId, InputFile file);

    /**
     * Отправить документ с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл документа
     * @param options Опции документа
     */
    void sendDocument(long chatId, InputFile file, DocumentOptions options);

    /**
     * Отправить голосовое сообщение.
     *
     * @param chatId ID чата
     * @param file   Файл голоса
     */
    void sendVoice(long chatId, InputFile file);

    /**
     * Отправить голосовое сообщение с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл голоса
     * @param options Опции голосового сообщения
     */
    void sendVoice(long chatId, InputFile file, VoiceOptions options);

    /**
     * Отправить стикер.
     *
     * @param chatId ID чата
     * @param file   Файл стикера
     */
    void sendSticker(long chatId, InputFile file);

    /**
     * Отправить стикер с дополнительными опциями.
     *
     * @param chatId ID чата
     * @param file   Файл стикера
     * @param options Опции стикера
     */
    void sendSticker(long chatId, InputFile file, StickerOptions options);

    /**
     * Отправить группу медиа (несколько медиа файлов в одном сообщении).
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов для отправки
     */
    void sendMediaGroup(long chatId, Collection<? extends InputMedia> groupMedia);

    /**
     * Отправить группу медиа с дополнительными опциями.
     *
     * @param chatId     ID чата
     * @param groupMedia Коллекция медиа объектов
     * @param options    Опции медиа группы
     */
    void sendMediaGroup(long chatId, Collection<? extends InputMedia> groupMedia, MediaOptions options);

    /**
     * Отредактировать текст сообщения.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     */
    void editText(long chatId, int messageId, String text);

    /**
     * Отредактировать текст сообщения с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param text      Новый текст сообщения
     * @param options   Опции редактирования текста
     */
    void editText(long chatId, int messageId, String text, EditTextOptions options);

    /**
     * Отредактировать медиа сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     */
    void editMedia(long chatId, int messageId, InputMedia file);

    /**
     * Отредактировать медиа сообщение с дополнительными опциями.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения
     * @param file      Новый медиа файл
     * @param options   Опции редактирования медиа
     */
    void editMedia(long chatId, int messageId, InputMedia file, EditMediaOptions options);

    /**
     * Удалить сообщение.
     *
     * @param chatId    ID чата
     * @param messageId ID сообщения для удаления
     */
    void deleteMessage(long chatId, int messageId);
}

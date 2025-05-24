package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для отправки аудиофайла через Telegram-бот.
 * Позволяет настраивать параметры сообщения с аудио.
 */
@Data
@Builder
public class AudioOptions {

    /** ID ветки обсуждения (треда). */
    private Integer messageThreadId;

    /** ID сообщения, на которое будет дан ответ. */
    private Integer replyToMessageId;

    /** Отключить уведомления для этого сообщения. */
    private Boolean disableNotification;

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** Исполнитель аудиофайла. */
    private String performer;

    /** Название трека. */
    private String title;

    /** Подпись к аудиофайлу. */
    private String caption;

    /** Режим парсинга подписи (Markdown, HTML и др.). */
    private String parseMode;

    /** Длительность аудио в секундах. */
    private Integer duration;

    /** Миниатюра для аудио. */
    private InputFile thumbnail;

    /** Список сущностей для форматирования подписи. */
    private List<MessageEntity> captionEntities;

    /** Разрешить отправку без ответа. */
    private Boolean allowSendingWithoutReply;

    /** Защитить содержимое сообщения от пересылки. */
    private Boolean protectContent;

    /** Параметры ответа для сообщения. */
    private ReplyParameters replyParameters;

    /** ID бизнес-соединения для рассылок. */
    private String businessConnectionId;

    /** ID визуального эффекта для сообщения. */
    private String messageEffectId;

    /** Разрешить платную рассылку сообщения. */
    private Boolean allowPaidBroadcast;
}

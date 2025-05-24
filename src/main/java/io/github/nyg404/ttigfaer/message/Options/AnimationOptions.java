package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для отправки анимации (GIF) через Telegram-бот.
 * Используется для настройки параметров сообщения с анимацией.
 */
@Data
@Builder
public class AnimationOptions {

    /** Длительность анимации в секундах. */
    private Integer duration;

    /** Ширина анимации в пикселях. */
    private Integer width;

    /** Высота анимации в пикселях. */
    private Integer height;

    /** Подпись к анимации. */
    private String caption;

    /** Режим парсинга подписи (Markdown, HTML и др.). */
    private String parseMode;

    /** Список сущностей для форматирования подписи. */
    private List<MessageEntity> captionEntities;

    /** Миниатюра для анимации. */
    private InputFile thumbnail;

    /** Отключить уведомления для этого сообщения. */
    private Boolean disableNotification;

    /** Защитить содержимое сообщения от пересылки. */
    private Boolean protectContent;

    /** Пометить сообщение как спойлер. */
    private Boolean hasSpoiler;

    /** Показывать подпись над медиа. */
    private Boolean showCaptionAboveMedia;

    /** Разрешить отправку без ответа. */
    private Boolean allowSendingWithoutReply;

    /** Разрешить платную рассылку сообщения. */
    private Boolean allowPaidBroadcast;

    /** ID сообщения, на которое будет дан ответ. */
    private Integer replyToMessageId;

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** ID бизнес-соединения для рассылок. */
    private String businessConnectionId;

    /** ID визуального эффекта для сообщения. */
    private String messageEffectId;
}

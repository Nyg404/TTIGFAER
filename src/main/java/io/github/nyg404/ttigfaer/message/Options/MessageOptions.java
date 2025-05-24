package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для отправки текстового сообщения.
 * Позволяют управлять уведомлениями, предпросмотром ссылок, форматированием, ответами, клавиатурой и другими параметрами.
 */
@Data
@Builder
public class MessageOptions {

    /** Режим парсинга текста (Markdown, HTML, MarkdownV2). */
    private String parseMode;

    /** Отключить предпросмотр ссылок. */
    private Boolean disableWebPagePreview;

    /** Отключить уведомления для этого сообщения. */
    private Boolean disableNotification;

    /** ID сообщения, на которое будет дан ответ. */
    private Integer replyToMessageId;

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** Список сущностей для форматирования текста. */
    private List<MessageEntity> entities;

    /** Разрешить отправку без ответа. */
    private Boolean allowSendingWithoutReply;

    /** Защитить содержимое сообщения от пересылки. */
    private Boolean protectContent;

    /** Настройки предпросмотра ссылок. */
    private LinkPreviewOptions linkPreviewOptions;

    /** Параметры ответа для сообщения. */
    private ReplyParameters replyParameters;

    /** ID бизнес-соединения для рассылок. */
    private String businessConnectionId;

    /** ID визуального эффекта для сообщения. */
    private String messageEffectId;

    /** Разрешить платную рассылку сообщения. */
    private Boolean allowPaidBroadcast;
}

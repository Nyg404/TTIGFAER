package io.github.nyg404.ttigfaer.message.Options;


import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Опции для отправки текстового сообщения.
 * Позволяют управлять уведомлениями, предпросмотром ссылок, форматированием, ответами и клавиатурой.
 */
@Data
@Builder
public class MessageOptions {
    /**
     * Отключает уведомления при отправке сообщения.
     */
    private boolean disableNotification;

    /**
     * Отключает предпросмотр веб-страниц по ссылкам.
     */
    private boolean disableWebPagePreview;

    /**
     * Форматирование текста (например, Markdown или HTML).
     */
    private String parseMode;

    /**
     * ID сообщения, на которое нужно ответить.
     */
    private Integer replyToMessageId;

    /**
     * Объект клавиатуры, который будет прикреплён к сообщению.
     */
    private ReplyKeyboard replyMarkup;
}



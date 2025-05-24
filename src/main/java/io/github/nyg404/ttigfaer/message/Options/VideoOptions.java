package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для отправки видео-сообщения.
 * Позволяют задавать описание, предпросмотр, форматирование, уведомления и другие параметры.
 */
@Data
@Builder
public class VideoOptions {

    /** Подпись к видео. */
    private String caption;

    /** Сущности для форматирования подписи (жирный, курсив и др.). */
    private List<MessageEntity> captionEntities;

    /** Режим форматирования текста (Markdown, HTML, MarkdownV2). */
    private String parseMode;

    /** Продолжительность видео в секундах. */
    private Integer duration;

    /** Ширина видео. */
    private Integer width;

    /** Высота видео. */
    private Integer height;

    /** Поддерживает ли видео потоковую передачу. */
    private Boolean supportsStreaming;

    /** Миниатюра (превью) для видео. */
    private InputFile thumbnail;

    /** Отключить уведомление о новом сообщении. */
    private Boolean disableNotification;

    /** ID сообщения, на которое дан ответ. */
    private Integer replyToMessageId;

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** Разрешить отправку без обязательного ответа. */
    private Boolean allowSendingWithoutReply;

    /** Защитить содержимое от пересылки. */
    private Boolean protectContent;
}

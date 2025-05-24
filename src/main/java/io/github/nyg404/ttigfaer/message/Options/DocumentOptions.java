package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для отправки документа через Telegram-бот.
 * Позволяет настраивать параметры сообщения с документом.
 */
@Data
@Builder
public class DocumentOptions {

    /** Подпись к документу. */
    private String caption;

    /** Список сущностей для форматирования подписи. */
    private List<MessageEntity> captionEntities;

    /** Режим парсинга подписи (Markdown, HTML и др.). */
    private String parseMode;

    /** Миниатюра для документа. */
    private InputFile thumbnail;

    /** Отключить уведомления для этого сообщения. */
    private Boolean disableNotification;

    /** ID сообщения, на которое будет дан ответ. */
    private Integer replyToMessageId;

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** Разрешить отправку без ответа. */
    private Boolean allowSendingWithoutReply;

    /** Защитить содержимое сообщения от пересылки. */
    private Boolean protectContent;
}

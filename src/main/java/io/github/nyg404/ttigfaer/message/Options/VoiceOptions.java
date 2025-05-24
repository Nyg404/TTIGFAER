package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

/**
 * Опции для отправки голосового сообщения (voice).
 * Позволяют настраивать описание, длительность, уведомления, кнопки и другие параметры.
 */
@Data
@Builder
public class VoiceOptions {

    /** Подпись к голосовому сообщению. */
    private String caption;

    /** Сущности для форматирования подписи (жирный, курсив и др.). */
    @Singular
    private List<MessageEntity> captionEntities;

    /** Длительность голосового сообщения в секундах. */
    private Integer duration;

    /** Отключить уведомление о новом сообщении. */
    private Boolean disableNotification;

    /** ID сообщения, на которое дан ответ. */
    private Integer replyToMessageId;

    /** Инлайн-клавиатура для сообщения. */
    private InlineKeyboardMarkup replyMarkup;

    /** Разрешить отправку без обязательного ответа. */
    private Boolean allowSendingWithoutReply;

    /** Защитить содержимое от пересылки. */
    private Boolean protectContent;
}

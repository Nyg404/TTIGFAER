package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Опции для отправки стикера через Telegram-бот.
 * Используется для настройки параметров сообщения со стикером.
 */
@Data
@Builder
public class StickerOptions {

    /** Идентификатор чата или username канала */
    private String chatId;

    /** Идентификатор топика сообщения в форуме (для супергрупп с форумами) */
    private Integer messageThreadId;

    /** Стикер (file_id, URL или загружаемый файл) */
    private InputFile sticker;

    /** Отключить уведомления */
    private Boolean disableNotification;

    /** ID сообщения, на которое отвечаем */
    private Integer replyToMessageId;

    /** Клавиатура или кнопки для сообщения */
    private ReplyKeyboard replyMarkup;

    /** Разрешить отправку, если сообщение для ответа не найдено */
    private Boolean allowSendingWithoutReply;

    /** Защитить содержимое сообщения от пересылки */
    private Boolean protectContent;

    /** Emoji, связанное со стикером (для загружаемых стикеров) */
    private String emoji;

    /** Параметры ответа */
    private ReplyParameters replyParameters;

    /** Идентификатор бизнес-соединения */
    private String businessConnectionId;

    /** Идентификатор эффекта для сообщения */
    private String messageEffectId;

    /** Разрешить платную рассылку (до 1000 сообщений в секунду) */
    private Boolean allowPaidBroadcast;
}


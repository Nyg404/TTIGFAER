package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.List;

@Data
@Builder
public class PhotoOptions {

    /**
     * Подпись к фото (caption)
     */
    private String caption;

    /**
     * Режим форматирования подписи (Markdown, HTML и т.п.)
     */
    private String parseMode;

    /**
     * Список спец. сущностей в подписи (например, ссылки, жирный шрифт)
     */
    private List<MessageEntity> captionEntities;

    /**
     * Отключить уведомления при отправке
     */
    private Boolean disableNotification;

    /**
     * ID сообщения, на которое делается ответ (reply)
     */
    private Integer replyToMessageId;

    /**
     * Клавиатура (inline или обычная)
     */
    private ReplyKeyboard replyMarkup;

    /**
     * Показывать подпись выше фото
     */
    private Boolean showCaptionAboveMedia;

    /**
     * Защитить сообщение от пересылки и сохранения
     */
    private Boolean protectContent;

    /**
     * Фото с эффектом спойлера
     */
    private Boolean hasSpoiler;
}

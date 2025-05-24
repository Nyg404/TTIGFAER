package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * Опции для редактирования медиа-сообщения (видео).
 * Позволяет изменить клавиатуру и бизнес-соединение.
 */
@Data
@Builder
public class EditMediaOptions {

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** ID бизнес-соединения для рассылок. */
    private String businessConnectionId;
}

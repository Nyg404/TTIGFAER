package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;

/**
 * Опции для отправки аудио.
 * Позволяют задавать настройки отправки аудиофайла: уведомления, форматирование и заголовок.
 */
@Data
@Builder
public class AudioOptions {
    /**
     * Отключает уведомления при отправке аудио.
     */
    private boolean disableNotification;

    /**
     * Форматирование текста (например, Markdown или HTML).
     */
    private String parseMode;

    /**
     * Заголовок аудиофайла.
     */
    private String title;
}

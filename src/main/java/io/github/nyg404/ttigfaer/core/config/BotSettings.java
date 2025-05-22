package io.github.nyg404.ttigfaer.core.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки бота, получаемые из application.yml по префиксу {@code bot}.
 */
@Data
@ConfigurationProperties(prefix = "bot")
public class BotSettings {

    /**
     * Токен авторизации Telegram-бота.
     */
    @NotBlank(message = "Токен бота не может быть пустым.")
    private String token;

    /**
     * Префикс команд, которые распознаёт бот.
     */
    @NotBlank(message = "Префикс бота не может быть пустым")
    private String prefix;
}

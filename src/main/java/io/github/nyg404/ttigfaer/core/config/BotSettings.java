package io.github.nyg404.ttigfaer.core.config;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "bot")
public class BotSettings {
    @NotBlank(message =  "Токен бота не может быть пустым.")
    private String token;
    @NotBlank(message = "Префикс бота не может быть пустым")
    private String prefix;

}

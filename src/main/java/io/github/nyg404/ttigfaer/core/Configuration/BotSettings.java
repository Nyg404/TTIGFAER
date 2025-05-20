package io.github.nyg404.ttigfaer.core.Configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "bot")
public class BotSettings {
    private String token;
    private String prefix;



}

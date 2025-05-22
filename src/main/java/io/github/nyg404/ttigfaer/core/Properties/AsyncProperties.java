package io.github.nyg404.ttigfaer.core.Properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = "ttigfaer.async")
public class AsyncProperties {
    @Min(value = 1, message = "Минимальное количество пулов равняется 1")
    private int corePoolSize = 4;

    @Min(value = 1, message = "Максимально минимальное общие количество пулов равняетяс 1")
    private int maxPoolSize = 10;

    private int queueCapacity = 50;

    @NotNull(message = "Префикс не может быть пустым.")
    private String threadNamePrefix = "Async-";

}

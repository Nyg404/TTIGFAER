package io.github.nyg404.ttigfaer.core.Properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки асинхронного исполнителя, загружаемые из {@code ttigfaer.async.*}.
 */
@Data
@ConfigurationProperties(prefix = "ttigfaer.async")
public class AsyncProperties {

    /**
     * Минимальное количество потоков в пуле.
     */
    @Min(value = 1, message = "Минимальное количество пулов равняется 1")
    private int corePoolSize = 4;

    /**
     * Максимальное количество потоков в пуле.
     */
    @Min(value = 1, message = "Максимально минимальное общее количество пулов равняется 1")
    private int maxPoolSize = 10;

    /**
     * Вместимость очереди задач.
     */
    private int queueCapacity = 50;

    /**
     * Префикс имен потоков.
     */
    @NotNull(message = "Префикс не может быть пустым.")
    private String threadNamePrefix = "Async-";
}

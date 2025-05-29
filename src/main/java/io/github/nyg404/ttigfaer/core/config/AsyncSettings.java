package io.github.nyg404.ttigfaer.core.config;

import io.github.nyg404.ttigfaer.core.Properties.AsyncProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;

/**
 * Конфигурация асинхронного исполнителя задач.
 * Использует свойства из {@link AsyncProperties}.
 */
@RequiredArgsConstructor
@Configuration
public class AsyncSettings {
    private final AsyncProperties asyncProperties;

    /**
     * Создаёт bean {@link ThreadPoolTaskExecutor}, если он ещё не определён.
     *
     * @return настроенный {@link ThreadPoolTaskExecutor}
     */
    @Bean(name = "asyncExecutor")
    @ConditionalOnMissingBean(name = "asyncExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService executorService(@Qualifier("asyncExecutor") ThreadPoolTaskExecutor taskExecutor) {
        return taskExecutor.getThreadPoolExecutor();
    }

}

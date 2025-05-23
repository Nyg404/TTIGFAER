package io.github.nyg404.ttigfaer.core.config;

import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.core.Properties.AsyncProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * Основная автоконфигурация библиотеки TTIGFAER.
 * Инициализирует Telegram клиента, менеджер команд и асинхронные настройки.
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties({BotSettings.class, AsyncProperties.class})
@ComponentScan(basePackages = "io.github.nyg404.ttigfaer")
@Import(AsyncSettings.class)
public class TtigfaerAutoConfiguration {

    /**
     * Создаёт клиента Telegram на основе токена.
     *
     * @param botSettings настройки бота
     * @return Telegram-клиент
     */
    @Bean
    public TelegramClient telegramClient(BotSettings botSettings) {
        return new OkHttpTelegramClient(botSettings.getToken());
    }

    /**
     * Регистрирует {@link CommandManager} с доступными обработчиками команд и исполнителем.
     *
     * @param handlers список обработчиков
     * @param asyncExecutor асинхронный исполнитель
     * @return {@link CommandManager}
     */
    @Bean
    public CommandManager commandManager(List<CommandHandler> handlers, @Qualifier("asyncExecutor") Executor asyncExecutor) {
        return new CommandManager(handlers, asyncExecutor);
    }
}

package io.github.nyg404.ttigfaer.core;

import io.github.nyg404.ttigfaer.core.config.BotSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
/**
 * Конфигурация Spring для регистрации кастомного Telegram бота.
 * Создаёт и регистрирует бота в TelegramBotsLongPollingApplication,
 * используя настройки из BotSettings и бин CustomBot.
 */
@Slf4j
@Configuration
public class RegisterCustomBot {

    /**
     * Создаёт и настраивает экземпляр TelegramBotsLongPollingApplication с кастомным ботом.
     *
     * @param botSettings настройки бота (токен, префикс и т.д.)
     * @param bots провайдер кастомных ботов (CustomBot)
     * @return настроенное приложение TelegramBotsLongPollingApplication
     * @throws TelegramApiException если произошла ошибка при регистрации бота
     */
    @Bean
    public TelegramBotsLongPollingApplication application(
            BotSettings botSettings,
            ObjectProvider<CustomBot> bots
    ) throws TelegramApiException {
        TelegramBotsLongPollingApplication application = new TelegramBotsLongPollingApplication();
        CustomBot customBot = bots.getIfAvailable();
        if(customBot != null){
            application.registerBot(botSettings.getToken(), customBot);
            log.info("Бот был запущен. {}", botSettings.getToken());
        } else {
            log.error("Бот не был запущен, не найден класс CustomBot");
        }
        return application;
    }
}

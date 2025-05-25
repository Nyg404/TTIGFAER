package io.github.nyg404.ttigfaer.core.config;

import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.core.Properties.AsyncProperties;
import io.github.nyg404.ttigfaer.core.Utils.ArgumentRegistry;
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

@Configuration
@EnableAsync
@EnableConfigurationProperties({BotSettings.class, AsyncProperties.class})
@ComponentScan(basePackages = "io.github.nyg404.ttigfaer")
@Import(AsyncSettings.class)
public class TtigfaerAutoConfiguration {


    @Bean
    public TelegramClient telegramClient(BotSettings botSettings) {
        return new OkHttpTelegramClient(botSettings.getToken());
    }

    @Bean
    public CommandManager commandManager(List<CommandHandler> handlers,
                                         @Qualifier("asyncExecutor") Executor asyncExecutor,
                                         ArgumentRegistry argumentRegistry) {
        return new CommandManager(handlers, asyncExecutor, argumentRegistry);
    }


}

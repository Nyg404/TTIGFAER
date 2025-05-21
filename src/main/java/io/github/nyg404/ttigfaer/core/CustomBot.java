package io.github.nyg404.ttigfaer.core;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Commands.CommandManager;
import io.github.nyg404.ttigfaer.core.Configuration.BotSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Главный класс регеструриющий бота.
 */
@Slf4j
@RequiredArgsConstructor
public abstract class CustomBot implements LongPollingSingleThreadUpdateConsumer {

    private final BotSettings botSettings;
    private final CommandManager commandManager;

    @Override
    public void consume(Update update) {
        try {
            MessageContext ctx = new MessageContext(update, botSettings.getPrefix());
            commandManager.dispatch(ctx);

        } catch (Exception e) {
            log.error("Ошибка при обработке обновления", e);
        }
    }
}

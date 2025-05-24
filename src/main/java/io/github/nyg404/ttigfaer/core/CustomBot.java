package io.github.nyg404.ttigfaer.core;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.core.config.BotSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Абстрактный базовый класс для кастомного Telegram бота.
 * Реализует интерфейс {@link LongPollingSingleThreadUpdateConsumer} для обработки обновлений.
 *
 * <p>Отвечает за получение обновлений из Telegram и их передачу в {@link CommandManager}
 * через контекст {@link MessageContext}.</p>
 *
 * <p>Все исключения при обработке логируются.</p>
 */
@Slf4j
@RequiredArgsConstructor
public abstract class CustomBot implements LongPollingSingleThreadUpdateConsumer {

    private final BotSettings botSettings;
    private final CommandManager commandManager;

    /**
     * Метод для обработки каждого обновления из Telegram.
     * Создаёт {@link MessageContext} и передаёт его в {@link CommandManager}.
     *
     * @param update обновление из Telegram
     */
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

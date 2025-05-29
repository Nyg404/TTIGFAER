package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.message.Manager.MessageManager;
import io.github.nyg404.ttigfaer.message.Manager.ModerationManager;
import org.springframework.context.annotation.Lazy;

/**
 * Абстрактный базовый класс для обработчиков команд.
 * Содержит ссылки на сервис сообщений и менеджер команд.
 * Позволяет реализовывать конкретные обработчики, используя готовые зависимости.
 */
@SuppressWarnings("all")
public abstract class CommandHelper implements CommandHandler {
    protected final MessageManager messageManager;
    protected final CommandManager commandManager;
    protected final ModerationManager moderationManager;

    /**
     * Конструктор CommandHelper.
     *
     * @param messageManager сервис для работы с сообщениями
     * @param commandManager менеджер команд
     */
    public CommandHelper(MessageManager messageManager, @Lazy CommandManager commandManager, ModerationManager moderationManager) {
        this.messageManager = messageManager;
        this.commandManager = commandManager;
        this.moderationManager = moderationManager;
    }
}

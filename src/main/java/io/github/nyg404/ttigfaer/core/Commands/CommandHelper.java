package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.message.MessageService;
import org.springframework.context.annotation.Lazy;

/**
 * Абстрактный базовый класс для обработчиков команд.
 * Содержит ссылки на сервис сообщений и менеджер команд.
 * Позволяет реализовывать конкретные обработчики, используя готовые зависимости.
 */
public abstract class CommandHelper implements CommandHandler {
    protected final MessageService messageService;
    protected final CommandManager commandManager;

    /**
     * Конструктор CommandHelper.
     *
     * @param messageService сервис для работы с сообщениями
     * @param commandManager менеджер команд
     */
    public CommandHelper(MessageService messageService, @Lazy CommandManager commandManager) {
        this.messageService = messageService;
        this.commandManager = commandManager;
    }
}

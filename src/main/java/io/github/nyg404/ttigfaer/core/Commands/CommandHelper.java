package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.core.Manager.CommandManager;
import io.github.nyg404.ttigfaer.message.MessageService;
import org.springframework.context.annotation.Lazy;

public abstract class CommandHelper implements CommandHandler {
    protected final MessageService messageService;
    protected final CommandManager commandManager;


    public CommandHelper(MessageService messageService, @Lazy CommandManager commandManager) {
        this.messageService = messageService;
        this.commandManager = commandManager;
    }
}

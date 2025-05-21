package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Annotations.*;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CommandManager {
    private final Map<String, CommandExecutor> commands = new HashMap<>();
    private final Map<String, CommandExecutor> textHandlers = new HashMap<>();
    private final Map<String, CommandExecutor> botMessageResponders = new HashMap<>();
    private final Map<String, CommandExecutor> messageHandlers = new HashMap<>();

    private final List<CommandHandler> handlers;

    public CommandManager(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }

    @PostConstruct
    public void init() {
        for (CommandHandler handler : handlers) {
            for (Method method : handler.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Handler.class)) {
                    Handler annotation = method.getAnnotation(Handler.class);
                    HandlerType type = annotation.value();

                    if (!isValidCommandMethod(method)) {
                        log.warn("Неверная сигнатура метода {} в классе {}", method.getName(), handler.getClass().getSimpleName());
                        continue;
                    }

                    CommandExecutor executor = new CommandExecutor(handler, method);

                    String k = handler.getClass().getName() + "#" + method.getName();
                    switch (type) {
                        case REGISTER_COMMAND -> {
                            String[] cmds = annotation.commands();
                            if(cmds.length == 0){
                                log.error("Для REGISTER_COMMAND не было указанно команд в методе: {}", method.getName(), handler.getClass().getSimpleName());
                            } else {
                                commands.put(k, executor);
                            }
                        }
                        case RESPOND_TO_BOT_MESSAGE -> {
                            botMessageResponders.put(k, executor);
                            log.info("Зарегистрирован ответ на сообщение бота: {} из класса {}", method.getName(), handler.getClass().getSimpleName());
                        }
                        case ON_TEXT -> {
                            textHandlers.put(k, executor);
                            log.info("Зарегистрирован обработчик текста: {} из класса {}", method.getName(), handler.getClass().getSimpleName());
                        }
                        case ON_MESSAGE -> {
                            messageHandlers.put(k, executor);
                            log.info("Зарегистрирован обработчик сообщений: {} из класса {}", method.getName(), handler.getClass().getSimpleName());
                        }
                    }
                }
            }
        }
    }


    private boolean isValidCommandMethod(Method method) {
        return method.getParameterCount() == 1 && method.getParameterTypes()[0] == MessageContext.class;
    }

    public void dispatch(MessageContext ctx) {
        if (ctx.getMessage().getReplyToMessage() != null
                && ctx.getMessage().getReplyToMessage().getFrom() != null
                && ctx.getMessage().getReplyToMessage().getFrom().getIsBot()) {
            botMessageResponders.forEach((key, executor) -> executor.invoke(ctx));
        }

        if (ctx.getMessage() != null && (ctx.getMessage().hasAudio() || ctx.getMessage().hasDocument()
                || ctx.getMessage().hasPhoto() || ctx.getMessage().hasText()
                || ctx.getMessage().hasAnimation() || ctx.getMessage().hasDice())) {
            messageHandlers.forEach((key, executor) -> executor.invoke(ctx));
        }

        if (ctx.getMessageText() != null && !ctx.getMessageText().isEmpty()) {
            textHandlers.forEach((key, executor) -> executor.invoke(ctx));
        }

        String command = ctx.getCommand();
        if (command != null && !command.isEmpty()) {
            CommandExecutor executor = commands.get(command);
            if (executor != null) {
                executor.invoke(ctx);
            }
        }
    }

    private static class CommandExecutor {
        private final Object bean;
        private final Method method;

        public CommandExecutor(Object bean, Method method) {
            this.bean = bean;
            this.method = method;
        }

        public void invoke(MessageContext ctx) {
            try {
                method.invoke(bean, ctx);
            } catch (Exception e) {
                log.error("Ошибка при выполнении команды: {}", ctx.getCommand(), e);
            }
        }
    }
}

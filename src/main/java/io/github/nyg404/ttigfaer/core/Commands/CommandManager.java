package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Annotations.*;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CommandManager {
    private final Map<HandlerType, Map<String, CommandExecutor>> handlersByType = new HashMap<>();


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

                    handlersByType.computeIfAbsent(type, k -> new HashMap<>());

                    if (type == HandlerType.REGISTER_COMMAND) {
                        String[] cmds = annotation.commands();
                        if (cmds.length == 0) {
                            log.error("Для REGISTER_COMMAND не было указано команд в методе: {}", method.getName());
                        } else {
                            for (String cmd : cmds) {
                                handlersByType.get(type).put(cmd, executor);
                                log.info("Зарегистрирована команда: /{} из метода {} в классе {}", cmd, method.getName(), handler.getClass().getSimpleName());
                            }
                        }
                    } else {
                        String key = handler.getClass().getName() + "#" + method.getName();
                        handlersByType.get(type).put(key, executor);
                        log.info("Зарегистрирован обработчик {} из класса {}", method.getName(), handler.getClass().getSimpleName());
                    }
                }
            }
        }
    }



    private boolean isValidCommandMethod(Method method) {
        return method.getParameterCount() == 1 && method.getParameterTypes()[0] == MessageContext.class;
    }

    public void dispatch(MessageContext ctx) {
        // 1) Ответ на сообщение бота
        if (ctx.getMessage().getReplyToMessage() != null
                && ctx.getMessage().getReplyToMessage().getFrom() != null
                && ctx.getMessage().getReplyToMessage().getFrom().getIsBot()) {
            invokeHandlers(HandlerType.RESPOND_TO_BOT_MESSAGE, ctx);
        }

        // 2) Любое сообщение с контентом
        if (ctx.getMessage() != null && (ctx.getMessage().hasAudio() || ctx.getMessage().hasDocument()
                || ctx.getMessage().hasPhoto() || ctx.getMessage().hasText()
                || ctx.getMessage().hasAnimation() || ctx.getMessage().hasDice()
                || ctx.getMessage().hasVideo() || ctx.getMessage().hasVoice()
                || ctx.getMessage().hasLocation() || ctx.getMessage().hasSticker())) {
            invokeHandlers(HandlerType.ON_MESSAGE, ctx);
        }

        // 3) Только текст
        if (ctx.getMessageText() != null && !ctx.getMessageText().isEmpty()) {
            invokeHandlers(HandlerType.ON_TEXT, ctx);
        }

        // 4) Команда (вызываем один обработчик по ключу команды)
        String command = ctx.getCommand();
        if (command != null && !command.isEmpty()) {
            Map<String, CommandExecutor> commandHandlers = handlersByType.get(HandlerType.REGISTER_COMMAND);
            if (commandHandlers != null) {
                CommandExecutor executor = commandHandlers.get(command);
                if (executor != null) {
                    executor.invoke(ctx);
                }
            }
        }
    }

    private void invokeHandlers(HandlerType type, MessageContext ctx) {
        Map<String, CommandExecutor> map = handlersByType.get(type);
        if (map == null) return;

        for (Map.Entry<String, CommandExecutor> entry : map.entrySet()) {
            try {
                entry.getValue().invoke(ctx);
            } catch (Exception e) {
                log.error("Ошибка в обработчике {} при обработке сообщения: {}", entry.getKey(), ctx, e);
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

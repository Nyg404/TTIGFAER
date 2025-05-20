package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Annotations.OnMessage;
import io.github.nyg404.ttigfaer.api.Annotations.OnText;
import io.github.nyg404.ttigfaer.api.Annotations.RegisterCommand;
import io.github.nyg404.ttigfaer.api.Annotations.RespondToBotMessage;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
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
        for (Object bean : handlers) {
            for (Method method : bean.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(RegisterCommand.class)) {
                    RegisterCommand annotation = method.getAnnotation(RegisterCommand.class);
                    String commandName = annotation.value();

                    if (!isValidCommandMethod(method)) {
                        log.warn("Метод {} в классе {} имеет неверную сигнатуру для команды {}",
                                method.getName(), bean.getClass().getSimpleName(), commandName);
                        continue;
                    }

                    commands.put(commandName, new CommandExecutor(bean, method));
                    log.info("Зарегистрирована команда: {} из класса {}", commandName, bean.getClass().getSimpleName());
                }

                if (method.isAnnotationPresent(RespondToBotMessage.class)) {
                    botMessageResponders.put("respondToBotMessage", new CommandExecutor(bean, method));
                    log.info("Зарегистрирован метод для ответа на сообщение бота: {} из класса {}", method.getName(), bean.getClass().getSimpleName());
                }

                if (method.isAnnotationPresent(OnText.class)) {
                    textHandlers.put("onText", new CommandExecutor(bean, method));
                    log.info("Зарегистрирован метод для обработки текста: {} из класса {}", method.getName(), bean.getClass().getSimpleName());
                }

                if (method.isAnnotationPresent(OnMessage.class)) {
                    messageHandlers.put("OnMessage", new CommandExecutor(bean, method));
                    log.info("Зарегистрирован метод для обработки сообщений: {} из класса {}", method.getName(), bean.getClass().getSimpleName());
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

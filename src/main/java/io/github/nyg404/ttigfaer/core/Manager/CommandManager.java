package io.github.nyg404.ttigfaer.core.Manager;

import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
import io.github.nyg404.ttigfaer.api.Annotations.TimeBot;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Component
@Slf4j
public class CommandManager {
    private final Map<HandlerType, Map<String, CommandExecutor>> handlersByType = new HashMap<>();
    private final List<CommandHandler> handlers;
    private final Executor asyncExecutor;

    public CommandManager(List<CommandHandler> handlers, @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.handlers = handlers;
        this.asyncExecutor = asyncExecutor;
    }

    @PostConstruct
    public void init() {
        log.info("Начало регистрации обработчиков для {} бинов", handlers.size());
        for (CommandHandler handler : handlers) {
            Class<?> targetClass = AopUtils.getTargetClass(handler);
            log.info("Сканирование класса: {}", targetClass.getSimpleName());
            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Handler.class)) {
                    Handler annotation = method.getAnnotation(Handler.class);
                    HandlerType type = annotation.value();
                    boolean isAsync = method.isAnnotationPresent(TAsync.class);

                    // Проверяем наличие аннотации TimeBot
                    int limit = 0;
                    int limitWindows = 0;
                    int delay = 0;
                    if (method.isAnnotationPresent(TimeBot.class)) {
                        TimeBot timeBot = method.getAnnotation(TimeBot.class);
                        limit = timeBot.limit();
                        limitWindows = timeBot.limitWindows();
                        delay = timeBot.delay();
                    }

                    log.info("Обнаружен метод с @Handler: {} (тип: {}, асинхронный: {}, limit: {}, limitWindows: {}, delay: {})",
                            method.getName(), type, isAsync, limit, limitWindows, delay);

                    CommandExecutor executor = new CommandExecutor(handler, method, isAsync, asyncExecutor, limit, limitWindows, delay);
                    handlersByType.computeIfAbsent(type, k -> new HashMap<>());

                    if (type == HandlerType.REGISTER_COMMAND) {
                        String[] cmds = annotation.commands();
                        if (cmds.length == 0) {
                            log.error("Для REGISTER_COMMAND не было указано команд в методе: {}", method.getName());
                        } else {
                            for (String cmd : cmds) {
                                handlersByType.get(type).put(cmd, executor);
                                log.info("Зарегистрирована команда: /{} из метода {} в классе {}", cmd, method.getName(), targetClass.getSimpleName());
                            }
                        }
                    } else {
                        String key = targetClass.getName() + "#" + method.getName();
                        handlersByType.get(type).put(key, executor);
                        log.info("Зарегистрирован обработчик {} из класса {}", method.getName(), targetClass.getSimpleName());
                    }
                }
            }
        }
        log.info("Завершена регистрация обработчиков. Зарегистрировано типов: {}", handlersByType.keySet());
    }

    public void dispatch(MessageContext ctx) {
        if (ctx.getMessage().getReplyToMessage() != null
                && ctx.getMessage().getReplyToMessage().getFrom() != null
                && ctx.getMessage().getReplyToMessage().getFrom().getIsBot()) {
            invokeHandlers(HandlerType.RESPOND_TO_BOT_MESSAGE, ctx);
        }

        if (ctx.getMessage() != null && (ctx.getMessage().hasAudio() || ctx.getMessage().hasDocument()
                || ctx.getMessage().hasPhoto() || ctx.getMessage().hasText()
                || ctx.getMessage().hasAnimation() || ctx.getMessage().hasDice()
                || ctx.getMessage().hasVideo() || ctx.getMessage().hasVoice()
                || ctx.getMessage().hasLocation() || ctx.getMessage().hasSticker())) {
            invokeHandlers(HandlerType.ON_MESSAGE, ctx);
        }

        if (ctx.getMessageText() != null && !ctx.getMessageText().isEmpty()) {
            invokeHandlers(HandlerType.ON_TEXT, ctx);
        }

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
        private final boolean isAsync;
        private final Executor asyncExecutor;
        private final int limit;
        private final int limitWindows;
        private final RateLimitManager rateLimitManager;
        private final int delay;

        public CommandExecutor(Object bean, Method method, boolean isAsync, Executor asyncExecutor,
                               int limit, int limitWindows, int delay) {
            this.bean = bean;
            this.method = method;
            this.isAsync = isAsync;
            this.asyncExecutor = asyncExecutor;
            this.limit = limit;
            this.limitWindows = limitWindows;
            this.delay = delay;
            this.rateLimitManager = new RateLimitManager(limit, limitWindows, asyncExecutor);
        }

        public void invoke(MessageContext ctx) {
            Runnable task = () -> {
                try {
                    if (delay > 0) {
                        Thread.sleep(delay * 1000L);
                    }
                    method.invoke(bean, ctx);
                } catch (Exception e) {
                    log.error("Ошибка при {}вызове команды: {}", isAsync ? "асинхронном " : "", ctx.getCommand(), e);
                }
            };

            if (isAsync) {
                if (limit > 0) {
                    rateLimitManager.submit(ctx.getChatId(), () -> asyncExecutor.execute(task));
                } else {
                    asyncExecutor.execute(task);
                }
            } else {
                if (limit > 0) {
                    rateLimitManager.submit(ctx.getChatId(), task);
                } else {
                    task.run();
                }
            }
        }
    }
}
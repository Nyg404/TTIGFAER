package io.github.nyg404.ttigfaer.core.Manager;

import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
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
    private final Executor asyncExecutor; // Пул потоков для асинхронных вызовов

    public CommandManager(List<CommandHandler> handlers, @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.handlers = handlers;
        this.asyncExecutor = asyncExecutor;
    }

    @PostConstruct
    public void init() {
        log.info("Начало регистрации обработчиков для {} бинов", handlers.size());
        for (CommandHandler handler : handlers) {
            Class<?> targetClass = AopUtils.getTargetClass(handler); // Используем AopUtils для обхода прокси
            log.info("Сканирование класса: {}", targetClass.getSimpleName());
            for (Method method : targetClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Handler.class)) {
                    Handler annotation = method.getAnnotation(Handler.class);
                    HandlerType type = annotation.value();
                    boolean isAsync = method.isAnnotationPresent(TAsync.class); // Проверяем наличие @TAsync
                    int limit = annotation.limit();
                    int limitWindows = annotation.limitWindows();
                    log.info("Обнаружен метод с @Handler: {} (тип: {}, асинхронный: {})", method.getName(), type, isAsync);


                    CommandExecutor executor = new CommandExecutor(handler, method, isAsync, asyncExecutor, limit, limitWindows);
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
        // Убрали @Async, так как асинхронность теперь управляется через @TAsync
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

        // Внутри CommandExecutor:
        public CommandExecutor(Object bean, Method method, boolean isAsync, Executor asyncExecutor, int limit, int limitWindows) {
            this.bean = bean;
            this.method = method;
            this.isAsync = isAsync;
            this.asyncExecutor = asyncExecutor;
            this.limit = limit;
            this.limitWindows = limitWindows;
            this.rateLimitManager = new RateLimitManager(limit, limitWindows);
        }


        public void invoke(MessageContext ctx) {
            Runnable task = () -> {
                try {
                    method.invoke(bean, ctx);
                } catch (Exception e) {
                    log.error("Ошибка при {}вызове команды: {}", isAsync ? "асинхронном " : "", ctx.getCommand(), e);
                }
            };

            if (limit > 0) {
                long chatId = ctx.getChatId(); // Получаем id чата из контекста
                rateLimitManager.submit(chatId, () -> {
                    if (isAsync) {
                        asyncExecutor.execute(task);
                    } else {
                        task.run();
                    }
                });
            } else {
                if (isAsync) {
                    asyncExecutor.execute(task);
                } else {
                    task.run();
                }
            }
        }
    }
}
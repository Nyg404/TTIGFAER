package io.github.nyg404.ttigfaer.core.Manager;

import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
import io.github.nyg404.ttigfaer.api.Annotations.TimeBot;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Commands.CommandExecutor;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;
import io.github.nyg404.ttigfaer.core.Utils.ArgumentRegistry;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Менеджер команд — отвечает за регистрацию и вызов обработчиков команд и callback.
 * Позволяет регистрировать методы с аннотацией @Handler и вызывать их по типам событий.
 */
@Component
@Slf4j
public class CommandManager {
    /** Карта обработчиков по типу и ключу команды */
    @Getter
    private final Map<HandlerType, Map<String, CommandExecutor>> handlersByType = new HashMap<>();
    private final List<CommandHandler> handlers;
    private final Executor asyncExecutor;
    private final ArgumentRegistry argumentRegistry;

    /**
     * Конструктор CommandManager.
     *
     * @param handlers список всех зарегистрированных обработчиков команд
     * @param asyncExecutor исполнитель для асинхронных задач
     */
    public CommandManager(List<CommandHandler> handlers, @Qualifier("asyncExecutor") Executor asyncExecutor, ArgumentRegistry argumentRegistry) {
        this.handlers = handlers;
        this.asyncExecutor = asyncExecutor;
        this.argumentRegistry = argumentRegistry;
    }

    /**
     * Инициализация менеджера команд — сканирование методов обработчиков и их регистрация.
     * Вызывается автоматически после создания бина Spring.
     */
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
                    if (!method.canAccess(handler)) {
                        method.setAccessible(true);
                    }
                    CommandExecutor executor = new CommandExecutor(handler, method, isAsync, asyncExecutor, limit, limitWindows, delay, argumentRegistry);
                    handlersByType.computeIfAbsent(type, k -> new HashMap<>());
                    if (type == HandlerType.REGISTER_COMMAND) {
                        String[] cmds = annotation.commands();
                        if (cmds.length == 0) {
                            log.error("Для REGISTER_COMMAND не указано команд в методе: {}", method.getName());
                        } else {
                            for (String cmd : cmds) {
                                if (handlersByType.get(type).containsKey(cmd)) {
                                    log.warn("Дублированная команда: /{} из метода {} в классе {}. Перезаписываем обработчик.",
                                            cmd, method.getName(), targetClass.getSimpleName());
                                }
                                handlersByType.get(type).put(cmd, executor);
                                log.info("Зарегистрирована команда: /{} из метода {} в классе {}",
                                        cmd, method.getName(), targetClass.getSimpleName());
                            }
                        }
                    }
                    else if (type == HandlerType.ON_CALLBACK_QUERY) {
                                String callback = annotation.callBack();
                                if (callback.isEmpty()) {
                                    log.error("Для ON_CALLBACK_QUERY не указано callback в методе: {}", method.getName());
                                } else {
                                    handlersByType.get(type).put(callback, executor);
                                    log.info("Зарегистрирован callback: {} из метода {} в классе {}", callback, method.getName(), targetClass.getSimpleName());
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

    /**
     * Обработка входящего контекста сообщения и вызов соответствующих обработчиков.
     *
     * @param ctx контекст сообщения
     */
    public void dispatch(MessageContext ctx) {
        if (ctx.getMessage() != null && ctx.getMessage().getReplyToMessage() != null
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
        if (ctx.isCallback()) {
            String action = ctx.getAction();
            Map<String, CommandExecutor> callbackHandlers = handlersByType.get(HandlerType.ON_CALLBACK_QUERY);
            if (callbackHandlers != null) {
                CommandExecutor executor = callbackHandlers.get(action);
                if (executor != null) {
                    executor.invoke(ctx);
                } else {
                    log.warn("Нет обработчика для callback: {}", action);
                }
            }
        }
    }

    /**
     * Вызов всех обработчиков определённого типа с переданным контекстом.
     *
     * @param type тип обработчиков
     * @param ctx контекст сообщения
     */
    private void invokeHandlers(HandlerType type, MessageContext ctx) {
        Map<String, CommandExecutor> map = handlersByType.get(type);
        if (map == null) return;
        for (Map.Entry<String, CommandExecutor> entry : map.entrySet()) {
            try {
                entry.getValue().invoke(ctx);
            } catch (Exception e) {
                log.error("Ошибка в обработчике {} при обработке: {}", entry.getKey(), ctx, e);
            }
        }
    }
}

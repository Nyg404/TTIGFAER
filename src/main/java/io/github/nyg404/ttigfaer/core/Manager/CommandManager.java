package io.github.nyg404.ttigfaer.core.Manager;

import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
import io.github.nyg404.ttigfaer.api.Annotations.TimeBot;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Commands.CommandExecutor;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;
import io.github.nyg404.ttigfaer.core.Enum.MessageFilter;
import io.github.nyg404.ttigfaer.core.Utils.ArgumentRegistry;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;
/**
 * Менеджер команд и событий для обработки аннотированных методов в Telegram-боте.
 * Сканирует все классы-обработчики, ищет методы с аннотацией {@link Handler} и регистрирует их.
 * Поддерживает фильтры, асинхронное выполнение, лимиты вызовов и различные типы обработчиков.
 *
 * <p>Поддерживаемые типы обработчиков ({@link HandlerType}):
 * <ul>
 *     <li>{@link HandlerType#REGISTER_COMMAND} - Обработка команд (начинаются с /)</li>
 *     <li>{@link HandlerType#ON_CALLBACK_QUERY} - Обработка callback-запросов</li>
 *     <li>{@link HandlerType#ON_MESSAGE} - Обработка сообщений с медиа и другими типами контента</li>
 *     <li>{@link HandlerType#RESPOND_TO_BOT_MESSAGE} - Ответы на сообщения бота</li>
 * </ul>
 *
 * <p>Обеспечивает вызов методов в зависимости от контекста сообщения ({@link MessageContext}).
 * Для методов можно указать фильтры ({@link MessageFilter}), асинхронный режим ({@link TAsync}),
 * а также ограничения по времени ({@link TimeBot}).
 *
 * @author tt
 */

@Component
@Slf4j
public class CommandManager {

    /** Карта обработчиков по типу и ключу (команда, callback и др.) */
    @Getter
    private final Map<HandlerType, Map<String, CommandExecutor>> handlersByType = new HashMap<>();

    private final List<CommandHandler> handlers;
    private final Executor asyncExecutor;
    private final ArgumentRegistry argumentRegistry;

    /**
     * Конструктор для внедрения зависимостей.
     *
     * @param handlers       список обработчиков команд
     * @param asyncExecutor  Executor для асинхронного выполнения
     * @param argumentRegistry Реестр аргументов для маппинга параметров методов
     */
    public CommandManager(List<CommandHandler> handlers, @Qualifier("asyncExecutor") Executor asyncExecutor, ArgumentRegistry argumentRegistry) {
        this.handlers = handlers;
        this.asyncExecutor = asyncExecutor;
        this.argumentRegistry = argumentRegistry;
    }

    /**
     * Инициализация менеджера: сканирование обработчиков, регистрация методов.
     * Вызывается после создания бина Spring (через {@link PostConstruct}).
     */
    @PostConstruct
    public void init() {
        log.info("Инициализация CommandManager: {} обработчиков найдено", handlers.size());
        for (CommandHandler handler : handlers) {
            Class<?> targetClass = AopUtils.getTargetClass(handler);
            log.info("Сканирование: {}", targetClass.getSimpleName());
            for (Method method : targetClass.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(Handler.class)) continue;

                Handler annotation = method.getAnnotation(Handler.class);
                HandlerType type = annotation.value();
                boolean isAsync = method.isAnnotationPresent(TAsync.class);

                int limit = 0, limitWindows = 0, delay = 0;
                if (method.isAnnotationPresent(TimeBot.class)) {
                    TimeBot tb = method.getAnnotation(TimeBot.class);
                    limit = tb.limit();
                    limitWindows = tb.limitWindows();
                    delay = tb.delay();
                }

                EnumSet<MessageFilter> filters = EnumSet.noneOf(MessageFilter.class);
                if (type == HandlerType.ON_MESSAGE) {
                    for (String filterName : annotation.filters()) {
                        try {
                            filters.add(MessageFilter.valueOf(filterName));
                        } catch (IllegalArgumentException e) {
                            log.warn("Неизвестный фильтр: {}", filterName);
                        }
                    }
                }

                if (!method.canAccess(handler)) method.setAccessible(true);
                CommandExecutor executor = new CommandExecutor(handler, method, isAsync, asyncExecutor, limit, limitWindows, delay, argumentRegistry, filters);
                handlersByType.computeIfAbsent(type, k -> new HashMap<>());

                switch (type) {
                    case REGISTER_COMMAND -> registerCommand(annotation.commands(), method, targetClass, executor);
                    case ON_CALLBACK_QUERY -> registerCallback(annotation.callBack(), method, targetClass, executor);
                    default -> registerGeneric(type, targetClass, method, executor);
                }
            }
        }
        log.info("Регистрация завершена. Обработчики: {}", handlersByType.keySet());
    }

    /**
     * Регистрирует команды для {@link HandlerType#REGISTER_COMMAND}.
     *
     * @param cmds         массив команд (например, /start, /help)
     * @param method       метод-обработчик
     * @param targetClass  класс-обработчик
     * @param executor     исполнитель метода
     */
    private void registerCommand(String[] cmds, Method method, Class<?> targetClass, CommandExecutor executor) {
        if (cmds.length == 0) {
            log.error("REGISTER_COMMAND без команд в методе: {}", method.getName());
            return;
        }
        Map<String, CommandExecutor> map = handlersByType.get(HandlerType.REGISTER_COMMAND);
        for (String cmd : cmds) {
            if (map.containsKey(cmd)) {
                log.warn("Перезапись команды /{} ({} -> {})", cmd, map.get(cmd).getMethod().getName(), method.getName());
            }
            map.put(cmd, executor);
            log.info("Зарегистрирована команда /{} ({}.{})", cmd, targetClass.getSimpleName(), method.getName());
        }
    }

    /**
     * Регистрирует обработчик callback-запроса для {@link HandlerType#ON_CALLBACK_QUERY}.
     *
     * @param callback     ключ callback
     * @param method       метод-обработчик
     * @param targetClass  класс-обработчик
     * @param executor     исполнитель метода
     */
    private void registerCallback(String callback, Method method, Class<?> targetClass, CommandExecutor executor) {
        if (callback.isEmpty()) {
            log.error("ON_CALLBACK_QUERY без callback в методе: {}", method.getName());
            return;
        }
        Map<String, CommandExecutor> map = handlersByType.get(HandlerType.ON_CALLBACK_QUERY);
        map.put(callback, executor);
        log.info("Зарегистрирован callback: {} ({}.{})", callback, targetClass.getSimpleName(), method.getName());
    }

    /**
     * Регистрирует обработчики для остальных типов событий (ON_TEXT, ON_MESSAGE и др.).
     *
     * @param type         тип обработчика
     * @param targetClass  класс-обработчик
     * @param method       метод-обработчик
     * @param executor     исполнитель метода
     */
    private void registerGeneric(HandlerType type, Class<?> targetClass, Method method, CommandExecutor executor) {
        String key = targetClass.getName() + "#" + method.getName();
        handlersByType.get(type).put(key, executor);
        log.info("Зарегистрирован {}: {}.{}", type, targetClass.getSimpleName(), method.getName());
    }

    /**
     * Основной метод для вызова обработчиков в зависимости от контекста сообщения.
     *
     * @param ctx контекст входящего сообщения
     */
    public void dispatch(MessageContext ctx) {
        if (ctx.getMessage() != null) {
            if (ctx.getMessage().getReplyToMessage() != null && ctx.getMessage().getReplyToMessage().getFrom() != null && ctx.getMessage().getReplyToMessage().getFrom().getIsBot()) {
                invokeHandlers(HandlerType.RESPOND_TO_BOT_MESSAGE, ctx);
            }
            if (hasMedia(ctx)) {
                invokeHandlers(HandlerType.ON_MESSAGE, ctx);
            }
        }


        if (ctx.getCommand() != null && !ctx.getCommand().isEmpty()) {
            CommandExecutor exec = getExecutor(HandlerType.REGISTER_COMMAND, ctx.getCommand());
            if (exec != null) exec.invoke(ctx);
        }

        if (ctx.isCallback()) {
            CommandExecutor exec = getExecutor(HandlerType.ON_CALLBACK_QUERY, ctx.getAction());
            if (exec != null) {
                exec.invoke(ctx);
            } else {
                log.warn("Нет обработчика для callback: {}", ctx.getAction());
            }
        }
    }

    /**
     * Проверяет, содержит ли сообщение медиа или другие особые элементы.
     *
     * @param ctx контекст сообщения
     * @return true, если есть медиа или другие элементы
     */
    private boolean hasMedia(MessageContext ctx) {
        return ctx.getMessage().hasAudio() || ctx.getMessage().hasDocument() || ctx.getMessage().hasPhoto()
                || ctx.getMessage().hasText() || ctx.getMessage().hasAnimation() || ctx.getMessage().hasDice()
                || ctx.getMessage().hasVideo() || ctx.getMessage().hasVoice() || ctx.getMessage().hasLocation()
                || ctx.getMessage().hasSticker();
    }

    /**
     * Получает зарегистрированный {@link CommandExecutor} по типу и ключу.
     *
     * @param type тип обработчика
     * @param key  ключ (например, команда, callback)
     * @return исполнитель команды или null
     */
    private CommandExecutor getExecutor(HandlerType type, String key) {
        Map<String, CommandExecutor> map = handlersByType.get(type);
        return (map != null) ? map.get(key) : null;
    }

    /**
     * Вызывает все обработчики указанного типа, подходящие по фильтрам.
     *
     * @param type тип обработчика
     * @param ctx  контекст сообщения
     */
    private void invokeHandlers(HandlerType type, MessageContext ctx) {
        Map<String, CommandExecutor> map = handlersByType.get(type);
        if (map == null) return;

        for (Map.Entry<String, CommandExecutor> entry : map.entrySet()) {
            CommandExecutor executor = entry.getValue();
            if (executor.matchesFilters(ctx)) {
                try {
                    executor.invoke(ctx);
                } catch (Exception e) {
                    log.error("Ошибка в обработчике {}: {}", entry.getKey(), e.getMessage(), e);
                }
            }
        }
    }
}


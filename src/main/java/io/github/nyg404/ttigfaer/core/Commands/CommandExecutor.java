package io.github.nyg404.ttigfaer.core.Commands;

import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.core.Enum.MessageFilter;
import io.github.nyg404.ttigfaer.core.Manager.RateLimitManager;
import io.github.nyg404.ttigfaer.core.Utils.ArgumentRegistry;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.concurrent.Executor;

/**
 * Класс, отвечающий за выполнение команд.
 * Позволяет вызывать методы обработчиков команд синхронно или асинхронно,
 * с поддержкой ограничения частоты вызовов (rate limiting) и задержек.
 */
@Data
@Slf4j
public class CommandExecutor {
    private final Object bean;
    private final Method method;
    private final boolean isAsync;
    private final Executor asyncExecutor;
    private final int limit;
    private final int limitWindows;
    private final RateLimitManager rateLimitManager;
    private final int delay;
    private final ArgumentRegistry argumentRegistry;
    private final EnumSet<MessageFilter> filters;


    /**
     * Конструктор CommandExecutor.
     *
     * @param bean объект, у которого вызывается метод
     * @param method метод обработчика команды
     * @param isAsync флаг асинхронного выполнения
     * @param asyncExecutor исполнитель для асинхронных задач
     * @param limit лимит вызовов команды
     * @param limitWindows временное окно для лимита (в миллисекундах)
     * @param delay задержка перед выполнением команды (в секундах)
     */
    public CommandExecutor(Object bean, Method method, boolean isAsync, Executor asyncExecutor,
                           int limit, int limitWindows, int delay, ArgumentRegistry argumentRegistry, EnumSet<MessageFilter> filters) {
        this.bean = bean;
        this.method = method;
        this.isAsync = isAsync;
        this.asyncExecutor = asyncExecutor;
        this.limit = limit;
        this.limitWindows = limitWindows;
        this.delay = delay;
        this.filters = filters;
        this.rateLimitManager = new RateLimitManager(limit, limitWindows, asyncExecutor);
        this.argumentRegistry = argumentRegistry;
    }

    /**
     * Выполнить команду с заданным контекстом.
     *
     * @param ctx контекст сообщения и команды
     */
    public void invoke(MessageContext ctx) {
        Runnable task = () -> {
            try {
                if (delay > 0) {
                    Thread.sleep(delay * 1000L);
                }

                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] args = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    if (paramTypes[i].isAssignableFrom(MessageContext.class)) {
                        args[i] = ctx;
                    } else {
                        args[i] = argumentRegistry.getRaw(paramTypes[i]);
                        if (args[i] == null) {
                            throw new IllegalArgumentException("Не найден аргумент типа: " + paramTypes[i].getSimpleName());
                        }
                    }
                }

                method.invoke(bean, args);

            } catch (Exception e) {
                log.error("Ошибка при {}вызове: {}", isAsync ? "асинхронном " : "",
                        ctx.getAction() != null ? ctx.getAction() : ctx.getCommand(), e);
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

    public boolean matchesFilters(MessageContext ctx) {
        if (filters.isEmpty()) return true; // Если фильтров нет, то подходит всё

        for (MessageFilter filter : filters) {
            switch (filter) {
                case Text -> {
                    if (ctx.getMessage() != null && ctx.getMessage().hasText()) return true;
                }
                case Photo -> {
                    if (ctx.getMessage() != null && ctx.getMessage().hasPhoto()) return true;
                }
                case Video -> {
                    if (ctx.getMessage() != null && ctx.getMessage().hasVideo()) return true;
                }
                case Voice -> {
                    if (ctx.getMessage() != null && ctx.getMessage().hasVoice()) return true;
                }
                case Document -> {
                    if (ctx.getMessage() != null && ctx.getMessage().hasDocument()) return true;
                }
                case Animation -> {
                    if (ctx.getMessage() != null && ctx.getMessage().hasAnimation()) return true;
                }
                case Sticker -> {
                    if(ctx.getMessage() != null && ctx.getMessage().hasSticker()) return true;
                }
                case GroupMedia -> {
                    if (ctx.getMessage() != null && ctx.getMessage().isGroupMessage()) return true;
                }
            }
        }
        return false;
    }
}

package io.github.nyg404.ttigfaer.core.Manager;

import io.github.nyg404.ttigfaer.api.Annotations.CallbackHandler;
import io.github.nyg404.ttigfaer.api.Interface.CommandHandler;
import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class CallbackManager {
    public record CallbackMethod(Object instance, Method method) {}
    private final List<CommandHandler> commandHandlers;
    private final HashMap<String, CallbackMethod> callbackHandlers = new HashMap<>();

    public CallbackManager(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }

    @PostConstruct
    public void init() {
        for (CommandHandler handler : commandHandlers) {
            Class<?> clazz = AopUtils.getTargetClass(handler);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(CallbackHandler.class)) {
                    CallbackHandler annotation = method.getAnnotation(CallbackHandler.class);
                    String callbackKey = annotation.value();

                    // Делаем доступным приватные методы
                    if (!method.canAccess(handler)) {
                        method.setAccessible(true);
                    }

                    callbackHandlers.put(callbackKey, new CallbackMethod(handler, method));
                    log.info("Зарегистрирован callback handler: {}", callbackKey);
                }
            }
        }
    }

    public void dispatch(MessageContext ctx) {
        if (!ctx.isCallback()) {
            log.warn("Попытка обработать не callback как callback");
            return;
        }

        String action = ctx.getAction();
        CallbackMethod callbackMethod = callbackHandlers.get(action);

        if (callbackMethod == null) {
            log.warn("Нет обработчика для callback действия: {}", action);
            return;
        }

        try {
            Method method = callbackMethod.method();
            if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == MessageContext.class) {
                method.invoke(callbackMethod.instance(), ctx);
            } else {
                method.invoke(callbackMethod.instance()); // fallback, если нет параметров
            }
        } catch (Exception e) {
            log.error("Ошибка при вызове callback метода: {}", action, e);
        }
    }

    public void handleCallback(MessageContext ctx) {
        if (!ctx.isCallback()) {
            log.warn("handleCallback вызван не для callback-сообщения.");
            return;
        }

        String action = ctx.getAction(); // action из CallbackData
        CallbackMethod callbackMethod = callbackHandlers.get(action);

        if (callbackMethod == null) {
            log.warn("Нет обработчика для callback: {}", action);
            return;
        }

        try {
            Method method = callbackMethod.method();
            if (method.getParameterCount() == 1 &&
                    method.getParameterTypes()[0].equals(MessageContext.class)) {

                method.invoke(callbackMethod.instance(), ctx);

            } else {
                log.warn("Метод {} не принимает MessageContext", method.getName());
            }
        } catch (Exception e) {
            log.error("Ошибка при вызове callback метода: {}", action, e);
        }
    }

    public boolean hasHandler(String action) {
        return callbackHandlers.containsKey(action);
    }

}

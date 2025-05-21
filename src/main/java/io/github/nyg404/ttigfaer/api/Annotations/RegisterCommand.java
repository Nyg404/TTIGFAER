package io.github.nyg404.ttigfaer.api.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @deprecated устарел и больше не работает
 * Аннотация для регистрации команды в Telegram-боте.
 * <p>
 * Метод, помеченный этой аннотацией, будет вызван при вводе пользователем указанной команды.
 * </p>
 *
 * <p><b>Пример использования:</b></p>
 * <pre>{@code
 * @RegisterCommand("start")
 * public void onStart(MessageContext ctx) {
 *     String response = "Привет, привет!";
 *     ctx.sendMessage(ctx, response);
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RegisterCommand {
    String value();
}

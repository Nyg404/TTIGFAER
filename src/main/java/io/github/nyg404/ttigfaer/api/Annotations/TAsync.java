package io.github.nyg404.ttigfaer.api.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Позволяет выполнять метод асинхронно.
 * <p><b>Пример использования:</b></p>
 *
 * <pre>{@code
 * @TAsync
 * @Handler(value = HandlerType.RESPOND_TO_BOT_MESSAGE)
 * public void onReplyToBotMessage(MessageContext ctx) {
 *     ctx.sendMessage(ctx, "Спасибо за ответ на мое сообщение!");
 * }
 * }</pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TAsync {
}

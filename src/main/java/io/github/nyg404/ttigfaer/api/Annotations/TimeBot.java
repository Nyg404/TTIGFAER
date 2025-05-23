package io.github.nyg404.ttigfaer.api.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * <pre>
 * <b>limit, limitWindows, delay</b>
 * Помощники для команд.
 * limit - Устанавливает лимит метода в N Окно
 * limitWindows - Устанавливает время для окна
 * delay - Устанавливает лимит перед отправкой метода.
 * </pre>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TimeBot {

    /**
     * Ставить лимит на отправку методов в N - окон
     */
    int limit() default 0;

    /**
     * Ставит время на одно окно.
     */
    int limitWindows() default 0;

    /**
     * Устаналивает время бота перед отправкой метода.
     */
    int delay() default 0;
}

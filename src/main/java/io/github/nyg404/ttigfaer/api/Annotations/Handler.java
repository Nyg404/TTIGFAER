package io.github.nyg404.ttigfaer.api.Annotations;

import io.github.nyg404.ttigfaer.core.Enum.HandlerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для обработки событий с помощью {@link HandlerType} — Enum, определяющего тип обработчика.
 * <p>
 * Позволяет указать тип события и, при необходимости, набор команд, которые обрабатывает метод.
 * </p>
 *
 * <p><b>Примеры использования:</b></p>
 * <pre>{@code
 * // Обработка конкретной команды "/start"
 * @Handler(value = HandlerType.REGISTER_COMMAND, commands = {"start"})
 * public void onStart(MessageContext ctx) {
 *     String response = "Привет, привет!";
 *     ctx.sendMessage(ctx, response);
 * }
 *
 * // Обработка любых текстовых сообщений
 * @Handler(value = HandlerType.ON_TEXT)
 * public void onText(MessageContext ctx) {
 *     // Реакция на любое текстовое сообщение
 *     ctx.sendMessage(ctx, "Вы написали: " + ctx.getMessageText());
 * }
 *
 * // Обработка любых сообщений (текст, фото, документ, аудио и т.п.)
 * @Handler(value = HandlerType.ON_MESSAGE)
 * public void onAnyMessage(MessageContext ctx) {
 *     ctx.sendMessage(ctx, "Сообщение получено.");
 * }
 *
 * // Обработка ответов на сообщения, отправленные ботом
 * @Handler(value = HandlerType.RESPOND_TO_BOT_MESSAGE)
 * public void onReplyToBotMessage(MessageContext ctx) {
 *     ctx.sendMessage(ctx, "Спасибо за ответ на мое сообщение!");
 * }
 * }</pre>
 *
 * @see HandlerType
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Handler {
    /**
     * Тип обработчика, определяющий способ вызова метода.
     */
    HandlerType value();

    /**
     * Массив команд, которые обрабатываются методом (актуально для REGISTER_COMMAND).
     */
    String[] commands() default {};

    /**
     * Ставить лимит на отправку сообщений.
     */
    int limit() default 0;

    int limitWindows() default 1;
}

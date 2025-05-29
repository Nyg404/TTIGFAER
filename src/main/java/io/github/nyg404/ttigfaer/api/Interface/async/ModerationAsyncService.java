package io.github.nyg404.ttigfaer.api.Interface.async;

import io.github.nyg404.ttigfaer.message.Options.ChatPermissionsOptions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;

/**
 * Асинхронный интерфейс для управления модерацией участников группы.
 * Все методы возвращают CompletableFuture для асинхронного выполнения.
 */
public interface ModerationAsyncService {

    /**
     * Асинхронно забанить пользователя.
     *
     * @param serverId       ID сервера/чата
     * @param userId         ID пользователя
     * @param duration       длительность бана в секундах
     * @param revokeMessages нужно ли удалить сообщения пользователя
     * @param messageToChat  сообщение для чата
     * @param messageToUser  сообщение для пользователя
     * @return CompletableFuture, который завершается при выполнении операции
     * @throws TelegramApiException при ошибке Telegram API
     */
    CompletableFuture<Void> asyncBanUser(long serverId, long userId, int duration, Boolean revokeMessages, String messageToChat, String messageToUser) throws TelegramApiException;

    /**
     * Асинхронно замутить пользователя.
     *
     * @param serverId      ID сервера/чата
     * @param userId        ID пользователя
     * @param duration      длительность мута в секундах
     * @param messageToChat сообщение для чата
     * @param messageToUser сообщение для пользователя
     * @param options       дополнительные настройки прав чата
     * @return CompletableFuture, который завершается при выполнении операции
     * @throws TelegramApiException при ошибке Telegram API
     */
    CompletableFuture<Void> asyncMuteUser(long serverId, long userId, int duration, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException;

    /**
     * Асинхронно снять мут с пользователя.
     *
     * @param serverId      ID сервера/чата
     * @param userId        ID пользователя
     * @param messageToChat сообщение для чата
     * @param messageToUser сообщение для пользователя
     * @param options       дополнительные настройки прав чата
     * @return CompletableFuture, который завершается при выполнении операции
     * @throws TelegramApiException при ошибке Telegram API
     */
    CompletableFuture<Void> asyncUnmuteUser(long serverId, long userId, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException;

    /**
     * Получить статус участника асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с строковым статусом пользователя
     */
    CompletableFuture<String> asyncStatusMember(String serverId, long userId);

    /**
     * Проверить, является ли пользователь владельцем асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с булевым значением: true если владелец, иначе false
     */
    CompletableFuture<Boolean> asyncHasOwner(String serverId, long userId);

    /**
     * Проверить, является ли пользователь администратором асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с булевым значением: true если админ, иначе false
     */
    CompletableFuture<Boolean> asyncHasAdmin(String serverId, long userId);

    /**
     * Проверить, является ли пользователь участником асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с булевым значением: true если участник, иначе false
     */
    CompletableFuture<Boolean> asyncHasMember(String serverId, long userId);

    /**
     * Проверить, имеет ли пользователь ограниченный статус (restricted) асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с булевым значением: true если ограничен, иначе false
     */
    CompletableFuture<Boolean> asyncHasRestricted(String serverId, long userId);

    /**
     * Проверить, покинул ли пользователь чат асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с булевым значением: true если покинул, иначе false
     */
    CompletableFuture<Boolean> asyncHasLeft(String serverId, long userId);

    /**
     * Проверить, был ли пользователь кикнут асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return CompletableFuture с булевым значением: true если кикнут, иначе false
     */
    CompletableFuture<Boolean> asyncHasKicked(String serverId, long userId);
}

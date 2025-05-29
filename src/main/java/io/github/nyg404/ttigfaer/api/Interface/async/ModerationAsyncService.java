package io.github.nyg404.ttigfaer.api.Interface.async;

import io.github.nyg404.ttigfaer.message.Options.ChatPermissionsOptions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.Future;

/**
 * Асинхронный интерфейс для управления модерацией участников группы.
 * Все методы используют ExecutorService для асинхронного выполнения.
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
     * @throws TelegramApiException при ошибке Telegram API
     */
    void asyncBanUser(long serverId, long userId, int duration, Boolean revokeMessages, String messageToChat, String messageToUser) throws TelegramApiException;

    /**
     *
     * @param serverId ID сервера/чата
     * @param userId Id пользователя
     * @throws TelegramApiException при ошибки Telegram API
     */
    void asyncUnBanUser(long serverId, long userId) throws TelegramApiException;
    /**
     * Асинхронно замутить пользователя.
     *
     * @param serverId      ID сервера/чата
     * @param userId        ID пользователя
     * @param duration      длительность мута в минутах
     * @param messageToChat сообщение для чата
     * @param messageToUser сообщение пользователю
     * @param options       дополнительные настройки прав чата
     * @throws TelegramApiException при ошибке Telegram API
     */
    void asyncMuteUser(long serverId, long userId, int duration, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException;

    /**
     * Асинхронно снять мут с пользователя.
     *
     * @param serverId      ID сервера/чата
     * @param userId        ID пользователя
     * @param messageToChat сообщение для чата
     * @param messageToUser сообщение пользователю
     * @param options       дополнительные настройки прав чата
     * @throws TelegramApiException при ошибке Telegram API
     */
    void asyncUnmuteUser(long serverId, long userId, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException;

    /**
     * Получить статус участника асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с строковым статусом пользователя
     */
    Future<String> asyncStatusMember(String serverId, long userId);

    /**
     * Проверить, является ли пользователь владельцем асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с булевым значением: true если владелец, иначе false
     */
    Future<Boolean> asyncHasOwner(String serverId, long userId);

    /**
     * Проверить, является ли пользователь администратором асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с булевым значением: true если админ, иначе false
     */
    Future<Boolean> asyncHasAdmin(String serverId, long userId);

    /**
     * Проверить, является ли пользователь участником асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с булевым значением: true если участник, иначе false
     */
    Future<Boolean> asyncHasMember(String serverId, long userId);

    /**
     * Проверить, имеет ли пользователь ограниченный статус (restricted) асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с булевым значением: true если ограничен, иначе false
     */
    Future<Boolean> asyncHasRestricted(String serverId, long userId);

    /**
     * Проверить, покинул ли пользователь чат асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с булевым значением: true если покинул, иначе false
     */
    Future<Boolean> asyncHasLeft(String serverId, long userId);

    /**
     * Проверить, был ли пользователь кикнут асинхронно.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return Future с булевым значением: true если кикнут, иначе false
     */
    Future<Boolean> asyncHasKicked(String serverId, long userId);
}
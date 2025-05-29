package io.github.nyg404.ttigfaer.api.Interface;

import io.github.nyg404.ttigfaer.message.Options.ChatPermissionsOptions;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Синхронный интерфейс для управления модерацией участников группы.
 */
@SuppressWarnings("all")
public interface ModerationService {

    /**
     * Забанить пользователя.
     *
     * @param serverId       ID сервера/чата
     * @param userId         ID пользователя
     * @param duration       длительность бана в секундах
     * @param revokeMessages удалить ли сообщения пользователя
     * @param messageToChat  сообщение для чата
     * @param messageToUser  сообщение для пользователя
     * @throws TelegramApiException при ошибке Telegram API
     */
    void banUser(long serverId, long userId, int duration, Boolean revokeMessages, String messageToChat, String messageToUser) throws TelegramApiException;

    /**
     *
     * @param serverId ID сервера/чата
     * @param userId Id пользователя
     * @throws TelegramApiException при ошибки Telegram API
     */
    void unBanUser(long serverId, long userId) throws TelegramApiException;

    /**
     * Замутить пользователя.
     *
     * @param serverId      ID сервера/чата
     * @param userId        ID пользователя
     * @param duration      длительность мута в секундах
     * @param messageToChat сообщение для чата
     * @param messageToUser сообщение для пользователя
     * @param options       настройки прав чата
     * @throws TelegramApiException при ошибке Telegram API
     */
    void muteUser(long serverId, long userId, int duration, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException;

    /**
     * Снять мут с пользователя.
     *
     * @param serverId      ID сервера/чата
     * @param userId        ID пользователя
     * @param messageToChat сообщение для чата
     * @param messageToUser сообщение для пользователя
     * @param options       настройки прав чата
     * @throws TelegramApiException при ошибке Telegram API
     */
    void unmuteUser(long serverId, long userId, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException;

    /**
     * Получить статус участника.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return статус пользователя (например, "creator", "admin", "member" и т.п.)
     */
    String statusMember(String serverId, long userId);

    /**
     * Проверить, является ли пользователь владельцем.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return true, если владелец, иначе false
     */
    boolean hasOwner(String serverId, long userId);

    /**
     * Проверить, является ли пользователь администратором.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return true, если админ, иначе false
     */
    boolean hasAdmin(String serverId, long userId);

    /**
     * Проверить, является ли пользователь участником.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return true, если участник, иначе false
     */
    boolean hasMember(String serverId, long userId);

    /**
     * Проверить, имеет ли пользователь ограниченный статус (restricted).
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return true, если ограничен, иначе false
     */
    boolean hasRestricted(String serverId, long userId);

    /**
     * Проверить, покинул ли пользователь чат.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return true, если покинул чат, иначе false
     */
    boolean hasLeft(String serverId, long userId);

    /**
     * Проверить, был ли пользователь кикнут.
     *
     * @param serverId ID сервера/чата
     * @param userId   ID пользователя
     * @return true, если был кикнут, иначе false
     */
    boolean hasKicked(String serverId, long userId);
}

package io.github.nyg404.ttigfaer.message.Manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.nyg404.ttigfaer.api.Interface.async.ModerationAsyncService;
import io.github.nyg404.ttigfaer.api.Interface.ModerationService;
import io.github.nyg404.ttigfaer.message.Options.ChatPermissionsOptions;
import io.github.nyg404.ttigfaer.message.Utils.ChatPermissionOptions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.*;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Сервисный класс для управления участниками группы в Telegram.
 * Реализует синхронные и асинхронные методы модерации (бан, мут, получение статуса и т.д.).
 *
 * @see ModerationService
 * @see ModerationAsyncService
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ModerationManager implements ModerationService, ModerationAsyncService {
    private final TelegramClient client;
    private final MessageManager msv;

    // Кэш для статусов пользователей: ключ — serverId:userId, значение — статус (creator, administrator, etc.)
    private final Cache<String, String> memberStatusCache = Caffeine.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES) // Статусы кэшируются на 5 минут
            .maximumSize(10_000) // Ограничение на количество записей
            .build();

    /**
     * Асинхронно забанить пользователя в чате на заданное время.
     * @param serverId ID чата (группы/сервера)
     * @param userId ID пользователя для бана
     * @param duration время бана в секундах
     * @param revokeMessages если true — удалять все сообщения пользователя из чата
     * @param messageToChat сообщение для отправки в чат после бана (если не пустое)
     * @param messageToUser сообщение для отправки пользователю после бана (если не пустое)
     * @return CompletableFuture, завершающийся по окончании операции
     * @throws TelegramApiException при ошибках API Telegram
     */
    @Override
    public CompletableFuture<Void> asyncBanUser(long serverId, long userId, int duration, Boolean revokeMessages, String messageToChat, String messageToUser) throws TelegramApiException {
        long untilDate = System.currentTimeMillis() / 1000L + duration;
        BanChatMember request = BanChatMember.builder()
                .chatId(serverId)
                .userId(userId)
                .untilDate((int) untilDate)
                .revokeMessages(revokeMessages)
                .build();

        return client.executeAsync(request)
                .thenAccept(response -> {
                    log.info("Пользователь забанен: userId={}", userId);
                    sendIfNotEmpty(messageToUser, userId);
                    sendIfNotEmpty(messageToChat, serverId);
                })
                .exceptionally(e -> {
                    log.error("Ошибка при бане: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    msv.sendMessage(serverId, "Ошибка при бане: " + e.getMessage());
                    return null;
                });
    }
    /**
     * Асинхронно замутить пользователя на заданное время с опциями прав.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @param duration время мута в минутах (от 1 до 1440)
     * @param messageToChat сообщение в чат (если не пустое)
     * @param messageToUser сообщение пользователю (если не пустое)
     * @param options настройки прав при муте
     * @return CompletableFuture с результатом операции
     * @throws TelegramApiException при ошибках API
     */
    @Override
    public CompletableFuture<Void> asyncMuteUser(long serverId, long userId, int duration, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException {
        // Валидация длительности
        if (duration <= 0 || duration > 1440) {
            log.error("Некорректная длительность мута: duration={}", duration);
            msv.sendMessage(serverId, "Ошибка: Длительность мута должна быть от 1 до 1440 минут.");
            return CompletableFuture.completedFuture(null);
        }

        long untilDate = System.currentTimeMillis() / 1000L + duration * 60L;
        ChatPermissions permissions = ChatPermissionOptions.buildChatPermissions(options);
        RestrictChatMember request = RestrictChatMember.builder()
                .chatId(serverId)
                .userId(userId)
                .permissions(permissions)
                .untilDate((int) untilDate)
                .build();

        return client.executeAsync(request)
                .thenAccept(response -> {
                    log.info("Пользователь замучен: userId={}", userId);
                    sendIfNotEmpty(messageToUser, userId);
                    sendIfNotEmpty(messageToChat, serverId);
                })
                .exceptionally(e -> {
                    log.error("Ошибка при муте: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    msv.sendMessage(serverId, "Ошибка при муте: " + getFriendlyErrorMessage(e.getMessage()));
                    return null;
                });
    }


    /**
     * Асинхронно размутить пользователя, восстанавливая права из опций.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @param messageToChat сообщение в чат (если не пустое)
     * @param messageToUser сообщение пользователю (если не пустое)
     * @param options настройки прав после размутывания
     * @return CompletableFuture с результатом операции
     * @throws TelegramApiException при ошибках API
     */
    @Override
    public CompletableFuture<Void> asyncUnmuteUser(long serverId, long userId, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException {
        ChatPermissions permissions = ChatPermissionOptions.buildChatPermissions(options);
        RestrictChatMember request = RestrictChatMember.builder()
                .chatId(serverId)
                .userId(userId)
                .permissions(permissions)
                .untilDate(0)
                .build();

        return client.executeAsync(request)
                .thenAccept(response -> {
                    log.info("Пользователь размучен: userId={}", userId);
                    sendIfNotEmpty(messageToUser, userId);
                    sendIfNotEmpty(messageToChat, serverId);
                })
                .exceptionally(e -> {
                    log.error("Ошибка при размуте: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    msv.sendMessage(serverId, "Ошибка при размуте: " + e.getMessage());
                    return null;
                });
    }
    /**
     * Асинхронно получить статус участника в чате.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture со статусом ("creator", "administrator", "member", "restricted", "left", "kicked" или "unknown")
     */
    @Override
    public CompletableFuture<String> asyncStatusMember(String serverId, long userId) {
        String cacheKey = serverId + ":" + userId;
        String cachedStatus = memberStatusCache.getIfPresent(cacheKey);
        if (cachedStatus != null) {
            log.debug("Статус пользователя из кэша: userId={}, serverId={}, status={}", userId, serverId, cachedStatus);
            return CompletableFuture.completedFuture(cachedStatus);
        }
        return getChatMember(serverId, userId)
                .thenApply(member -> {
                    String status = member != null ? member.getStatus() : "unknown";
                    memberStatusCache.put(cacheKey, status);
                    log.debug("Статус пользователя сохранён в кэш: userId={}, serverId={}, status={}", userId, serverId, status);
                    return status;
                })
                .exceptionally(e -> {
                    log.error("Ошибка при получении статуса: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return "error";
                });
    }

    /**
     * Асинхронно проверить, является ли пользователь владельцем чата.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с результатом проверки
     */
    @Override
    public CompletableFuture<Boolean> asyncHasOwner(String serverId, long userId) {
        return asyncStatusMember(serverId, userId)
                .thenApply("creator"::equals)
                .exceptionally(e -> {
                    log.error("Ошибка при проверке владельца: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return false;
                });
    }
    /**
     * Асинхронно проверить, является ли пользователь администратором чата.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с результатом проверки
     */
    @Override
    public CompletableFuture<Boolean> asyncHasAdmin(String serverId, long userId) {
        return asyncStatusMember(serverId, userId)
                .thenApply("administrator"::equals)
                .exceptionally(e -> {
                    log.error("Ошибка при проверке администратора: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return false;
                });
    }
    /**
     * Асинхронно проверить, является ли пользователь обычным участником чата.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с результатом проверки
     */
    @Override
    public CompletableFuture<Boolean> asyncHasMember(String serverId, long userId) {
        return asyncStatusMember(serverId, userId)
                .thenApply("member"::equals)
                .exceptionally(e -> {
                    log.error("Ошибка при проверке member: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return false;
                });
    }
    /**
     * Асинхронно проверить, находится ли пользователь в ограниченном статусе.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с результатом проверки
     */
    @Override
    public CompletableFuture<Boolean> asyncHasRestricted(String serverId, long userId) {
        return asyncStatusMember(serverId, userId)
                .thenApply("restricted"::equals)
                .exceptionally(e -> {
                    log.error("Ошибка при проверке restricted: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return false;
                });
    }
    /**
     * Асинхронно проверить, покинул ли пользователь чат.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с результатом проверки
     */
    @Override
    public CompletableFuture<Boolean> asyncHasLeft(String serverId, long userId) {
        return asyncStatusMember(serverId, userId)
                .thenApply("left"::equals)
                .exceptionally(e -> {
                    log.error("Ошибка при проверке left: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return false;
                });
    }
    /**
     * Асинхронно проверить, был ли пользователь кикнут из чата.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с результатом проверки
     */
    @Override
    public CompletableFuture<Boolean> asyncHasKicked(String serverId, long userId) {
        return asyncStatusMember(serverId, userId)
                .thenApply("kicked"::equals)
                .exceptionally(e -> {
                    log.error("Ошибка при проверке kicked: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
                    return false;
                });
    }
    /**
     * Забанить пользователя синхронно (обёртка над asyncBanUser).
     * @param serverId ID чата
     * @param userId ID пользователя
     * @param duration длительность бана в секундах
     * @param revokeMessages удалять ли сообщения
     * @param messageToChat сообщение в чат
     * @param messageToUser сообщение пользователю
     * @throws TelegramApiException при ошибке API
     */
    @Override
    public void banUser(long serverId, long userId, int duration, Boolean revokeMessages, String messageToChat, String messageToUser) throws TelegramApiException {
        asyncBanUser(serverId, userId, duration, revokeMessages, messageToChat, messageToUser).join();
    }
    /**
     * Замутить пользователя синхронно (обёртка над asyncMuteUser).
     * @param serverId ID чата
     * @param userId ID пользователя
     * @param duration длительность мута в минутах
     * @param messageToChat сообщение в чат
     * @param messageToUser сообщение пользователю
     * @param options настройки прав
     * @throws TelegramApiException при ошибках API
     */
    @Override
    public void muteUser(long serverId, long userId, int duration, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException {
        asyncMuteUser(serverId, userId, duration, messageToChat, messageToUser, options).join();
    }
    /**
     * Размутить пользователя синхронно (обёртка над asyncUnmuteUser).
     * @param serverId ID чата
     * @param userId ID пользователя
     * @param messageToChat сообщение в чат
     * @param messageToUser сообщение пользователю
     * @param options настройки прав
     * @throws TelegramApiException при ошибках API
     */
    @Override
    public void unmuteUser(long serverId, long userId, String messageToChat, String messageToUser, ChatPermissionsOptions options) throws TelegramApiException {
        asyncUnmuteUser(serverId, userId, messageToChat, messageToUser, options).join();
    }

    /**
     * Получить статус участника синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return строка статуса
     */
    @Override
    public String statusMember(String serverId, long userId) {
        return asyncStatusMember(serverId, userId).join();
    }
    /**
     * Проверить, является ли пользователь владельцем синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return true, если владелец
     */
    @Override
    public boolean hasOwner(String serverId, long userId) {
        return asyncHasOwner(serverId, userId).join();
    }
    /**
     * Проверить, является ли пользователь администратором синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return true, если администратор
     */
    @Override
    public boolean hasAdmin(String serverId, long userId) {
        return asyncHasAdmin(serverId, userId).join();
    }
    /**
     * Проверить, является ли пользователь обычным участником синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return true, если обычный участник
     */
    @Override
    public boolean hasMember(String serverId, long userId) {
        return asyncHasMember(serverId, userId).join();
    }
    /**
     * Проверить, находится ли пользователь в статусе restricted синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return true, если restricted
     */
    @Override
    public boolean hasRestricted(String serverId, long userId) {
        return asyncHasRestricted(serverId, userId).join();
    }
    /**
     * Проверить, покинул ли пользователь чат синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return true, если left
     */
    @Override
    public boolean hasLeft(String serverId, long userId) {
        return asyncHasLeft(serverId, userId).join();
    }
    /**
     * Проверить, был ли пользователь кикнут синхронно.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return true, если kicked
     */
    @Override
    public boolean hasKicked(String serverId, long userId) {
        return asyncHasKicked(serverId, userId).join();
    }
    /**
     * Отправить сообщение в чат, если текст не пустой.
     * @param message текст сообщения
     * @param chatId ID чата или пользователя
     */
    private void sendIfNotEmpty(String message, long chatId) {
        if (message != null && !message.isEmpty()) {
            msv.sendMessage(chatId, message);
        }
    }
    /**
     * Получить информацию об участнике чата.
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture с объектом ChatMember
     */
    public CompletableFuture<ChatMember> getChatMember(String serverId, long userId) {
        GetChatMember request = new GetChatMember(serverId, userId);
        try {
            return client.executeAsync(request);
        } catch (TelegramApiException e) {
            log.error("Ошибка при получении участника: userId={}, serverId={}, error={}", userId, serverId, e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    /**
     * Асинхронно получить ID бота.
     * @return CompletableFuture с ID бота
     */
    public CompletableFuture<Long> asyncGetBotId() {
        try {
            return client.executeAsync(new org.telegram.telegrambots.meta.api.methods.GetMe())
                    .thenApply(User::getId);
        } catch (TelegramApiException e) {
            log.error("Ошибка при получении ID бота: {}", e.getMessage(), e);
            return CompletableFuture.failedFuture(e);
        }
    }
    /**
     * Синхронно получить ID бота.
     * @return ID бота
     */
    @SuppressWarnings("all")
    public Long getBotId() {
        return asyncGetBotId().join();
    }
    /**
     * Получить статус из кэша или обновить его из Telegram API.
     * @param cacheKey ключ для кэша (serverId:userId)
     * @param serverId ID чата
     * @param userId ID пользователя
     * @return CompletableFuture со статусом
     */
    @SuppressWarnings("all")
    private CompletableFuture<String> getCachedOrFetchStatus(@NonNull String cacheKey, @NonNull String serverId, long userId) {
        String cachedStatus = memberStatusCache.getIfPresent(cacheKey);
        if (cachedStatus != null) {
            log.debug("Статус из кэша: key={}, status={}", cacheKey, cachedStatus);
            return CompletableFuture.completedFuture(cachedStatus);
        }
        return asyncStatusMember(serverId, userId)
                .thenApply(status -> {
                    memberStatusCache.put(cacheKey, status);
                    log.debug("Статус сохранён в кэш: key={}, status={}", cacheKey, status);
                    return status;
                });
    }

    /**
     * Преобразует техническое сообщение об ошибке в более понятное.
     * @param error текст ошибки
     * @return человекочитаемое сообщение
     */
    private String getFriendlyErrorMessage(String error) {
        return switch (error.toLowerCase()) {
            case "chat_admin_required" -> "Бот не имеет прав администратора.";
            case "user_is_an_administrator_of_the_chat" -> "Нельзя замутить администратора чата.";
            case "can't remove chat owner" -> "Нельзя замутить владельца чата.";
            default -> "Неизвестная ошибка: " + error;
        };
    }
}
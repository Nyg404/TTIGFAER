package io.github.nyg404.ttigfaer.message.Options;


import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для отправки текстового сообщения.
 * Позволяют управлять уведомлениями, предпросмотром ссылок, форматированием, ответами и клавиатурой.
 */
@Data
@Builder
public class MessageOptions {
    private String parseMode; // Markdown, HTML, MarkdownV2
    private Boolean disableWebPagePreview;
    private Boolean disableNotification;
    private Integer replyToMessageId;
    private ReplyKeyboard replyMarkup;
    private List<MessageEntity> entities;
    private Boolean allowSendingWithoutReply;
    private Boolean protectContent;
    private LinkPreviewOptions linkPreviewOptions;
    private ReplyParameters replyParameters;
    private String businessConnectionId;
    private String messageEffectId;
    private Boolean allowPaidBroadcast;
}



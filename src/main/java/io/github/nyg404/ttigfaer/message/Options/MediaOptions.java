package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;

import org.telegram.telegrambots.meta.api.objects.ReplyParameters;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Data
@Builder
public class MediaOptions {
    private Integer messageThreadId;
    private Integer replyToMessageId;
    private Boolean disableNotification;
    private Boolean allowSendingWithoutReply;
    private Boolean protectContent;
    private ReplyParameters replyParameters;
    private String businessConnectionId;
    private ReplyKeyboard replyMarkup;
    private String messageEffectId;
    private Boolean allowPaidBroadcast;
}
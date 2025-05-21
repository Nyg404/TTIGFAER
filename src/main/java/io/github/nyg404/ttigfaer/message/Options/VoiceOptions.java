package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

@Data
@Builder
public class VoiceOptions {
    private String caption;
    @Singular
    private List<MessageEntity> captionEntities;

    private Integer duration;  // продолжительность в секундах

    private Boolean disableNotification;
    private Integer replyToMessageId;
    private InlineKeyboardMarkup replyMarkup;

    private Boolean allowSendingWithoutReply;
    private Boolean protectContent;
}

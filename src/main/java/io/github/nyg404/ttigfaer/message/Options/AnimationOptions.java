package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Data
@Builder
public class AnimationOptions {

    private Integer duration;
    private Integer width;
    private Integer height;

    private String caption;
    private String parseMode;
    private List<MessageEntity> captionEntities;

    private InputFile thumbnail;

    private Boolean disableNotification;
    private Boolean protectContent;
    private Boolean hasSpoiler;
    private Boolean showCaptionAboveMedia;
    private Boolean allowSendingWithoutReply;
    private Boolean allowPaidBroadcast;

    private Integer replyToMessageId;

    private ReplyKeyboard replyMarkup;

    private String businessConnectionId;
    private String messageEffectId;
}

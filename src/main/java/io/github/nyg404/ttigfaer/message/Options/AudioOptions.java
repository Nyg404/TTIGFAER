package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Data
@Builder
public class AudioOptions {
    private Integer messageThreadId;
    private Integer replyToMessageId;
    private Boolean disableNotification;
    private ReplyKeyboard replyMarkup;
    private String performer;
    private String title;
    private String caption;
    private String parseMode;
    private Integer duration;
    private InputFile thumbnail;
    private List<MessageEntity> captionEntities;
    private Boolean allowSendingWithoutReply;
    private Boolean protectContent;
    private ReplyParameters replyParameters;
    private String businessConnectionId;
    private String messageEffectId;
    private Boolean allowPaidBroadcast;
}

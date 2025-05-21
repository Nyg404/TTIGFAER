package io.github.nyg404.ttigfaer.message.Options;


import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

@Data
@Builder
public class VideoOptions {
    private String caption;
    private List<MessageEntity> captionEntities;
    private String parseMode;
    private Integer duration;
    private Integer width;
    private Integer height;
    private Boolean supportsStreaming;
    private InputFile thumbnail;
    private Boolean disableNotification;
    private Integer replyToMessageId;
    private ReplyKeyboard replyMarkup;
    private Boolean allowSendingWithoutReply;
    private Boolean protectContent;
}
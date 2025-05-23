package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.ReplyParameters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;
@Data
@Builder
public class EditTextOptions {
    private String parseMode; // Markdown, HTML, MarkdownV2
    private Boolean disableWebPagePreview;
    private ReplyKeyboard replyMarkup;
    private List<MessageEntity> entities;
    private LinkPreviewOptions linkPreviewOptions;
    private String businessConnectionId;
}

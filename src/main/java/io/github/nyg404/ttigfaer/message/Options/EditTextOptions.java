package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.LinkPreviewOptions;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.List;

/**
 * Опции для редактирования текстового сообщения.
 * Позволяет настраивать форматирование текста, предпросмотр ссылок, клавиатуру и другие параметры.
 */
@Data
@Builder
public class EditTextOptions {

    /** Режим парсинга текста (Markdown, HTML, MarkdownV2). */
    private String parseMode;

    /** Отключить предпросмотр ссылок. */
    private Boolean disableWebPagePreview;

    /** Клавиатура или кнопки для сообщения. */
    private ReplyKeyboard replyMarkup;

    /** Список сущностей для форматирования текста. */
    private List<MessageEntity> entities;

    /** Настройки предпросмотра ссылок. */
    private LinkPreviewOptions linkPreviewOptions;

    /** ID бизнес-соединения для рассылок. */
    private String businessConnectionId;
}

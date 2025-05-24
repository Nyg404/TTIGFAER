package io.github.nyg404.ttigfaer.message.Utils;

import io.github.nyg404.ttigfaer.message.MessageService;
import io.github.nyg404.ttigfaer.message.Options.*;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Вспомогательные методы для применения опций сообщений Telegram API.
 * Предназначен для упрощения настройки билдеров сообщений с помощью опциональных параметров.
 */
public class MessageOptionUtils {

    /**
     * Применяет опции к билдеру SendMessage.
     *
     * @param builder билдер SendMessage
     * @param options объект опций сообщения
     */
    public static void applyMessageOptions(SendMessage.SendMessageBuilder<SendMessage, ?> builder, MessageOptions options) {
        if (options == null) return;
        builder
                .parseMode(options.getParseMode())
                .disableWebPagePreview(options.getDisableWebPagePreview())
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .entities(options.getEntities())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent())
                .linkPreviewOptions(options.getLinkPreviewOptions())
                .replyParameters(options.getReplyParameters())
                .businessConnectionId(options.getBusinessConnectionId())
                .messageEffectId(options.getMessageEffectId())
                .allowPaidBroadcast(options.getAllowPaidBroadcast());
    }

    /**
     * Применяет опции к аудиосообщению.
     *
     * @param builder билдер SendAudio
     * @param options объект опций аудио
     * @param audio   файл аудио
     */
    public static void applyAudioOptions(SendAudio.SendAudioBuilder<SendAudio, ?> builder, AudioOptions options, InputFile audio) {
        if (options == null) return;
        builder
                .audio(audio)
                .messageThreadId(options.getMessageThreadId())
                .replyToMessageId(options.getReplyToMessageId())
                .disableNotification(options.getDisableNotification())
                .replyMarkup(options.getReplyMarkup())
                .performer(options.getPerformer())
                .title(options.getTitle())
                .caption(options.getCaption())
                .parseMode(options.getParseMode())
                .duration(options.getDuration())
                .thumbnail(options.getThumbnail())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent())
                .replyParameters(options.getReplyParameters())
                .businessConnectionId(options.getBusinessConnectionId())
                .messageEffectId(options.getMessageEffectId())
                .allowPaidBroadcast(options.getAllowPaidBroadcast());

        if (options.getCaptionEntities() != null && !options.getCaptionEntities().isEmpty()) {
            builder.captionEntities(options.getCaptionEntities());
        }
    }

    /**
     * Применяет опции к фото.
     *
     * @param builder билдер SendPhoto
     * @param options объект опций фото
     * @param photo   файл фото
     */
    public static void applyPhotoOptions(SendPhoto.SendPhotoBuilder<SendPhoto, ?> builder, PhotoOptions options, InputFile photo) {
        if (options == null) return;
        builder
                .photo(photo)
                .caption(options.getCaption())
                .parseMode(options.getParseMode())
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .showCaptionAboveMedia(options.getShowCaptionAboveMedia())
                .protectContent(options.getProtectContent())
                .hasSpoiler(options.getHasSpoiler());

        if (options.getCaptionEntities() != null && !options.getCaptionEntities().isEmpty()) {
            builder.captionEntities(options.getCaptionEntities());
        }
    }

    /**
     * Применяет опции к анимации (GIF).
     *
     * @param builder билдер SendAnimation
     * @param options объект опций анимации
     * @param animation файл анимации
     */
    public static void applyAnimationOptions(SendAnimation.SendAnimationBuilder<SendAnimation, ?> builder, AnimationOptions options, InputFile animation) {
        if (options == null) return;
        builder
                .animation(animation)
                .caption(options.getCaption())
                .parseMode(options.getParseMode())
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .showCaptionAboveMedia(options.getShowCaptionAboveMedia())
                .protectContent(options.getProtectContent())
                .hasSpoiler(options.getHasSpoiler())
                .duration(options.getDuration())
                .width(options.getWidth())
                .height(options.getHeight())
                .thumbnail(options.getThumbnail())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .businessConnectionId(options.getBusinessConnectionId())
                .messageEffectId(options.getMessageEffectId())
                .allowPaidBroadcast(options.getAllowPaidBroadcast());

        if (options.getCaptionEntities() != null && !options.getCaptionEntities().isEmpty()) {
            builder.captionEntities(options.getCaptionEntities());
        }
    }

    /**
     * Применяет опции к видео.
     *
     * @param builder билдер SendVideo
     * @param options объект опций видео
     * @param video   файл видео
     */
    public static void applyVideoOptions(SendVideo.SendVideoBuilder<SendVideo, ?> builder, VideoOptions options, InputFile video) {
        if (options == null) return;
        builder
                .video(video)
                .caption(options.getCaption())
                .parseMode(options.getParseMode())
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .duration(options.getDuration())
                .width(options.getWidth())
                .height(options.getHeight())
                .supportsStreaming(options.getSupportsStreaming())
                .thumbnail(options.getThumbnail())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent());

        if (options.getCaptionEntities() != null && !options.getCaptionEntities().isEmpty()) {
            builder.captionEntities(options.getCaptionEntities());
        }
    }

    /**
     * Применяет опции к документу.
     *
     * @param builder билдер SendDocument
     * @param options объект опций документа
     * @param document файл документа
     */
    public static void applyDocumentOptions(SendDocument.SendDocumentBuilder<SendDocument, ?> builder, DocumentOptions options, InputFile document) {
        if (options == null) return;
        builder
                .document(document)
                .caption(options.getCaption())
                .parseMode(options.getParseMode())
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .thumbnail(options.getThumbnail())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent());

        if (options.getCaptionEntities() != null && !options.getCaptionEntities().isEmpty()) {
            builder.captionEntities(options.getCaptionEntities());
        }
    }

    /**
     * Применяет опции к голосовому сообщению.
     *
     * @param builder билдер SendVoice
     * @param options объект опций голосового сообщения
     * @param file файл голосового сообщения
     */
    public static void applyVoiceOptions(SendVoice.SendVoiceBuilder<SendVoice, ?> builder, VoiceOptions options, InputFile file) {
        if (options == null) return;

        builder
                .voice(file)
                .caption(options.getCaption())
                .captionEntities(options.getCaptionEntities())
                .duration(options.getDuration())
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent());

        if (options.getCaptionEntities() != null && !options.getCaptionEntities().isEmpty()) {
            builder.captionEntities(options.getCaptionEntities());
        }
    }

    /**
     * Применяет опции к стикеру.
     *
     * @param builder билдер SendSticker
     * @param options объект опций стикера
     * @param sticker файл стикера
     */
    public static void applyStickerOptions(SendSticker.SendStickerBuilder<SendSticker, ?> builder, StickerOptions options, InputFile sticker) {
        if (options == null) return;
        builder
                .sticker(sticker)
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent())
                .emoji(options.getEmoji())
                .businessConnectionId(options.getBusinessConnectionId())
                .messageEffectId(options.getMessageEffectId())
                .allowPaidBroadcast(options.getAllowPaidBroadcast());

        // если нужны дополнительные поля, например replyParameters
        if (options.getReplyParameters() != null) {
            builder.replyParameters(options.getReplyParameters());
        }
    }

    public static void applyMediaOptions(SendMediaGroup.SendMediaGroupBuilder builder, MediaOptions options) {
        if (options == null) return;

        builder
                .disableNotification(options.getDisableNotification())
                .replyToMessageId(options.getReplyToMessageId())
                .replyMarkup(options.getReplyMarkup())
                .businessConnectionId(options.getBusinessConnectionId())
                .messageEffectId(options.getMessageEffectId())
                .allowPaidBroadcast(options.getAllowPaidBroadcast())
                .allowSendingWithoutReply(options.getAllowSendingWithoutReply())
                .protectContent(options.getProtectContent());

        if (options.getMessageThreadId() != null) {
            builder.messageThreadId(options.getMessageThreadId());
        }
        if (options.getReplyParameters() != null) {
            builder.replyParameters(options.getReplyParameters());
        }
    }

    /**
     * Применяет опции к редактируемому текстовому сообщению.
     *
     * @param builder билдер EditMessageText
     * @param options объект опций редактирования текста
     */
    public static void applyEditTextOptions(EditMessageText.EditMessageTextBuilder<EditMessageText, ?> builder, EditTextOptions options) {
        if (options == null) return;
        builder
                .parseMode(options.getParseMode())
                .disableWebPagePreview(options.getDisableWebPagePreview())
                .replyMarkup((InlineKeyboardMarkup) options.getReplyMarkup())
                .entities(options.getEntities())
                .linkPreviewOptions(options.getLinkPreviewOptions())
                .businessConnectionId(options.getBusinessConnectionId());
    }

    /**
     * Применяет опции к редактируемому медиа-сообщению.
     *
     * @param builder билдер EditMessageMedia
     * @param options объект опций редактирования медиа
     */
    public static void applyEditMediaOptions(EditMessageMedia.EditMessageMediaBuilder<EditMessageMedia, ?> builder, EditMediaOptions options){
        if (options == null) return;
        builder
                .replyMarkup((InlineKeyboardMarkup) options.getReplyMarkup())
                .businessConnectionId(options.getBusinessConnectionId());
    }
}

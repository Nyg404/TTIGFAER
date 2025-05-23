package io.github.nyg404.ttigfaer.message.Utils;

import io.github.nyg404.ttigfaer.message.MessageService;
import io.github.nyg404.ttigfaer.message.Options.*;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class MessageOptionUtils {

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

    public static void applyEditMediaOptions(EditMessageMedia.EditMessageMediaBuilder<EditMessageMedia, ?> builder, EditMediaOptions options){
        if (options == null) return;
        builder
                .replyMarkup((InlineKeyboardMarkup) options.getReplyMarkup())
                .businessConnectionId(options.getBusinessConnectionId());
    }

    private void teest(){

    }
}

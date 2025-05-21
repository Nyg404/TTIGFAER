package io.github.nyg404.ttigfaer.message.Utils;

import io.github.nyg404.ttigfaer.message.Options.AnimationOptions;
import io.github.nyg404.ttigfaer.message.Options.AudioOptions;
import io.github.nyg404.ttigfaer.message.Options.MessageOptions;
import io.github.nyg404.ttigfaer.message.Options.PhotoOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

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
}

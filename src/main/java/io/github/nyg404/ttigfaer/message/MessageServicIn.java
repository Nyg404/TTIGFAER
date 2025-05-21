package io.github.nyg404.ttigfaer.message;




import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface MessageServicIn {
    void sendMessage(MessageContext context, String text);
    void sendMessage(MessageContext context, String text, MessageOptions options);

    void sendMessage(long chatId, String text);
    void sendMessage(long chatId, String text, MessageOptions options);

    void sendReplayMessage(MessageContext context, String text);

    Message sendForwardMessage(MessageContext context, long targetChaId, Message message);

    void sendAudio(MessageContext ctx, InputFile file);
    void sendAudio(MessageContext ctx, InputFile file, AudioOptions options);

    void sendAudio(long chatId, InputFile file);
    void sendAudio(long chatId, InputFile file, AudioOptions options);

    void sendPhoto(MessageContext ctx, InputFile file);
    void sendPhoto(MessageContext ctx, InputFile file, PhotoOptions options);

    void sendPhoto(long chatId, InputFile file);
    void sendPhoto(long chatId, InputFile file, PhotoOptions options);

    void sendAnimation(MessageContext ctx, InputFile file);
    void sendAnimation(MessageContext ctx, InputFile file, AnimationOptions options);

    void sendAnimation(long chatId, InputFile file);
    void sendAnimation(long chatId, InputFile file, AnimationOptions options);

    // Добавленные методы для видео
    void sendVideo(MessageContext ctx, InputFile file);
    void sendVideo(MessageContext ctx, InputFile file, VideoOptions options);

    void sendVideo(long chatId, InputFile file);
    void sendVideo(long chatId, InputFile file, VideoOptions options);

    // Добавленные методы для документов
    void sendDocument(MessageContext ctx, InputFile file);
    void sendDocument(MessageContext ctx, InputFile file, DocumentOptions options);

    void sendDocument(long chatId, InputFile file);
    void sendDocument(long chatId, InputFile file, DocumentOptions options);

    void sendVoice(MessageContext ctx, InputFile file);
    void sendVoice(MessageContext ctx, InputFile file, VoiceOptions options);

    void sendVoice(long chatId, InputFile file);
    void sendVoice(long chatId, InputFile file, VoiceOptions options);


    void sendLocation(MessageContext ctx, double latitude, double longitude);
    void sendLocation(long chatId, double latitude, double longitude);


}


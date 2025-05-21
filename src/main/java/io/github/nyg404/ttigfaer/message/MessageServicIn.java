package io.github.nyg404.ttigfaer.message;




import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import io.github.nyg404.ttigfaer.message.Options.AudioOptions;
import io.github.nyg404.ttigfaer.message.Options.MessageOptions;
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

    void sendAnimation(MessageContext ctx, InputFile file);
}


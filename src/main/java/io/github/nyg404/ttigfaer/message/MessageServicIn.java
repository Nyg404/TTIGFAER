package io.github.nyg404.ttigfaer.message;




import io.github.nyg404.ttigfaer.api.Message.MessageContext;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface MessageServicIn{
    void sendMessage(MessageContext context, String text);
    void sendMessage(long chatId, String text);
    void sendReplayMessage(MessageContext context, String text);
    Message sendForwardMessage(MessageContext context, long targetChaId, Message message);
}

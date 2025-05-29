package io.github.nyg404.ttigfaer.message.Utils;

import io.github.nyg404.ttigfaer.message.Options.ChatPermissionsOptions;
import org.telegram.telegrambots.meta.api.objects.ChatPermissions;

public class ChatPermissionOptions {
    public static ChatPermissions buildChatPermissions(ChatPermissionsOptions options) {
        ChatPermissions.ChatPermissionsBuilder<?, ?> builder = ChatPermissions.builder();

        if (options != null) {
            builder
                    .canSendMessages(options.getCanSendMessages() != null ? options.getCanSendMessages() : false)
                    .canSendPolls(options.getCanSendPolls() != null ? options.getCanSendPolls() : false)
                    .canSendOtherMessages(options.getCanSendOtherMessages() != null ? options.getCanSendOtherMessages() : false)
                    .canAddWebPagePreviews(options.getCanAddWebPagePreviews() != null ? options.getCanAddWebPagePreviews() : false)
                    .canChangeInfo(options.getCanChangeInfo() != null ? options.getCanChangeInfo() : false)
                    .canInviteUsers(options.getCanInviteUsers() != null ? options.getCanInviteUsers() : false)
                    .canPinMessages(options.getCanPinMessages() != null ? options.getCanPinMessages() : false);
        }

        return builder.build();
    }

}

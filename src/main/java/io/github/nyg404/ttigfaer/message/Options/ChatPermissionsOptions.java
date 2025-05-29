package io.github.nyg404.ttigfaer.message.Options;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatPermissionsOptions {
    private Boolean canSendMessages;
    private Boolean canSendPolls;
    private Boolean canSendOtherMessages;
    private Boolean canAddWebPagePreviews;
    private Boolean canChangeInfo;
    private Boolean canInviteUsers;
    private Boolean canPinMessages;
}

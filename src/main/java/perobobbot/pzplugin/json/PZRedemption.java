
package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;
import perobobbot.lang.IdentityInfo;
import perobobbot.twitch.api.UserInfo;

import java.util.Optional;

@Value
public class PZRedemption implements PZNotification {

    @NonNull String id;
    @NonNull IdentityInfo broadcaster;
    @NonNull String rewardKind;
    @NonNull String userInput;
    @NonNull UserInfo user;

    @Override
    public @NonNull Optional<IdentityInfo> getOwner() {
        return Optional.of(broadcaster);
    }

}

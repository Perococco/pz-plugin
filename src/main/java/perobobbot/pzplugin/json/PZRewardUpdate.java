
package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;
import perobobbot.lang.IdentityInfo;

import java.util.Optional;

@Value
public class PZRewardUpdate implements PZNotification {

    @NonNull IdentityInfo broadcaster;
    @NonNull RewardDTO reward;

    @Override
    public @NonNull Optional<IdentityInfo> getOwner() {
        return Optional.of(broadcaster);
    }
}

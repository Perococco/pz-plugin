package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;

@Value
public class PZRedemptionResult {

    @NonNull String id;
    boolean completed;
}

package perobobbot.pzplugin;

import lombok.NonNull;
import perobobbot.pzplugin.json.PZRedemption;
import perobobbot.pzplugin.rewards.ParsedRedemption;

import java.util.Optional;

public interface RedemptionHandler {

    @NonNull Optional<PZRedemption> handleRedemption(@NonNull ParsedRedemption parsedRedemption);

}

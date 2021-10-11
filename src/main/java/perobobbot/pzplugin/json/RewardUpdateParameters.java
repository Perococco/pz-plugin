package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;
import perobobbot.pzplugin.Tools;
import perobobbot.pzplugin.rewards.Prompt;

import java.awt.*;
import java.util.Optional;

@Value
public class RewardUpdateParameters {

    @NonNull String id;
    @NonNull String kind;
    int cost;
    boolean useCredit;
    @NonNull String prompt;
    @NonNull String backgroundColor;

    String title;
    Boolean enabled;
    Boolean paused;
    Double availableTime;



    public int getActualCost() {
        if (useCredit) {
            return 1;
        }
        return cost;
    }

    public @NonNull String getFullPrompt() {
        return new Prompt(kind,useCredit?cost:0, Tools.decodeColor(backgroundColor),prompt.trim()).formPrompt();
    }

    public Optional<Color> getDecodedBackgroundColor() {
        return Optional.ofNullable(backgroundColor).map(Color::decode);
    }
}

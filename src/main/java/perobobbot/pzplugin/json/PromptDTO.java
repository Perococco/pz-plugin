package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;
import perobobbot.pzplugin.rewards.Prompt;

@Value
public class PromptDTO {

    @NonNull String kind;
    boolean useCredit;
    int cost;
    @NonNull String text;

}

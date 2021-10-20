package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;

@Value
public class PromptDTO {

    @NonNull String kind;
    boolean useCredit;
    int cost;
    @NonNull String text;

}

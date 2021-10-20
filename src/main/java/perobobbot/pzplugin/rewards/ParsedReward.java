package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.pzplugin.Tools;
import perobobbot.twitch.client.api.channelpoints.CustomReward;

import java.util.Optional;


@RequiredArgsConstructor
public class ParsedReward {


    public static @NonNull Optional<ParsedReward> from(@NonNull CustomReward customReward) {
        return Prompt.from(customReward).map(p -> new ParsedReward(p, customReward));
    }

    private final @NonNull Prompt prompt;
    private final @NonNull CustomReward customReward;


    public @NonNull String kind() {
        return prompt.kind();
    }

    public String getId() {
        return customReward.getId();
    }

    public String getBackgroundColor() {
        return customReward.isPaused()? Tools.encodeColor(prompt.color()):customReward.getBackgroundColor();
    }

    public boolean isEnabled() {
        return customReward.isEnabled();
    }

    public int getCost() {
        return useCredit()?prompt.cost():customReward.getCost();
    }

    @NonNull
    public String getTitle() {
        return customReward.getTitle();
    }

    @NonNull
    public String getPrompt() {
        return prompt.value();
    }

    public boolean isPaused() {
        return customReward.isPaused();
    }

    public boolean useCredit() {
        return prompt.useCredit();
    }
}

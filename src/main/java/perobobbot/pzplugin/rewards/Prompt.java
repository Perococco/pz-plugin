package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import perobobbot.pzplugin.Tools;
import perobobbot.twitch.client.api.channelpoints.CustomReward;

import java.awt.*;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Prompt(@NonNull String kind, int cost, @NonNull Color color, @NonNull String value) {

    public @NonNull String formPrompt() {
        final var cost = Integer.toString(cost());
        return value()+" //"+String.join(":", kind, cost, Tools.encodeColor(color));
    }


    public boolean useCredit() {
        return cost>0;
    }

    private static final Pattern PZ_ID = Pattern.compile("(?<value>.*)//(?<kind>[a-z_]+):(?<cost>[0-9]+):(?<color>.+)$");


    public static @NonNull Optional<Prompt> from(@NonNull CustomReward customReward) {
        return from(customReward.getPrompt());
    }

    public static @NonNull Optional<Prompt> from(@NonNull String customRewardPrompt) {
        return Optional.of(PZ_ID.matcher(customRewardPrompt))
                       .filter(Matcher::matches)
                       .map(m -> {
                           final var kind = m.group("kind");
                           final var cost = Integer.parseInt(m.group("cost"),10);
                           final var color = Tools.decodeColor(m.group("color"));
                           final var value = m.group("value");

                           return new Prompt(kind, cost, color, value.trim());
                       });
    }


}

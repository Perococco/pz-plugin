package perobobbot.pzplugin.json;

import lombok.NonNull;
import lombok.Value;
import org.springframework.jmx.export.annotation.ManagedNotification;
import perobobbot.pzplugin.Tools;
import perobobbot.pzplugin.endpoint.RewardUpdater;
import perobobbot.pzplugin.rewards.ParsedReward;
import perobobbot.pzplugin.rewards.Prompt;
import perobobbot.twitch.client.api.channelpoints.CustomReward;
import perobobbot.twitch.eventsub.api.event.ChannelPointsCustomRewardUpdateEvent;

import java.util.Optional;

@Value
public class RewardDTO {

    @NonNull String id;
    @NonNull String kind;
    boolean useCredit;
    @NonNull String backgroundColor;
    @NonNull String title;
    @NonNull String prompt;
    boolean enabled;
    int cost;
    boolean paused;

    public static @NonNull Optional<RewardDTO> from(@NonNull CustomReward reward) {
        return ParsedReward.from(reward).map(RewardDTO::from);
    }

    public static Optional<RewardDTO> from(@NonNull ChannelPointsCustomRewardUpdateEvent event) {
        final var prompt = Prompt.from(event.getPrompt());
        return prompt.map(p -> create(p,event));
    }

    private static RewardDTO create(@NonNull Prompt prompt, @NonNull ChannelPointsCustomRewardUpdateEvent event) {
        return new RewardDTO(
                event.getId(),
                prompt.kind(),
                prompt.useCredit(),
                event.isPaused()? Tools.encodeColor(prompt.color()):event.getBackgroundColor(),
                event.getTitle(),
                prompt.value(),
                event.isEnabled(),
                prompt.useCredit()?prompt.cost():event.getCost(),
                event.isPaused()
        );
    }


    public static RewardDTO from(@NonNull ParsedReward parsedReward) {
        return new RewardDTO(
                parsedReward.getId(),
                parsedReward.kind(),
                parsedReward.useCredit(),
                parsedReward.getBackgroundColor(),
                parsedReward.getTitle(),
                parsedReward.getPrompt(),
                parsedReward.isEnabled(),
                parsedReward.getCost(),
                parsedReward.isPaused()
        );
    }

}

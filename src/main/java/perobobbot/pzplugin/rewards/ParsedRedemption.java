package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.lang.IdentityInfo;
import perobobbot.twitch.api.UserInfo;
import perobobbot.twitch.eventsub.api.event.ChannelPointsCustomRewardRedemptionAddEvent;

import java.util.Optional;


@RequiredArgsConstructor
public class ParsedRedemption {


    public static @NonNull Optional<ParsedRedemption> from(@NonNull ChannelPointsCustomRewardRedemptionAddEvent redemptionAddEvent) {
        return Prompt.from(redemptionAddEvent.getReward().getPrompt()).map(p -> new ParsedRedemption(p, redemptionAddEvent));
    }

    private final @NonNull Prompt prompt;
    private final @NonNull ChannelPointsCustomRewardRedemptionAddEvent redemptionAddEvent;


    public boolean useCredit() {
        return prompt.useCredit();
    }

    public @NonNull UserInfo getUserInfo() {
        return redemptionAddEvent.getUser();
    }

    public @NonNull String getChannelName() {
        return redemptionAddEvent.getBroadcaster().getLogin();
    }

    public int getCost() {
        return prompt.useCredit()?prompt.cost():redemptionAddEvent.getReward().getCost();
    }

    public @NonNull IdentityInfo getBroadcaster() {
        return redemptionAddEvent.getBroadcaster().asIdentityInfo();
    }

    public @NonNull String getKind() {
        return prompt.kind();
    }

    public @NonNull String getUserInput() {
        return redemptionAddEvent.getUserInput();
    }

    public @NonNull String getRedemptionId() {
        return redemptionAddEvent.getId();
    }

    public @NonNull String getRewardId() {
        return redemptionAddEvent.getReward().getId();
    }
}

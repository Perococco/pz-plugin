package perobobbot.pzplugin.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.endpoint.EndPoint;
import perobobbot.endpoint.SecuredEndPoint;
import perobobbot.pzplugin.Tools;
import perobobbot.pzplugin.json.RewardDTO;
import perobobbot.pzplugin.json.RewardUpdateParameters;
import perobobbot.twitch.client.api.TwitchService;
import perobobbot.twitch.client.api.channelpoints.UpdateCustomRewardParameter;

import java.awt.*;
import java.time.Duration;

@RequiredArgsConstructor
public class RewardUpdater implements EndPoint<RewardUpdateParameters> {

    public static final Color PAUSED_COLOR = new Color(208, 208, 208);


    public static @NonNull SecuredEndPoint<RewardUpdateParameters> asSecuredEndPoint(@NonNull TwitchService twitchService) {
        return user -> new RewardUpdater(twitchService);
    }

    private final @NonNull TwitchService twitchService;

    @Override
    public @NonNull Class<RewardUpdateParameters> getBodyType() {
        return RewardUpdateParameters.class;
    }

    @Override
    public RewardDTO handle(RewardUpdateParameters body) {

        //TODO take care of availableTime

        final var paused = body.getPaused();

        final var builder = UpdateCustomRewardParameter.builder()
                                                       .cost(body.getActualCost())
                                                       .enabled(body.getEnabled())
                                                       .title(body.getTitle())
                                                       .paused(paused)
                                                       .prompt(body.getFullPrompt());

        if ("say".equals(body.getKind())) {
            System.out.println("### "+body.getKind() + " PAUSED="+ paused);
        }

        if (paused != null && paused) {
            builder.backgroundColor(PAUSED_COLOR);
        } else {
            builder.backgroundColor(Tools.decodeColor(body.getBackgroundColor()));
        }

        return twitchService.updateCustomReward(body.getId(), builder.build())
                            .map(RewardDTO::from)
                            .map(o -> o.orElseThrow(() -> new RuntimeException("Bug in prompt parsing")))
                            .block(Duration.ofSeconds(30));
    }

}

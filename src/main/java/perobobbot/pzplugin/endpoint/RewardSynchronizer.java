package perobobbot.pzplugin.endpoint;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.endpoint.EndPoint;
import perobobbot.endpoint.SecuredEndPoint;
import perobobbot.lang.Nil;
import perobobbot.pzplugin.json.RewardDTO;
import perobobbot.pzplugin.rewards.CustomRewardSynchronizer;
import perobobbot.pzplugin.rewards.ParsedReward;
import perobobbot.security.com.SimpleUser;
import perobobbot.twitch.client.api.TwitchService;
import perobobbot.twitch.client.api.channelpoints.GetCustomRewardsParameter;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class RewardSynchronizer implements EndPoint<Nil> {

    public static @NonNull SecuredEndPoint<Nil> asSecuredEndPoint(@NonNull TwitchService twitchService) {
        return user -> new RewardSynchronizer(user, twitchService);
    }

    private static final GetCustomRewardsParameter ALL_MANAGEABLE_REWARD = new GetCustomRewardsParameter(new String[]{}, true);

    private final @NonNull SimpleUser user;
    private final @NonNull TwitchService twitchService;

    @Override
    public @NonNull Class<Nil> getBodyType() {
        return Nil.class;
    }

    @Override
    public List<RewardDTO> handle(Nil nil) {
        return RewardRetriever.retrieve(twitchService)
                              .flatMap(CustomRewardSynchronizer.synchronizeWith(twitchService, user.getLocale()))
                              .then(RewardRetriever.retrieve(twitchService))
                              .map(m -> m.values().stream().map(RewardDTO::from).toList())
                              .block();

    }

    private Mono<ImmutableMap<String, ParsedReward>> retrieveRewardOnPlatform() {

        return twitchService.getCustomReward(ALL_MANAGEABLE_REWARD)
                            .subscribeOn(SCHEDULER)
                            .map(ParsedReward::from)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(ImmutableMap.toImmutableMap(ParsedReward::kind, Function.identity()));
    }
}

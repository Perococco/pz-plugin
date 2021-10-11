package perobobbot.pzplugin.endpoint;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.endpoint.EndPoint;
import perobobbot.pzplugin.rewards.ParsedReward;
import perobobbot.twitch.client.api.TwitchService;
import perobobbot.twitch.client.api.channelpoints.GetCustomRewardsParameter;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class RewardRetriever {


    public static @NonNull  Mono<ImmutableMap<String, ParsedReward>> retrieve(@NonNull TwitchService twitchService) {
        return new RewardRetriever(twitchService).retrieve();
    }

    private static final GetCustomRewardsParameter ALL_MANAGEABLE_REWARD = new GetCustomRewardsParameter(new String[]{}, true);

    private final @NonNull TwitchService twitchService;

    private @NonNull Mono<ImmutableMap<String, ParsedReward>> retrieve() {

        return twitchService.getCustomReward(ALL_MANAGEABLE_REWARD)
                            .subscribeOn(EndPoint.SCHEDULER)
                            .map(ParsedReward::from)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(ImmutableMap.toImmutableMap(ParsedReward::kind, Function.identity()));
    }

}

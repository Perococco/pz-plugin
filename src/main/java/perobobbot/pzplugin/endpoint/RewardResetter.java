package perobobbot.pzplugin.endpoint;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import perobobbot.endpoint.EndPoint;
import perobobbot.endpoint.SecuredEndPoint;
import perobobbot.lang.Nil;
import perobobbot.lang.ThrowableTool;
import perobobbot.pzplugin.json.RewardDTO;
import perobobbot.pzplugin.rewards.ParsedReward;
import perobobbot.security.com.SimpleUser;
import perobobbot.twitch.client.api.TwitchService;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class RewardResetter implements EndPoint<Nil> {

    public static @NonNull SecuredEndPoint<Nil> asSecuredEndPoint(@NonNull TwitchService twitchService) {
        return user -> new RewardResetter(user,twitchService);
    }

    private final @NonNull SimpleUser user;
    private final @NonNull TwitchService twitchService;

    @Override
    public @NonNull Class<Nil> getBodyType() {
        return Nil.class;
    }

    @Override
    public List<RewardDTO> handle(Nil body) {
        RewardRetriever.retrieve(twitchService)
                       .flatMap(this::mapMany)
                       .block();
        return new RewardSynchronizer(user,twitchService).handle(body);
    }

    private @NonNull Mono<?> mapMany(@NonNull ImmutableMap<String, ParsedReward> rewards) {
        return Mono.when(rewards.values().stream().map(this::removeReward).toList());
    }

    private @NonNull Mono<Nil> removeReward(@NonNull ParsedReward parsedReward) {
        final var id = parsedReward.getId();
        return twitchService.deleteCustomRewards(id)
                            .map(o -> Nil.NIL)
                            .onErrorResume((t) -> {
                                LOG.warn("Fail to remove reward {} : {}", id, ThrowableTool.formCauseMessageChain(t));
                                return Mono.just(Nil.NIL);
                            });
    }

}

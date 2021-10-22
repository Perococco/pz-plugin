package perobobbot.pzplugin.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.endpoint.EndPoint;
import perobobbot.endpoint.SecuredEndPoint;
import perobobbot.lang.Nil;
import perobobbot.pzplugin.json.RewardDTO;
import perobobbot.pzplugin.rewards.CustomRewardSynchronizer;
import perobobbot.security.com.SimpleUser;
import perobobbot.twitch.client.api.TwitchService;

import java.util.List;

@RequiredArgsConstructor
public class RewardSynchronizer implements EndPoint<Nil> {

    public static @NonNull SecuredEndPoint<Nil> asSecuredEndPoint(@NonNull TwitchService twitchService) {
        return user -> new RewardSynchronizer(user, twitchService);
    }


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

}

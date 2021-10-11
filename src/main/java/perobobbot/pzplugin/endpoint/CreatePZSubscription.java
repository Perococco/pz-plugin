package perobobbot.pzplugin.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import perobobbot.data.service.SubscriptionService;
import perobobbot.data.service.ViewerIdentityService;
import perobobbot.endpoint.EndPoint;
import perobobbot.endpoint.SecuredEndPoint;
import perobobbot.lang.Conditions;
import perobobbot.lang.Nil;
import perobobbot.lang.Platform;
import perobobbot.lang.SubscriptionData;
import perobobbot.security.com.SimpleUser;
import perobobbot.twitch.eventsub.api.CriteriaType;
import perobobbot.twitch.eventsub.api.SubscriptionType;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Log4j2
public class CreatePZSubscription implements EndPoint<Nil> {

    public static @NonNull SecuredEndPoint<Nil> asSecuredEndPoint(@NonNull SubscriptionService subscriptionService, @NonNull ViewerIdentityService viewerIdentityService) {
        return user -> new CreatePZSubscription(user, subscriptionService,viewerIdentityService);
    }


    private final @NonNull SimpleUser user;
    private final @NonNull SubscriptionService subscriptionService;
    private final @NonNull ViewerIdentityService viewerIdentityService;

    @Override
    public @NonNull Class<Nil> getBodyType() {
        return Nil.class;
    }

    @Override
    public Object handle(Nil body) {
        final var viewerIdentity = viewerIdentityService.findIdentity(Platform.TWITCH, user.getLogin()).orElseThrow(() -> new RuntimeException("TODO"));

        Stream.of(
                      SubscriptionType.CHANNEL_CHANNEL_POINTS_CUSTOM_REWARD_ADD,
                      SubscriptionType.CHANNEL_CHANNEL_POINTS_CUSTOM_REWARD_REMOVE,
                      SubscriptionType.CHANNEL_CHANNEL_POINTS_CUSTOM_REWARD_UPDATE,
                      SubscriptionType.CHANNEL_CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_ADD,
                      SubscriptionType.CHANNEL_CHANNEL_POINTS_CUSTOM_REWARD_REDEMPTION_UPDATE
              )
              .forEach(s -> addSubscription(s, viewerIdentity.getViewerId()));

        return Nil.NIL;
    }


    private void addSubscription(@NonNull SubscriptionType subscriptionType, @NonNull String userId) {
        try {
            final var data = new SubscriptionData(Platform.TWITCH, subscriptionType, Conditions.with(CriteriaType.BROADCASTER_USER_ID, userId));
            final var subscription = subscriptionService.getOrCreateSubscription(data);
            subscriptionService.addUserToSubscription(subscription.getId(), user.getLogin());
        } catch (Throwable t) {
            LOG.error("Could no create subscription {} : {}",subscriptionType,t.getMessage());
        }
    }
}

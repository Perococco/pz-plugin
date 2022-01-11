package perobobbot.pzplugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jplugman.api.ServiceProvider;
import lombok.NonNull;
import org.springframework.web.servlet.HandlerMapping;
import perobobbot.data.service.PlatformUserService;
import perobobbot.data.service.SubscriptionService;
import perobobbot.data.service.UserService;
import perobobbot.endpoint.RequestFactory;
import perobobbot.lang.UserAuthenticator;
import perobobbot.pzplugin.endpoint.*;
import perobobbot.pzplugin.redemption.RedemptionMap;
import perobobbot.twitch.client.api.TwitchService;


public class PZHandleMappingFactory {


    private final @NonNull ObjectMapper objectMapper;
    private final @NonNull UserAuthenticator userAuthenticator;
    private final @NonNull TwitchService twitchService;
    private final @NonNull UserService userService;
    private final @NonNull PlatformUserService platformUserService;
    private final @NonNull SubscriptionService subscriptionService;
    private final @NonNull RedemptionMap redemptionMap;

    public PZHandleMappingFactory(ServiceProvider serviceProvider, @NonNull RedemptionMap redemptionMap) {
        this.objectMapper = serviceProvider.getAnyService(Requirements.OBJECT_MAPPER_FACTORY).create();
        this.twitchService = serviceProvider.getAnyService(Requirements.TWITCH_SERVICE);
        this.userService = serviceProvider.getAnyService(Requirements.USER_SERVICE);
        this.userAuthenticator = serviceProvider.getAnyService(Requirements.USER_AUTHENTICATOR);
        this.platformUserService = serviceProvider.getAnyService(Requirements.PLATFORM_USER_SERVICE);
        this.subscriptionService = serviceProvider.getAnyService(Requirements.SUBSCRIPTION_SERVICE);
        this.redemptionMap = redemptionMap;
    }

    public @NonNull HandlerMapping create() {
        final var factory = RequestFactory.jsonBased(objectMapper,userAuthenticator,userService);
        final var b = perobobbot.endpoint.HandlerMappingBuilder.create(factory);

        return b.put("/pz/rewards/sync", RewardSynchronizer.asSecuredEndPoint(twitchService))
                .put("/pz/rewards/reset", RewardResetter.asSecuredEndPoint(twitchService))
                .put("/pz/rewards", RewardUpdater.asSecuredEndPoint(twitchService))
                .put("/pz/subscriptions", CreatePZSubscription.asSecuredEndPoint(subscriptionService, platformUserService))
                .post("/pz/redemptions", PZRedemptionHandler.asSecuredEndPoint(redemptionMap))
                .build();
    }


}

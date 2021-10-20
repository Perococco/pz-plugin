package perobobbot.pzplugin.endpoint;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.endpoint.EndPoint;
import perobobbot.endpoint.SecuredEndPoint;
import perobobbot.lang.Nil;
import perobobbot.pzplugin.json.PZRedemptionResult;
import perobobbot.pzplugin.redemption.RedemptionMap;
import perobobbot.security.com.SimpleUser;

@RequiredArgsConstructor
public class PZRedemptionHandler implements EndPoint<PZRedemptionResult> {


    public static @NonNull SecuredEndPoint<PZRedemptionResult> asSecuredEndPoint(@NonNull RedemptionMap redemptionMap) {
        return user -> new PZRedemptionHandler(user, redemptionMap);
    }

    private final @NonNull SimpleUser user;
    private final @NonNull RedemptionMap redemptionMap;

    @Override
    public @NonNull Class<PZRedemptionResult> getBodyType() {
        return PZRedemptionResult.class;
    }

    @Override
    public Object handle(PZRedemptionResult body) {
        final var completer = redemptionMap.extract(body.getId()).orElse(null);
        if (completer == null) {
            return Nil.NIL;
        }
        final Runnable action = body.isCompleted()?completer::complete:completer::reject;
        action.run();
        return Nil.NIL;
    }
}


package perobobbot.pzplugin.redemption;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.lang.Nil;
import perobobbot.lang.Platform;
import perobobbot.lang.fp.Function1;
import perobobbot.oauth.BroadcasterIdentifier;
import perobobbot.oauth.OAuthTokenIdentifierSetter;
import perobobbot.pzplugin.RedemptionCompleter;
import perobobbot.pzplugin.rewards.ParsedRedemption;
import perobobbot.twitch.api.RewardRedemptionStatus;
import perobobbot.twitch.api.UserInfo;
import perobobbot.twitch.client.api.TwitchService;
import perobobbot.twitch.client.api.channelpoints.UpdateRedemptionStatus;

@RequiredArgsConstructor
public class RewardRedemptionHandler {


    public static @NonNull Function1<ParsedRedemption, RedemptionCompleter> handler(@NonNull TwitchService twitchService, @NonNull OAuthTokenIdentifierSetter oAuthTokenIdentifierSetter) {
        return r -> handle(twitchService, oAuthTokenIdentifierSetter, r);
    }

    public static @NonNull RedemptionCompleter handle(@NonNull TwitchService twitchService,
                                                      @NonNull OAuthTokenIdentifierSetter oAuthTokenIdentifierSetter,
                                                      @NonNull ParsedRedemption parsedRedemption) {
        return new RewardRedemptionHandler(twitchService, oAuthTokenIdentifierSetter, parsedRedemption).handle();
    }

    private final @NonNull TwitchService twitchService;
    private final @NonNull OAuthTokenIdentifierSetter oAuthTokenIdentifierSetter;
    private final @NonNull ParsedRedemption parsedRedemption;


    private @NonNull RedemptionCompleter handle() {
        System.out.println("Perform redemption for " + parsedRedemption.getUserInfo().getName());
        return new TransactionCompleter(parsedRedemption.getUserInfo(), parsedRedemption.getRewardId(), parsedRedemption.getRedemptionId());
    }

    @RequiredArgsConstructor
    private class TransactionCompleter implements RedemptionCompleter {

        private final @NonNull UserInfo userInfo;
        private final @NonNull String rewardId;
        private final @NonNull String redemptionId;

        @Override
        public void complete() {
            update(RewardRedemptionStatus.FULFILLED);
        }

        @Override
        public void reject() {
            update(RewardRedemptionStatus.CANCELED);
        }

        private void update(@NonNull RewardRedemptionStatus status) {
            System.out.println("## Complete redemption for '" + userInfo.getName() + "' with status : " + status);
            oAuthTokenIdentifierSetter.wrapRun(new BroadcasterIdentifier(Platform.TWITCH, userInfo.getId()),
                    () -> twitchService.updateRedemptionStatus(rewardId, new String[]{redemptionId}, new UpdateRedemptionStatus(status)));
        }
    }
}

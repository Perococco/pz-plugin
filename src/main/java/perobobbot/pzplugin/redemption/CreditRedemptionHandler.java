package perobobbot.pzplugin.redemption;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.data.com.NotEnoughPoints;
import perobobbot.data.service.BankService;
import perobobbot.data.service.PlatformUserService;
import perobobbot.lang.Platform;
import perobobbot.lang.PointType;
import perobobbot.lang.TwitchIdentity;
import perobobbot.lang.fp.Function1;
import perobobbot.oauth.BroadcasterIdentifier;
import perobobbot.oauth.OAuthContextHolder;
import perobobbot.oauth.OAuthTokenIdentifierSetter;
import perobobbot.pzplugin.RedemptionCompleter;
import perobobbot.pzplugin.rewards.ParsedRedemption;
import perobobbot.twitch.api.RewardRedemptionStatus;
import perobobbot.twitch.api.UserInfo;
import perobobbot.twitch.client.api.TwitchService;
import perobobbot.twitch.client.api.channelpoints.UpdateRedemptionStatus;

import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
public class CreditRedemptionHandler {


    public static @NonNull Function1<ParsedRedemption,RedemptionCompleter> handler(@NonNull PlatformUserService platformUserService,
                                                                                   @NonNull BankService bankService,
                                                                                   @NonNull TwitchService twitchService,
                                                                                   @NonNull OAuthTokenIdentifierSetter oAuthTokenIdentifierSetter) {
        return r -> handle(platformUserService,bankService,twitchService,oAuthTokenIdentifierSetter,r);
    }

    public static @NonNull RedemptionCompleter handle(@NonNull PlatformUserService platformUserService,
                                                      @NonNull BankService bankService,
                                                      @NonNull TwitchService twitchService,
                                                      @NonNull OAuthTokenIdentifierSetter oAuthTokenIdentifierSetter,
                                                      @NonNull ParsedRedemption parsedRedemption) {
        return new CreditRedemptionHandler(platformUserService, bankService,twitchService,oAuthTokenIdentifierSetter,parsedRedemption).handle();
    }

    private final @NonNull PlatformUserService platformUserService;
    private final @NonNull BankService bankService;
    private final @NonNull TwitchService twitchService;
    private final @NonNull OAuthTokenIdentifierSetter oAuthTokenIdentifierSetter;
    private final @NonNull ParsedRedemption parsedRedemption;


    private @NonNull RedemptionCompleter handle() {
        final var userInfo = parsedRedemption.getUserInfo();
        final var channelName = parsedRedemption.getChannelName();
        final var cost = parsedRedemption.getCost();
        final var identity = platformUserService.updateUserIdentity(new TwitchIdentity(userInfo.getId(), userInfo.getLogin(), userInfo.getName()));
        final var tokenIdentifier = new BroadcasterIdentifier(Platform.TWITCH,userInfo.getId());

        oAuthTokenIdentifierSetter.wrapRun(tokenIdentifier, this::cancelRedemption);

        final var safe = bankService.findSafe(identity.getId(), channelName);

        System.out.println("## Create transaction for '"+userInfo.getName()+"' for "+cost+" credits");

        try {
            final var transaction = bankService.createTransaction(safe.getId(), PointType.CREDIT, cost, Duration.ofMinutes(10));
            return new TransactionCompleter(userInfo,cost,transaction.getId());
        } catch (NotEnoughPoints nep) {
            System.out.println(userInfo.getName()+" does not have enough credits : asked for "+cost);
            throw nep;
        }
    }

    private void cancelRedemption() {
        twitchService.updateRedemptionStatus(parsedRedemption.getRewardId(), new String[]{parsedRedemption.getRedemptionId()}, new UpdateRedemptionStatus(RewardRedemptionStatus.CANCELED))
                .subscribe();
    }

    @RequiredArgsConstructor
    private class TransactionCompleter implements RedemptionCompleter {

        private final @NonNull UserInfo userInfo;
        private final int cost;
        private final @NonNull UUID transactionId;

        @Override
        public void complete() {
            System.out.println("## Complete transaction for '"+userInfo.getName()+"' for "+cost+" credits");
            bankService.completeTransaction(transactionId);
        }

        @Override
        public void reject() {
            System.out.println("## Reject transaction for '"+userInfo.getName()+"' for "+cost+" credits");
            bankService.cancelTransaction(transactionId);
        }
    }
}

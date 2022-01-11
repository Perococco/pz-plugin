package perobobbot.pzplugin.redemption;

import jplugman.api.ServiceProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import perobobbot.lang.ThrowableTool;
import perobobbot.lang.fp.Function1;
import perobobbot.pzplugin.RedemptionCompleter;
import perobobbot.pzplugin.RedemptionHandler;
import perobobbot.pzplugin.Requirements;
import perobobbot.pzplugin.json.PZRedemption;
import perobobbot.pzplugin.rewards.ParsedRedemption;

import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
public class DefaultRedemptionHandler implements RedemptionHandler {

    private final @NonNull RedemptionMap redemptionMap;

    private final Function1<ParsedRedemption, RedemptionCompleter> creditHandler;
    private final Function1<ParsedRedemption, RedemptionCompleter> rewardHandler;

    public DefaultRedemptionHandler(
            @NonNull ServiceProvider serviceProvider,
            @NonNull RedemptionMap redemptionMap) {
        this.redemptionMap = redemptionMap;
        final var platformUserService = serviceProvider.getAnyService(Requirements.PLATFORM_USER_SERVICE);
        final var bankService = serviceProvider.getAnyService(Requirements.BANK_SERVICE);
        final var twitchService = serviceProvider.getAnyService(Requirements.TWITCH_SERVICE);
        final var oauth = serviceProvider.getAnyService(Requirements.O_AUTH_TOKEN_IDENTIFIER_SETTER);
        this.creditHandler = CreditRedemptionHandler.handler(platformUserService, bankService,twitchService,oauth);
        this.rewardHandler = RewardRedemptionHandler.handler(twitchService,oauth);
    }

    public @NonNull Optional<PZRedemption> handleRedemption(@NonNull ParsedRedemption parsedRedemption) {
        return formCompleter(parsedRedemption)
                .map(c -> formPZRedemption(c, parsedRedemption));
    }

    private @NonNull Optional<RedemptionCompleter> formCompleter(@NonNull ParsedRedemption parsedRedemption) {
        try {
            final Function1<ParsedRedemption, RedemptionCompleter> handler;
            {
                if (parsedRedemption.useCredit()) {
                    handler = creditHandler;
                } else {
                    handler = rewardHandler;
                }
            }
            return Optional.of(handler.f(parsedRedemption));
        } catch (Throwable t) {
            LOG.warn("Fail to handle redemption : '{}'", ThrowableTool.formCauseMessageChain(t));
            return Optional.empty();
        }
    }

    private @NonNull PZRedemption formPZRedemption(@NonNull RedemptionCompleter completer, @NonNull ParsedRedemption parsedRedemption) {
        final var id = redemptionMap.addCompleter(completer);
        return new PZRedemption(id,
                parsedRedemption.getBroadcaster(),
                parsedRedemption.getKind(),
                parsedRedemption.getUserInput(),
                parsedRedemption.getUserInfo());
    }


}

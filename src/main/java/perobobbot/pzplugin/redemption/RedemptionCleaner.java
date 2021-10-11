package perobobbot.pzplugin.redemption;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.lang.Looper;
import perobobbot.lang.WaitStrategy;

@RequiredArgsConstructor
public class RedemptionCleaner extends Looper {

    private final @NonNull RedemptionMap redemptionMap;
    private final @NonNull WaitStrategy waitStrategy;

    @Override
    protected @NonNull IterationCommand performOneIteration() throws Exception {
        waitStrategy.waitFor(RedemptionMap.TIMEOUT_DURATION);
        redemptionMap.checkTimeout();
        return IterationCommand.CONTINUE;
    }

}

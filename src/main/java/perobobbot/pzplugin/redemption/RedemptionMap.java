package perobobbot.pzplugin.redemption;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import perobobbot.pzplugin.RedemptionCompleter;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class RedemptionMap {

    public static final Duration TIMEOUT_DURATION = Duration.ofSeconds(30);

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    private final Map<String, Data> redemptionCompleterMap = new HashMap<>();


    @Synchronized
    public @NonNull String addCompleter(@NonNull RedemptionCompleter redemptionCompleter) {
        final var id = String.valueOf(ID_GENERATOR.incrementAndGet());
        this.redemptionCompleterMap.put(id, new Data(redemptionCompleter));
        return id;
    }

    @Synchronized
    public @NonNull Optional<RedemptionCompleter> extract(@NonNull String id) {
        final var data = redemptionCompleterMap.remove(id);
        return Optional.ofNullable(data).map(d -> d.redemptionCompleter);
    }

    @Synchronized
    public void checkTimeout() {
        final var now = Instant.now();
        final var iter = redemptionCompleterMap.values().iterator();
        while (iter.hasNext()) {
            final var data = iter.next();
            if (data.isTimedOut(now)) {
                iter.remove();
                data.redemptionCompleter.reject();
            }
        }
    }

    @RequiredArgsConstructor
    private static class Data {
        private final @NonNull RedemptionCompleter redemptionCompleter;
        private final Instant timeoutInstant = Instant.now().plus(TIMEOUT_DURATION);

        public boolean isTimedOut(Instant now) {
            return now.isAfter(timeoutInstant);
        }
    }
}

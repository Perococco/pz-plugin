package perobobbot.pzplugin.rewards;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardLoader {

    public static @NonNull ImmutableSet<DefaultRewardInfo> load(@NonNull Locale locale) {
        final var resourceBundle = ResourceBundle.getBundle("rewards", locale);
        return new RewardLoader(resourceBundle).load();
    }

    private final ResourceBundle resourceBundle;

    private @NonNull ImmutableSet<DefaultRewardInfo> load() {
        return RewardKind.stream()
                         .map(this::createDescription)
                         .flatMap(Optional::stream)
                         .collect(ImmutableSet.toImmutableSet());
    }

    private @NonNull Optional<DefaultRewardInfo> createDescription(RewardKind rewardKind) {
        try {
            final var title = resourceBundle.getString(rewardKind.getTitleI18nKey());
            final var prompt = resourceBundle.getString(rewardKind.getPromptI18nKey());
            return Optional.of(new DefaultRewardInfo(rewardKind, title, prompt));
        } catch (MissingResourceException mre) {
            LOG.warn("Title or prompt not defined for reward {}", rewardKind);
            return Optional.empty();
        }
    }

}

package perobobbot.pzplugin.rewards;

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

public class TestParsedRewardKindLoading {

    public static Stream<ImmutableSet<DefaultRewardInfo>> rewards() {
        return Stream.of(Locale.ENGLISH, Locale.FRENCH)
                     .map(RewardLoader::load);
    }

//    @ParameterizedTest
//    @MethodSource("rewards")
//    public void shouldHave13Rewards(@NonNull ImmutableSet<DefaultRewardInfo> defaultRewards) {
//        Assertions.assertEquals(RewardKind.values().length, defaultRewards.size());
//    }
}

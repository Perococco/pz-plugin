package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.Assert;

import java.awt.*;
import java.util.stream.Stream;

public class RewardInfoSerDeserTest {


    public static Stream<RewardInfo> rewardInfoStream() {
        return Stream.of(
                new RewardInfo("Velo",1200, Color.YELLOW),
                new RewardInfo("Voiture",20_000, Color.BLUE),
                new RewardInfo("Voiture de sport",50_000, Color.RED),
                new RewardInfo("Voiture de luxe",100_000, Color.BLACK)
        );
    }

    @ParameterizedTest
    @MethodSource("rewardInfoStream")
    public void testSerDeser(@NonNull RewardInfo rewardInfo) {
        final var serialized = rewardInfo.serialize();
        System.out.println("serialized "+serialized);
        final var deserialized = RewardInfo.deserialize(serialized);

        Assertions.assertEquals(rewardInfo,deserialized);
    }
}

package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.stream.Stream;

public class TestPromptParser001 {


    public static Stream<Arguments> prompts() {
        return Stream.of(
                Arguments.of("toto hello //god_mod:0:#FF0000","toto hello","god_mod",0, Color.RED),
                Arguments.of("titi //god_mod:0:#00FF00","titi","god_mod",0,Color.GREEN),
                Arguments.of("titi //hello:1230:#0000FF","titi","hello",1230,Color.BLUE),
                Arguments.of("titi //hello:012:#ffffFF","titi","hello",12,Color.WHITE)
        );
    }

    @ParameterizedTest
    @MethodSource("prompts")
    public void shouldMatchKind(@NonNull String fullPrompt, @NonNull String value, @NonNull String kind, int cost, Color color) {
        final var prompt = Prompt.from(fullPrompt).orElse(null);
        Assertions.assertNotNull(prompt);
        Assertions.assertEquals(kind,prompt.kind());
    }

    @ParameterizedTest
    @MethodSource("prompts")
    public void shouldMatchCost(@NonNull String fullPrompt, @NonNull String value, @NonNull String kind, int cost, Color color) {
        final var prompt = Prompt.from(fullPrompt).orElse(null);
        Assertions.assertNotNull(prompt);
        Assertions.assertEquals(cost,prompt.cost());
    }

    @ParameterizedTest
    @MethodSource("prompts")
    public void shouldMatchValue(@NonNull String fullPrompt, @NonNull String value, @NonNull String kind, int cost, Color color) {
        final var prompt = Prompt.from(fullPrompt).orElse(null);
        Assertions.assertNotNull(prompt);
        Assertions.assertEquals(value,prompt.value());
    }
    @ParameterizedTest
    @MethodSource("prompts")
    public void shouldMatchColor(@NonNull String fullPrompt, @NonNull String value, @NonNull String kind, int cost, Color color) {
        final var prompt = Prompt.from(fullPrompt).orElse(null);
        Assertions.assertNotNull(prompt);
        Assertions.assertEquals(color,prompt.color());
    }
}

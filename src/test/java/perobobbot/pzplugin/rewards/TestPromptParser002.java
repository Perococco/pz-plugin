package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.Assert;

import java.awt.*;
import java.util.stream.Stream;

public class TestPromptParser002 {


    public static Stream<Prompt> prompts() {
        return Stream.of(
                new Prompt("toto",100, Color.BLACK,"Hello")
        );
    }

    @ParameterizedTest
    @MethodSource("prompts")
    public void testIdempotent(@NonNull Prompt prompt) {
        final var full = prompt.formPrompt();
        final var p = Prompt.from(full).orElse(null);
        Assertions.assertEquals(prompt,p);
    }
}

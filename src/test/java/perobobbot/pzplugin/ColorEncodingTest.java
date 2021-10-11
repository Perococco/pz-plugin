package perobobbot.pzplugin;

import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.awt.*;
import java.util.stream.Stream;

public class ColorEncodingTest {

    public static Stream<Color> colors() {
        return Stream.of(Color.BLACK,Color.RED,Color.GREEN,Color.BLUE,Color.WHITE,Color.GRAY,Color.CYAN);
    }

    @ParameterizedTest
    @MethodSource("colors")
    public void testEncodeDecode(@NonNull Color color) {
        final var actual = Tools.decodeColor(Tools.encodeColor(color));
        Assertions.assertEquals(color,actual);
    }
}

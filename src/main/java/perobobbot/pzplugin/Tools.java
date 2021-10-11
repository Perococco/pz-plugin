package perobobbot.pzplugin;

import lombok.NonNull;

import java.awt.*;

public class Tools {

    public static @NonNull String encodeColor(@NonNull Color color) {
        return String.format("#%02x%02x%02x",color.getRed(), color.getGreen(), color.getBlue());
    }

    public static @NonNull Color decodeColor(@NonNull String encodedColor) {
        return Color.decode(encodedColor);
    }

}

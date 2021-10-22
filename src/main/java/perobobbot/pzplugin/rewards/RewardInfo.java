package perobobbot.pzplugin.rewards;

import lombok.NonNull;
import lombok.Value;
import perobobbot.pzplugin.Tools;

import java.awt.*;
import java.io.*;
import java.util.Base64;

@Value
public class RewardInfo {
    @NonNull String kind;
    int cost;
    @NonNull Color color;


    public @NonNull String serialize() {
        final var tag = kind+":"+cost+":"+ Tools.encodeColor(color);

        final var b = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(b)) {
            oos.writeUTF(kind);
            oos.writeInt(cost);
            oos.writeInt(color.getRGB());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return Base64.getEncoder().encodeToString(b.toByteArray());
    }

    public static RewardInfo deserialize(@NonNull String serialized) {
        final var bytes = Base64.getDecoder().decode(serialized);
        try (ObjectInputStream b = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            final var kind = b.readUTF();
            final var cost = b.readInt();
            final var color = new Color(b.readInt());
            return new RewardInfo(kind,cost,color);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}

package perobobbot.pzplugin.rewards;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.stream.Stream;

@RequiredArgsConstructor
public enum RewardKind {
    ALARM("alarm",4_000,true,false),
    DRUNK("drunk",1_000,true,false),
    SAY("say",200,false,true),
    GUNSHOT("gunshot",1_000,true,false),
    CALL_HELICOPTER("call_helicopter",10_000,true,false),
    CANCEL_HELICOPTER("cancel_helicopter",5_000,false,false),
    FULL_HEALTH("full_health",5_000,false,false),
    GOD_MOD("god_mod",20_000,false,false),
    WOUND("wound",10_000,true,false),
    NUDE("tohot",1_000,true,false),
    SHOELESS("shoeless",2_000,true,false),
    SHOUT("shout",2_000,true,false),
    SPAWN_HORDE("spawn_horde",10_000,true,false),
    ;

    private static final Color BAD_COLOR = new Color(239, 109, 109);
    private static final Color GOOD_COLOR = new Color(129, 211, 129);

    private static final String TITLE_SUFFIX = ".title";
    private static final String PROMPT_SUFFIX = ".prompt";

    private final @NonNull String base;
    @Getter
    private final int defaultCost;
    private final boolean badAction;
    @Getter
    private final boolean userInputRequired;

    public @NonNull String getTitleI18nKey() {
        return base+TITLE_SUFFIX;
    }
    public @NonNull String getPromptI18nKey() {
        return base+PROMPT_SUFFIX;
    }


    public @NonNull Color getDefaultColor() {
        return badAction?BAD_COLOR:GOOD_COLOR;
    }
    public static @NonNull Stream<RewardKind> stream() {
        return Holder.VALUES.stream();
    }

    public @NonNull String getId() {
        return base;
    }

    private static class Holder {
        private static @NonNull ImmutableList<RewardKind> VALUES = ImmutableList.copyOf(values());
    }


}

package perobobbot.pzplugin.rewards;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.twitch.client.api.channelpoints.CreateCustomRewardParameter;

@RequiredArgsConstructor
public class DefaultRewardInfo {

    private final @NonNull RewardKind rewardKind;
    @Getter
    private final @NonNull String title;
    private final @NonNull Prompt prompt;

    public DefaultRewardInfo(@NonNull RewardKind rewardKind, @NonNull String title, @NonNull String prompt) {
        this.rewardKind = rewardKind;
        this.title = title;
        this.prompt = new Prompt(rewardKind.getId(), 0, rewardKind.getDefaultColor(),prompt);
    }

    public @NonNull String id() {
        return rewardKind.getId();
    }

    public @NonNull CreateCustomRewardParameter formCreateCustomRewardParameter() {
        return CreateCustomRewardParameter.builder()
                                          .cost(rewardKind.getDefaultCost())
                                          .title(title)
                                          .userInputRequired(rewardKind.isUserInputRequired())
                                          .backgroundColor(rewardKind.getDefaultColor())
                                          .prompt(prompt.formPrompt())
                                          .enabled(false)
                                          .build();
    }
}

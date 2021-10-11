package perobobbot.pzplugin.rewards;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.twitch.client.api.TwitchService;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CustomRewardSynchronizer {

    public static @NonNull Mono<?> synchronize(@NonNull TwitchService twitchService,
                                               @NonNull Locale locale,
                                               @NonNull ImmutableMap<String, ParsedReward> onPlatformById) {
        return new CustomRewardSynchronizer(twitchService, locale, onPlatformById).synchronize();
    }


    public static @NonNull Function<ImmutableMap<String, ParsedReward>, Mono<?>> synchronizeWith(@NonNull TwitchService twitchService,
                                                                                                 @NonNull Locale locale) {
        return map -> synchronize(twitchService, locale, map);
    }

    private final @NonNull TwitchService twitchService;
    private final @NonNull Locale locale;
    private final @NonNull ImmutableMap<String, ParsedReward> onPlatformById;


    private Mono<?> synchronize() {

        final var expected = RewardLoader.load(locale)
                                         .stream()
                                         .collect(ImmutableMap.toImmutableMap(DefaultRewardInfo::id, Function.identity()));


        final var idOnPlatformToDelete = Sets.difference(onPlatformById.keySet(), expected.keySet());
        final var expectedIdToCreate = Sets.difference(expected.keySet(), onPlatformById.keySet());


//        displayRewards(expected,idOnPlatformToDelete,expectedIdToCreate);

        return Mono.when(Stream.concat(
                        idOnPlatformToDelete.stream()
                                            .map(onPlatformById::get)
                                            .map(ParsedReward::getId)
                                            .map(twitchService::deleteCustomRewards),
                        expectedIdToCreate.stream()
                                          .map(expected::get)
                                          .map(DefaultRewardInfo::formCreateCustomRewardParameter)
                                          .map(twitchService::createCustomReward)
                ).toList()
        );
    }

    private void displayRewards(ImmutableMap<String, DefaultRewardInfo> expected, Sets.SetView<String> idOnPlatformToDelete, Sets.SetView<String> expectedIdToCreate) {
        System.out.println("----- on platform -----");
        onPlatformById.forEach((id, c) -> System.out.printf("%15s - '%s'%n", id, c.getTitle()));

        System.out.println("----- expected -----");
        expected.forEach((id, c) -> System.out.printf("%15s - '%s'%n", id, c.getTitle()));

        System.out.println("----- to Delete ----");
        idOnPlatformToDelete.forEach(System.out::println);

        System.out.println("----- to Create ----");
        expectedIdToCreate.forEach(System.out::println);

    }

}

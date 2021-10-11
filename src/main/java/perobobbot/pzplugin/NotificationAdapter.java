package perobobbot.pzplugin;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.lang.Notification;
import perobobbot.lang.NotificationDispatcher;
import perobobbot.lang.NotificationListener;
import perobobbot.pzplugin.json.PZNotification;
import perobobbot.pzplugin.json.PZRewardUpdate;
import perobobbot.pzplugin.json.RewardDTO;
import perobobbot.pzplugin.rewards.ParsedRedemption;
import perobobbot.twitch.eventsub.api.event.ChannelPointsCustomRewardRedemptionAddEvent;
import perobobbot.twitch.eventsub.api.event.ChannelPointsCustomRewardUpdateEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class NotificationAdapter implements NotificationListener {

    private final @NonNull NotificationDispatcher notificationDispatcher;
    private final @NonNull RedemptionHandler redemptionHandler;

    private final long t = System.nanoTime();

    @Override
    public void onMessage(@NonNull Notification notification) {
        transformNotification(notification)
                .ifPresent(notificationDispatcher::sendNotification);
    }

    private @NonNull Optional<? extends PZNotification> transformNotification(@NonNull Notification notification) {
        if (notification instanceof ChannelPointsCustomRewardUpdateEvent rewardUpdateEvent) {
            return handleRewardUpdate(rewardUpdateEvent);
        } else if (notification instanceof ChannelPointsCustomRewardRedemptionAddEvent redemptionAddEvent) {
            return handleRedemptionAddEvent(redemptionAddEvent);
        } else {
            return Optional.empty();
        }
    }

    private @NonNull Optional<? extends PZNotification> handleRewardUpdate(@NonNull ChannelPointsCustomRewardUpdateEvent rewardUpdateEvent) {
        return RewardDTO.from(rewardUpdateEvent).map(dto -> new PZRewardUpdate(rewardUpdateEvent.getBroadcaster().asIdentityInfo(), dto));
    }

    private @NonNull Optional<? extends PZNotification> handleRedemptionAddEvent(@NonNull ChannelPointsCustomRewardRedemptionAddEvent redemptionAddEvent) {
        return ParsedRedemption.from(redemptionAddEvent).flatMap(redemptionHandler::handleRedemption);
    }




}

package perobobbot.pzplugin;


import jplugman.annotation.Extension;
import jplugman.api.Disposable;
import jplugman.api.ServiceProvider;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import perobobbot.lang.SubscriptionHolder;
import perobobbot.lang.WaitStrategy;
import perobobbot.plugin.EndPointPluginData;
import perobobbot.plugin.PerobobbotPlugin;
import perobobbot.pzplugin.redemption.DefaultRedemptionHandler;
import perobobbot.pzplugin.redemption.RedemptionCleaner;
import perobobbot.pzplugin.redemption.RedemptionMap;

@RequiredArgsConstructor
@Extension(point = PerobobbotPlugin.class, version = "1.0.0")
public class PZPlugin implements PerobobbotPlugin, Disposable {


    private final SubscriptionHolder subscriptionHolder = new SubscriptionHolder();

    @Getter
    private final EndPointPluginData data;

    private final RedemptionCleaner redemptionCleaner;

    public PZPlugin(@NonNull ServiceProvider serviceProvider) {
        final RedemptionMap redemptionMap = new RedemptionMap();
        final var notificationDispatcher = serviceProvider.getSingleService(Requirements.NOTIFICATION_DISPATCHER);
        final var notificationAdapter = new NotificationAdapter(notificationDispatcher,new DefaultRedemptionHandler(serviceProvider, redemptionMap));

        this.data = new EndPointPluginData(new PZHandleMappingFactory(serviceProvider, redemptionMap).create());
        this.subscriptionHolder.replaceWith(() -> notificationDispatcher.addListener(notificationAdapter));
        this.redemptionCleaner = new RedemptionCleaner(redemptionMap, WaitStrategy.create());

        this.redemptionCleaner.start();
    }


    @Override
    public void dispose() {
        subscriptionHolder.unsubscribe();
        redemptionCleaner.requestStop();
    }

    @Override
    public @NonNull String getName() {
        return "PZ Plugin";
    }


}

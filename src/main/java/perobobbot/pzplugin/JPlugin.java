package perobobbot.pzplugin;


import com.google.common.collect.ImmutableSet;
import jplugman.api.Plugin;
import jplugman.api.Requirement;
import jplugman.api.ServiceProvider;
import lombok.NonNull;
import perobobbot.plugin.PerobobbotPlugin;

/**
 * This is the entry point of the plugin (at the jplugman level).
 * <p>
 * For jplugman, the plugin provides a service which in the case
 * of the bot, is an ExtensionPlugin (which might be confusing, sorry).
 * <p>
 * An ExtensionPlugin is a plugin for the Bot to add an extension to itself.
 */
public class JPlugin implements Plugin {

    /**
     * @return the type of the ExtensionPlugin
     */
    @Override
    public @NonNull Class<?> getServiceClass() {
        return PerobobbotPlugin.class;
    }

    /**
     * @return the requirements of the extension
     */
    @Override
    public @NonNull ImmutableSet<Requirement<?>> getRequirements() {
        return ImmutableSet.of(
                Requirements.USER_SERVICE,
                Requirements.USER_AUTHENTICATOR,
                Requirements.O_AUTH_TOKEN_IDENTIFIER_SETTER,
                Requirements.NOTIFICATION_DISPATCHER,
                Requirements.OBJECT_MAPPER_FACTORY,
                Requirements.PLATFORM_USER_SERVICE,
                Requirements.SUBSCRIPTION_SERVICE,
                Requirements.BANK_SERVICE,
                Requirements.TWITCH_SERVICE);
    }

    @Override
    public @NonNull PZPlugin loadService(@NonNull ModuleLayer pluginLayer, @NonNull ServiceProvider serviceProvider) {
        return new PZPlugin(serviceProvider);
    }


}

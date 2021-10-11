

package perobobbot.pzplugin;

import jplugman.api.Requirement;
import perobobbot.chat.core.IO;
import perobobbot.command.CommandDeclarationLister;
import perobobbot.data.service.*;
import perobobbot.eventsub.UserEventSubManager;
import perobobbot.http.WebHookManager;
import perobobbot.lang.*;
import perobobbot.messaging.ChatController;
import perobobbot.oauth.OAuthTokenIdentifierSetter;
import perobobbot.security.com.OAuthAuthorizationCodeFlow;
import perobobbot.twitch.client.api.TwitchService;


public class Requirements {

    public static final Requirement<OAuthAuthorizationCodeFlow> O_AUTH_AUTHORIZATION_CODE_FLOW = new Requirement<>(OAuthAuthorizationCodeFlow.class, 1);
    public static final Requirement<SubscriptionService> SUBSCRIPTION_SERVICE = new Requirement<>(SubscriptionService.class, 1);
    public static final Requirement<UserService> USER_SERVICE = new Requirement<>(UserService.class, 1);
    public static final Requirement<BankService> BANK_SERVICE = new Requirement<>(BankService.class, 1);
    public static final Requirement<BotService> BOT_SERVICE = new Requirement<>(BotService.class, 1);
    public static final Requirement<OAuthService> O_AUTH_SERVICE = new Requirement<>(OAuthService.class, 1);
    public static final Requirement<ViewerIdentityService> VIEWER_IDENTITY_SERVICE = new Requirement<>(ViewerIdentityService.class, 1);
    public static final Requirement<Instants> INSTANTS = new Requirement<>(Instants.class, 1);
    public static final Requirement<NotificationDispatcher> NOTIFICATION_DISPATCHER = new Requirement<>(NotificationDispatcher.class, 1);
    public static final Requirement<UserAuthenticator> USER_AUTHENTICATOR = new Requirement<>(UserAuthenticator.class, 1);
    public static final Requirement<UserEventSubManager> USER_EVENT_SUB_MANAGER = new Requirement<>(UserEventSubManager.class, 1);
    public static final Requirement<OAuthTokenIdentifierSetter> O_AUTH_TOKEN_IDENTIFIER_SETTER = new Requirement<>(OAuthTokenIdentifierSetter.class, 1);
    public static final Requirement<CommandDeclarationLister> COMMAND_DECLARATION_LISTER = new Requirement<>(CommandDeclarationLister.class, 1);
    public static final Requirement<ObjectMapperFactory> OBJECT_MAPPER_FACTORY = new Requirement<>(ObjectMapperFactory.class, 1);
    public static final Requirement<WebHookManager> WEB_HOOK_MANAGER = new Requirement<>(WebHookManager.class, 1);
    public static final Requirement<IO> IO = new Requirement<>(IO.class, 1);
    public static final Requirement<StandardInputProvider> STANDARD_INPUT_PROVIDER = new Requirement<>(StandardInputProvider.class, 1);
    public static final Requirement<TwitchService> TWITCH_SERVICE = new Requirement<>(TwitchService.class, 1);
    public static final Requirement<ApplicationCloser> APPLICATION_CLOSER = new Requirement<>(ApplicationCloser.class, 1);
    public static final Requirement<MessageDispatcher> MESSAGE_DISPATCHER = new Requirement<>(MessageDispatcher.class, 1);
    public static final Requirement<ChatController> CHAT_CONTROLLER = new Requirement<>(ChatController.class, 1);

}

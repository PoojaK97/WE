package com.womandroid.we.chatSDK.core.session;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.womandroid.we.chatSDK.core.base.BaseNetworkAdapter;
import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.error.ChatSDKException;
import com.womandroid.we.chatSDK.core.events.EventType;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.handlers.AudioMessageHandler;
import com.womandroid.we.chatSDK.core.handlers.AuthenticationHandler;
import com.womandroid.we.chatSDK.core.handlers.BlockingHandler;
import com.womandroid.we.chatSDK.core.handlers.ContactHandler;
import com.womandroid.we.chatSDK.core.handlers.CoreHandler;
import com.womandroid.we.chatSDK.core.handlers.EncryptionHandler;
import com.womandroid.we.chatSDK.core.handlers.EventHandler;
import com.womandroid.we.chatSDK.core.handlers.FileMessageHandler;
import com.womandroid.we.chatSDK.core.handlers.HookHandler;
import com.womandroid.we.chatSDK.core.handlers.ImageMessageHandler;
import com.womandroid.we.chatSDK.core.handlers.LastOnlineHandler;
import com.womandroid.we.chatSDK.core.handlers.LocationMessageHandler;
import com.womandroid.we.chatSDK.core.handlers.ProfilePicturesHandler;
import com.womandroid.we.chatSDK.core.handlers.PublicThreadHandler;
import com.womandroid.we.chatSDK.core.handlers.PushHandler;
import com.womandroid.we.chatSDK.core.handlers.ReadReceiptHandler;
import com.womandroid.we.chatSDK.core.handlers.SearchHandler;
import com.womandroid.we.chatSDK.core.handlers.SocialLoginHandler;
import com.womandroid.we.chatSDK.core.handlers.StickerMessageHandler;
import com.womandroid.we.chatSDK.core.handlers.ThreadHandler;
import com.womandroid.we.chatSDK.core.handlers.TypingIndicatorHandler;
import com.womandroid.we.chatSDK.core.handlers.UploadHandler;
import com.womandroid.we.chatSDK.core.handlers.VideoMessageHandler;
import com.womandroid.we.chatSDK.core.interfaces.InterfaceAdapter;
import com.womandroid.we.chatSDK.core.interfaces.LocalNotificationHandler;
import com.womandroid.we.chatSDK.core.interfaces.ThreadType;
import com.womandroid.we.chatSDK.core.types.ReadStatus;
import com.womandroid.we.chatSDK.core.utils.AppBackgroundMonitor;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Created by ben on 9/5/17.
 */

public class ChatSDK {

    public static String Preferences = "chat_sdk_preferences";

    private static final ChatSDK instance = new ChatSDK();
    public WeakReference<Context> context;
    public Configuration config;
    public Disposable localNotificationDisposable;

    protected ChatSDK () {
    }

    private void setContext (Context context) {
        this.context = new WeakReference<>(context);
    }


    public static ChatSDK initialize (Configuration config) throws ChatSDKException {
        return initialize(config, null, null);
    }

    public static ChatSDK initialize (Configuration config, BaseNetworkAdapter networkAdapter, InterfaceAdapter interfaceAdapter) throws ChatSDKException {
        shared().setContext(config.context.get());
        shared().config = config;

        DaoCore.init(shared().context());

        if(interfaceAdapter != null) {
            InterfaceManager.shared().a = interfaceAdapter;
        }
        else {
            shared().activateModule("UserInterfaceModule", "activate", new MethodArgument(Context.class, shared().context()));
        }

        if (networkAdapter != null) {
            NetworkManager.shared().a = networkAdapter;
        }
        else {
            shared().activateModule("FirebaseModule", "activate");
        }

        shared().handleLocalNotifications();
        // Monitor the app so if it goes into the background we know
        AppBackgroundMonitor.shared().setEnabled(true);

        if (config().debug) {
            Timber.plant(new Timber.DebugTree());
        }
      
        return shared();
    }

    public void activateModule (String moduleName, String methodName, MethodArgument... arguments) throws ChatSDKException {
        try {
            ArrayList<Class<?>> classes = new ArrayList<>();
            ArrayList<Object> values = new ArrayList<>();

            for(MethodArgument a : arguments) {
                classes.add(a.type);
                values.add(a.value);
            }

            Class<?> interfaceModule = Class.forName(moduleName);
            Method method = interfaceModule.getMethod(methodName, classes.toArray(new Class<?>[0]));
            method.invoke(null, values.toArray(new Object[0]));
        }
        catch (ClassNotFoundException e) {
            throw new ChatSDKException("Module: " + moduleName + "Not found");
        }
        catch (NoSuchMethodException e) {
            throw new ChatSDKException("Activate method not found for module");
        }
        catch (IllegalAccessException e) {
            throw new ChatSDKException("Activate method not found for module");
        }
        catch (InvocationTargetException e) {
            throw new ChatSDKException("Activate method not found for module");
        }
    }

    public void handleLocalNotifications () {

        if (localNotificationDisposable != null) {
            localNotificationDisposable.dispose();
        }

        // TODO: Check this
        localNotificationDisposable = ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.MessageAdded))
                .subscribe(networkEvent -> {
                    Message message = networkEvent.message;
                    Thread thread = networkEvent.thread;
                    if(message != null && !AppBackgroundMonitor.shared().inBackground()) {
                        if (thread.typeIs(ThreadType.Private) || (thread.typeIs(ThreadType.Public) && ChatSDK.config().pushNotificationsForPublicChatRoomsEnabled)) {
                            if(!message.getSender().isMe() && ChatSDK.ui().showLocalNotifications(message.getThread())) {
                                ReadStatus status = message.readStatusForUser(ChatSDK.currentUser());
                                if (!message.isRead() && !status.is(ReadStatus.delivered())) {
                                    // Only show the alert if we'recyclerView not on the private threads tab
                                    ChatSDK.ui().notificationDisplayHandler().createMessageNotification(message);
                                }
                            }
                        }
                    }
                });
    }

    public void setLocalNotificationHandler (LocalNotificationHandler handler) {

    }

    public static ChatSDK shared () {
        return instance;
    }

    public SharedPreferences getPreferences () {
        return context.get().getSharedPreferences(Preferences, Context.MODE_PRIVATE);
    }

    public Context context () {
        return context.get();
    }

    public static Configuration config () {
        return shared().config;
    }

    public static void logError (Throwable t) {
        logError(new Exception(t));
    }

    public static void logError (Exception e) {
        if (config().debug) {
            e.printStackTrace();
        }
        if (config().crashHandler != null) {
            config().crashHandler.log(e);
        }
    }

    /**
     * Shortcut to return the interface adapter
     * @return InterfaceAdapter
     */
    public static InterfaceAdapter ui () {
        return InterfaceManager.shared().a;
    }

    public void setInterfaceAdapter (InterfaceAdapter interfaceAdapter) {
        InterfaceManager.shared().a = interfaceAdapter;
    }

    public static CoreHandler core () {
        return a().core;
    }

    public static AuthenticationHandler auth () {
        return a().auth;
    }

    public static ThreadHandler thread () {
        return a().thread;
    }

    public static PublicThreadHandler publicThread () {
        return a().publicThread;
    }

    public static PushHandler push () {
        return a().push;
    }

    public static UploadHandler upload () {
        return a().upload;
    }

    public static EventHandler events () {
        return a().events;
    }

    public static User currentUser () {
        return ChatSDK.core().currentUserModel();
    }

    public static String currentUserID() {
        return ChatSDK.core().currentUserModel().getEntityID();
    }

    public static SearchHandler search () {
        return a().search;
    }

    public static ContactHandler contact () {
        return a().contact;
    }

    public static BlockingHandler blocking () {
        return a().blocking;
    }

    public static EncryptionHandler encryption () { return a().encryption; }

    public static LastOnlineHandler lastOnline () {
        return a().lastOnline;
    }

    public static AudioMessageHandler audioMessage () {
        return a().audioMessage;
    }

    public static VideoMessageHandler videoMessage () {
        return a().videoMessage;
    }

    public static HookHandler hook () {
        return a().hook;
    }

    public static SocialLoginHandler socialLogin () {
        return a().socialLogin;
    }

    public static StickerMessageHandler stickerMessage () {
        return a().stickerMessage;
    }

    public static FileMessageHandler fileMessage () {
        return a().fileMessage;
    }

    public static ImageMessageHandler imageMessage () {
        return a().imageMessage;
    }

    public static LocationMessageHandler locationMessage () {
        return a().locationMessage;
    }

    public static ReadReceiptHandler readReceipts () {
        return a().readReceipts;
    }

    public static TypingIndicatorHandler typingIndicator () {
        return a().typingIndicator;
    }

    public static ProfilePicturesHandler profilePictures () {
        return a().profilePictures;
    }

    public static BaseNetworkAdapter a() {
        return NetworkManager.shared().a;
    }

    public static StorageManager db () {
        return StorageManager.shared();
    }

}

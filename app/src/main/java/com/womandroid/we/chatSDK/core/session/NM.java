package com.womandroid.we.chatSDK.core.session;

import com.womandroid.we.chatSDK.core.base.BaseNetworkAdapter;
import com.womandroid.we.chatSDK.core.dao.User;
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

/**
 * Created by benjaminsmiley-andrews on 25/05/2017.
 */

/* @deprecated Use ChatSDK.core() etc... instead */
@Deprecated

public class NM {

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

    public static SearchHandler search () {
        return a().search;
    }

    public static ContactHandler contact () {
        return a().contact;
    }

    public static BlockingHandler blocking () { return a().blocking; }

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

    public static BaseNetworkAdapter a() {
        return NetworkManager.shared().a;
    }

    public static InterfaceAdapter ui () {
        return ChatSDK.ui();
    }

}

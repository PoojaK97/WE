package com.womandroid.we.chatSDK.core.base;

import java.util.HashMap;

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
import com.womandroid.we.chatSDK.core.handlers.ModerationHandler;
import com.womandroid.we.chatSDK.core.handlers.NearbyUsersHandler;
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

/**
 * Created by benjaminsmiley-andrews on 02/05/2017.
 */

public class BaseNetworkAdapter {

    public CoreHandler core;
    public AuthenticationHandler auth;
    public PushHandler push;
    public UploadHandler upload;
    public ThreadHandler thread;
    public VideoMessageHandler videoMessage;
    public AudioMessageHandler audioMessage;
    public ImageMessageHandler imageMessage = new BaseImageMessageHandler();
    public LocationMessageHandler locationMessage = new BaseLocationMessageHandler();
    public ContactHandler contact = new BaseContactHandler();
    public TypingIndicatorHandler typingIndicator;
    public ModerationHandler moderation;
    public SearchHandler search;
    public PublicThreadHandler publicThread;
    public ProfilePicturesHandler profilePictures;
    public BlockingHandler blocking;
    public LastOnlineHandler lastOnline;
    public NearbyUsersHandler nearbyUsers;
    public ReadReceiptHandler readReceipts;
    public StickerMessageHandler stickerMessage;
    public FileMessageHandler fileMessage;
    public SocialLoginHandler socialLogin;
    public EventHandler events;
    public HookHandler hook = new BaseHookHandler();
    public EncryptionHandler encryption;

    private HashMap<String, Object> handlers = new HashMap<>();

    public void setHandler(Object handler, String name) {
        handlers.put(name, handler);
    }

    public Object getHandler (String name) {
        return handlers.get(name);
    }

}

package com.womandroid.we.chatSDK.firebase.push;

import com.womandroid.we.chatSDK.core.session.NetworkManager;
import com.womandroid.we.chatSDK.firebase.push.FirebasePushHandler;

/**
 * Created by ben on 9/1/17.
 */

public class FirebasePushModule  {

    public static void activate () {
        NetworkManager.shared().a.push = new FirebasePushHandler();
    }

}
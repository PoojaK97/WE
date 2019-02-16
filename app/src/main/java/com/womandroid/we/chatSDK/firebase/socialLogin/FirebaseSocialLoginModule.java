package com.womandroid.we.chatSDK.firebase.socialLogin;

import android.content.Context;

import com.womandroid.we.chatSDK.core.session.NetworkManager;

/**
 * Created by ben on 9/5/17.
 */

public class FirebaseSocialLoginModule {

    public static void activate (Context context) {
        NetworkManager.shared().a.socialLogin = new FirebaseSocialLoginHandler(context);
    }

}

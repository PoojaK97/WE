package com.womandroid.we.chatSDK.firebase;

import com.womandroid.we.chatSDK.core.session.NetworkManager;
import com.womandroid.we.chatSDK.firebase.FirebaseNetworkAdapter;

/**
 * Created by benjaminsmiley-andrews on 12/07/2017.
 */

public class FirebaseModule {
    public static void activate() {
        NetworkManager.shared().a = new FirebaseNetworkAdapter();
    }
}

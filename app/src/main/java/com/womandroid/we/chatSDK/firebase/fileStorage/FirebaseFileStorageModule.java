package com.womandroid.we.chatSDK.firebase.fileStorage;

import com.womandroid.we.chatSDK.core.session.NetworkManager;
import com.womandroid.we.chatSDK.firebase.fileStorage.FirebaseUploadHandler;

/**
 * Created by ben on 9/1/17.
 */

public class FirebaseFileStorageModule {

    public static void activate () {
        NetworkManager.shared().a.upload = new FirebaseUploadHandler();
    }

}

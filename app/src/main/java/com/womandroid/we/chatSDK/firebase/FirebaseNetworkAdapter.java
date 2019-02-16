package com.womandroid.we.chatSDK.firebase;

import com.womandroid.we.chatSDK.core.base.BaseNetworkAdapter;
import com.womandroid.we.chatSDK.firebase.FirebaseAuthenticationHandler;
import com.womandroid.we.chatSDK.firebase.FirebaseContactHandler;
import com.womandroid.we.chatSDK.firebase.FirebaseCoreHandler;
import com.womandroid.we.chatSDK.firebase.FirebaseEventHandler;
import com.womandroid.we.chatSDK.firebase.FirebasePublicThreadHandler;
import com.womandroid.we.chatSDK.firebase.FirebaseSearchHandler;
import com.womandroid.we.chatSDK.firebase.FirebaseThreadHandler;

/**
 * Created by benjaminsmiley-andrews on 03/05/2017.
 */

public class FirebaseNetworkAdapter extends BaseNetworkAdapter {

    public FirebaseNetworkAdapter () {
        core = new FirebaseCoreHandler();
        auth = new FirebaseAuthenticationHandler();
        thread = new FirebaseThreadHandler();
        publicThread = new FirebasePublicThreadHandler();
        search = new FirebaseSearchHandler();
        events = FirebaseEventHandler.shared();
        contact = new FirebaseContactHandler();
    }

}

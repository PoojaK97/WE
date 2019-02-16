package com.womandroid.we;

import android.app.Application;
import android.content.Context;

import co.chatsdk.core.error.ChatSDKException;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.session.Configuration;
import co.chatsdk.firebase.FirebaseNetworkAdapter;
import co.chatsdk.firebase.file_storage.FirebaseFileStorageModule;
import co.chatsdk.firebase.push.FirebasePushModule;
import co.chatsdk.ui.manager.BaseInterfaceAdapter;
//import androidx.multidex.MultiDexApplication;

public class ApplicationClass extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();

// Create a new configuration
        Configuration.Builder builder = new Configuration.Builder(context);

// Perform any configuration steps (optional)
//        builder.firebaseRootPath("prod");

// Initialize the Chat SDK
        try {
            ChatSDK.initialize(builder.build(), new FirebaseNetworkAdapter(), new BaseInterfaceAdapter(context));
        } catch (ChatSDKException e) {

        }

// File storage is needed for profile image upload and image messages
        FirebaseFileStorageModule.activate();
        FirebasePushModule.activate();
    }

}

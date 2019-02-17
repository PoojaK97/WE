package com.womandroid.we;

import android.app.Application;
import android.content.Context;

import com.firebase.geofire.GeoFire;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.womandroid.we.chatSDK.core.error.ChatSDKException;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.Configuration;
import com.womandroid.we.chatSDK.firebase.FirebaseNetworkAdapter;
import com.womandroid.we.chatSDK.firebase.fileStorage.FirebaseFileStorageModule;
import com.womandroid.we.chatSDK.firebase.push.FirebasePushModule;
import com.womandroid.we.chatSDK.ui.manager.BaseInterfaceAdapter;
//import androidx.multidex.MultiDexApplication;

public class ApplicationClass extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();

// Create a new configuration
        Configuration.Builder builder = new Configuration.Builder(context);
//        config.firebaseRootPath("19_01_test");
//        config.googleMaps("AIzaSyCwwtZrlY9Rl8paM0R6iDNBEit_iexQ1aE");
        builder.publicRoomCreationEnabled(true);
//        config.pushNotificationSound("default");
//
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

//        FirebaseUIModule.activate(context, EmailAuthProvider.PROVIDER_ID, PhoneAuthProvider.PROVIDER_ID);

//        ProfilePicturesModule.activate();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("path/to/geofire");
        GeoFire geoFire = new GeoFire(ref);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }

}

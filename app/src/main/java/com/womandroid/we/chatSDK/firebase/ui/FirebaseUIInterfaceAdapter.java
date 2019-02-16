package com.womandroid.we.chatSDK.firebase.ui;

import android.content.Context;

import com.womandroid.we.chatSDK.ui.manager.BaseInterfaceAdapter;

/**
 * Created by ben on 1/2/18.
 */

public class FirebaseUIInterfaceAdapter extends BaseInterfaceAdapter {

    public FirebaseUIInterfaceAdapter(Context context) {
        super(context);
    }

    @Override
    public Class getLoginActivity() {
        return FirebaseUIModule.shared().getSplashScreenActivity();
    }

}

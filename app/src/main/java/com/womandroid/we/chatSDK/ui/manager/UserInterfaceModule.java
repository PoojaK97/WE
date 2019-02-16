package com.womandroid.we.chatSDK.ui.manager;

import android.content.Context;

import com.womandroid.we.chatSDK.core.session.InterfaceManager;
import com.womandroid.we.chatSDK.ui.manager.BaseInterfaceAdapter;

/**
 * Created by ben on 10/27/17.
 */

public class UserInterfaceModule {

    public static void activate (Context context) {
        InterfaceManager.shared().a = new BaseInterfaceAdapter(context);
    }
}

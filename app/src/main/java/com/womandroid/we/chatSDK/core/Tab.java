package com.womandroid.we.chatSDK.core;


import androidx.fragment.app.Fragment;
import com.womandroid.we.chatSDK.core.session.ChatSDK;

/**
 * Created by benjaminsmiley-andrews on 12/07/2017.
 */

public class Tab {

    public Fragment fragment;
    public String title;
    public int icon;

    public Tab (int titleResource, int icon, Fragment fragment) {
        this.fragment = fragment;
        this.title = ChatSDK.shared().context().getString(titleResource);
        this.icon = icon;
    }

    public Tab (String title, int icon, Fragment fragment) {
        this.fragment = fragment;
        this.title = title;
        this.icon = icon;
    }

}

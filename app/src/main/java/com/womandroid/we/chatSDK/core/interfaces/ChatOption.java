package com.womandroid.we.chatSDK.core.interfaces;

import android.app.Activity;

import com.womandroid.we.chatSDK.core.types.ChatOptionType;
import com.womandroid.we.chatSDK.core.dao.Thread;

import io.reactivex.Observable;

/**
 * Created by ben on 10/11/17.
 */

public interface ChatOption {

    int getIconResourceId();
    String getTitle();
    Observable<?> execute(Activity activity, Thread thread);
    ChatOptionType getType();

}

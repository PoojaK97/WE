package com.womandroid.we.chatSDK.ui.chat.handlers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.interfaces.MessageDisplayHandler;

public abstract class AbstractMessageDisplayHandler implements MessageDisplayHandler {

    protected View row (boolean isReply, Activity activity) {
        View row;
        LayoutInflater inflater = LayoutInflater.from(activity);
        if(isReply) {
            row = inflater.inflate(R.layout.chat_sdk_row_message_reply , null);
        } else {
            row = inflater.inflate(R.layout.chat_sdk_row_message_me , null);
        }
        return row;
    }
}

package com.womandroid.we.chatSDK.core.interfaces;

import android.app.Activity;
import android.content.Context;

import com.womandroid.we.chatSDK.core.base.AbstractMessageViewHolder;
import com.womandroid.we.chatSDK.core.dao.Message;

/**
 * Created by ben on 10/11/17.
 */

public interface MessageDisplayHandler {

    void updateMessageCellView(Message message, AbstractMessageViewHolder viewHolder, Context context);
    String displayName(Message message);
    AbstractMessageViewHolder newViewHolder(boolean isReply, Activity activity);

}

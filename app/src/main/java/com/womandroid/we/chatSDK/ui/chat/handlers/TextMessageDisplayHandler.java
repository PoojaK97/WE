package com.womandroid.we.chatSDK.ui.chat.handlers;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.womandroid.we.chatSDK.core.base.AbstractMessageViewHolder;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.ui.chat.handlers.AbstractMessageDisplayHandler;
import com.womandroid.we.chatSDK.ui.chat.viewholder.TextMessageViewHolder;

public class TextMessageDisplayHandler extends AbstractMessageDisplayHandler {

    @Override
    public void updateMessageCellView(Message message, AbstractMessageViewHolder viewHolder, Context context) {

    }

    @Override
    public String displayName(Message message) {
        return message.getText();
    }

    @Override
    public AbstractMessageViewHolder newViewHolder(boolean isReply, Activity activity) {
        View row = row(isReply, activity);
        return new TextMessageViewHolder(row, activity);
    }
}

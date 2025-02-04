package com.womandroid.we.chatSDK.ui.chat;

import com.womandroid.we.chatSDK.core.dao.Message;

public class MessageListItem {

    public Message message;
    public float progress;

    public MessageListItem (Message message) {
        this.message = message;
    }

    public Message getMessage () {
        return message;
    }

    public long getTimeInMillis() {
        return message.getDate().toDate().getTime();
    }


}

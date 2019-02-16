package com.womandroid.we.chatSDK.core.types;

import com.womandroid.we.chatSDK.core.dao.Message;

/**
 * Created by ben on 9/29/17.
 */

public class MessageSendProgress {

    public Message message;
    public Progress uploadProgress;

    public MessageSendProgress (Message message) {
        this(message, null);
    }

    public MessageSendProgress (Message message, Progress progress) {
        this.message = message;
        this.uploadProgress = progress;
    }

    public MessageSendStatus getStatus () {
        return message.getMessageStatus();
    }

}

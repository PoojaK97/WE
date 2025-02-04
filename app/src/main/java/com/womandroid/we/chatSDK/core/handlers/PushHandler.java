package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.dao.Message;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface PushHandler {

    void subscribeToPushChannel(String channel);
    void unsubscribeToPushChannel(String channel);

    void pushForMessage(Message message);

}

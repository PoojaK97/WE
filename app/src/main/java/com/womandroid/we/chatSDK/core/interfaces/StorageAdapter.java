package com.womandroid.we.chatSDK.core.interfaces;


import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.User;

/**
 * Created by benjaminsmiley-andrews on 03/05/2017.
 */

public interface StorageAdapter {

    User fetchUserWithEntityID(String entityID);
    Thread fetchThreadWithEntityID(String entityID);
    Message fetchMessageWithEntityID(String entityID);

}

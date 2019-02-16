package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.dao.Thread;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface ReadReceiptHandler {

    void updateReadReceipts(Thread thread);
    void markRead(Thread thread);

}

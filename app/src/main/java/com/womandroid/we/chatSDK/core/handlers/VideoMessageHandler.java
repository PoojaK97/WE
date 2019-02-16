package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.interfaces.MessageDisplayHandler;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import com.womandroid.we.chatSDK.core.dao.Thread;
import io.reactivex.Observable;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface VideoMessageHandler extends MessageDisplayHandler {

    /**
     * Send a video message
     */
    Observable<MessageSendProgress> sendMessageWithVideo(final String videoPath, final Thread thread);

}

package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.audio.Recording;
import com.womandroid.we.chatSDK.core.interfaces.MessageDisplayHandler;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import com.womandroid.we.chatSDK.core.dao.Thread;

import io.reactivex.Observable;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface AudioMessageHandler extends MessageDisplayHandler {

    /**
     * Send an audio message
     */
    Observable<MessageSendProgress> sendMessage(final Recording recording, final Thread thread);

}

package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.interfaces.MessageDisplayHandler;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import io.reactivex.Observable;

/**
 * Created by Pepe Becker on 01/05/2018.
 */

public interface FileMessageHandler extends MessageDisplayHandler {
    Observable<MessageSendProgress> sendMessageWithFile(String name, String mimeType, byte[] data, final Thread thread);
}

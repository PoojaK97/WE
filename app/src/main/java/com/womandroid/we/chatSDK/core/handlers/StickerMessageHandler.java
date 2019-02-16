package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.interfaces.MessageDisplayHandler;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import io.reactivex.Observable;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface StickerMessageHandler extends MessageDisplayHandler {
    Observable<MessageSendProgress> sendMessageWithSticker(String stickerImageName, final Thread thread);
}

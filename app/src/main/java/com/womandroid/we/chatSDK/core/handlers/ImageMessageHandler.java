package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import com.womandroid.we.chatSDK.core.dao.Thread;
import io.reactivex.Observable;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface ImageMessageHandler {
    /**
     * Preparing an image message,
     * This is only the build part of the send from here the message will passed to "sendMessage" Method.
     * From there the message will be uploaded to the server if the upload fails the message will be deleted from the local db.
     * If the upload is successful we will update the message entity so the entityId given from the server will be saved.
     * When done or when an error occurred the calling method will be notified.
     *
     * @param filePath is a file that contain the image. For now the file will be decoded to a Base64 image representation.
     * @param thread   thread that the message is sent to.
     */
    Observable<MessageSendProgress> sendMessageWithImage(String filePath, Thread thread);
}

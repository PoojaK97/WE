package com.womandroid.we.chatSDK.core.base;

import android.graphics.Bitmap;

import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.handlers.UploadHandler;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.FileUploadResult;
import com.womandroid.we.chatSDK.core.utils.ImageUtils;
import io.reactivex.Observable;

/**
 * Created by benjaminsmiley-andrews on 24/05/2017.
 */

public abstract class AbstractUploadHandler implements UploadHandler {

    public Observable<FileUploadResult> uploadImage(final Bitmap image) {
        return ChatSDK.upload().uploadFile(ImageUtils.getImageByteArray(image), "image.jpg", "image/jpeg");
    }

    public String getUUID() {
        return DaoCore.generateRandomName();
    }

}

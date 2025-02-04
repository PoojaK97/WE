package com.womandroid.we.chatSDK.core.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.handlers.ImageMessageHandler;
import com.womandroid.we.chatSDK.core.rx.ObservableConnector;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.FileUploadResult;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import com.womandroid.we.chatSDK.core.types.MessageSendStatus;
import com.womandroid.we.chatSDK.core.types.MessageType;
import com.womandroid.we.chatSDK.core.utils.StringChecker;
import id.zelory.compressor.Compressor;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by ben on 10/24/17.
 */

public class BaseImageMessageHandler implements ImageMessageHandler {
        @Override
        public Observable<MessageSendProgress> sendMessageWithImage(final String filePath, final Thread thread) {
            return Observable.create((ObservableOnSubscribe<MessageSendProgress>) e -> {

                final Message message = AbstractThreadHandler.newMessage(MessageType.Image, thread);

                // First pass back an empty result so that we add the cell to the table view
                message.setMessageStatus(MessageSendStatus.Uploading);
                message.update();
                e.onNext(new MessageSendProgress(message));

                File compress = new Compressor(ChatSDK.shared().context())
                        .setMaxHeight(ChatSDK.config().imageMaxHeight)
                        .setMaxWidth(ChatSDK.config().imageMaxWidth)
                        .compressToFile(new File(filePath));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                final Bitmap image = BitmapFactory.decodeFile(compress.getPath(), options);

                if(image == null) {
                    e.onError(new Throwable(ChatSDK.shared().context().getString(R.string.unable_to_save_image_to_disk)));
                    return;
                }

                ChatSDK.upload().uploadImage(image).subscribe(new Observer<FileUploadResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(FileUploadResult result) {
                        if(!StringChecker.isNullOrEmpty(result.url))  {

                            message.setValueForKey(image.getWidth(), Keys.MessageImageWidth);
                            message.setValueForKey(image.getHeight(), Keys.MessageImageHeight);
                            message.setValueForKey(result.url, Keys.MessageImageURL);
                            message.setValueForKey(result.url, Keys.MessageThumbnailURL);

                            message.update();

                            Timber.v("ProgressListener: " + result.progress.asFraction());

                        }

                        e.onNext(new MessageSendProgress(message, result.progress));

                    }

                    @Override
                    public void onError(Throwable ex) {
                        e.onError(ex);
                    }

                    @Override
                    public void onComplete() {

                        message.setMessageStatus(MessageSendStatus.Sending);
                        message.update();

                        e.onNext(new MessageSendProgress(message));

                        ObservableConnector<MessageSendProgress> connector = new ObservableConnector<>();
                        connector.connect(ChatSDK.thread().sendMessage(message), e);

                    }
                });
            }).subscribeOn(Schedulers.single());

        }
}

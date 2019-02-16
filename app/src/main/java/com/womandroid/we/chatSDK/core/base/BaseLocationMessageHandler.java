package com.womandroid.we.chatSDK.core.base;

import com.google.android.gms.maps.model.LatLng;

import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.handlers.LocationMessageHandler;
import com.womandroid.we.chatSDK.core.rx.ObservableConnector;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.MessageSendProgress;
import com.womandroid.we.chatSDK.core.types.MessageType;
import com.womandroid.we.chatSDK.core.utils.GoogleUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ben on 10/24/17.
 */

public class BaseLocationMessageHandler implements LocationMessageHandler {

    public Observable<MessageSendProgress> sendMessageWithLocation(final String filePath, final LatLng location, final Thread thread) {
        return Observable.create((ObservableOnSubscribe<MessageSendProgress>) e -> {
            final Message message = AbstractThreadHandler.newMessage(MessageType.Location, thread);

            int maxSize = ChatSDK.config().imageMaxThumbnailDimension;
            String imageURL = GoogleUtils.getMapImageURL(location, maxSize, maxSize);

            // Add the LatLng data to the message and the image url and thumbnail url
            // TODO: Deprecated
//            message.setText(String.valueOf(location.latitude)
//                    + Defines.DIVIDER
//                    + String.valueOf(location.longitude)
//                    + Defines.DIVIDER + imageURL
//                    + Defines.DIVIDER + imageURL
//                    + Defines.DIVIDER + ImageUtils.getDimensionAsString(maxSize, maxSize));

            message.setValueForKey(location.longitude, Keys.MessageLongitude);
            message.setValueForKey(location.latitude, Keys.MessageLatitude);
            message.setValueForKey(maxSize, Keys.MessageImageWidth);
            message.setValueForKey(maxSize, Keys.MessageImageHeight);
            message.setValueForKey(imageURL, Keys.MessageImageURL);
            message.setValueForKey(imageURL, Keys.MessageThumbnailURL);

            e.onNext(new MessageSendProgress(message));

            ObservableConnector<MessageSendProgress> connector = new ObservableConnector<>();
            connector.connect(ChatSDK.thread().sendMessage(message), e);

        }).subscribeOn(Schedulers.single());
    }

}

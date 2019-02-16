package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by benjaminsmiley-andrews on 16/05/2017.
 */

public interface EventHandler {

    PublishSubject<NetworkEvent> source();
    Observable<NetworkEvent> sourceOnMain();

}

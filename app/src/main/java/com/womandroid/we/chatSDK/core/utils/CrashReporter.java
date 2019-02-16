package com.womandroid.we.chatSDK.core.utils;

import java.lang.ref.WeakReference;

import com.womandroid.we.chatSDK.core.session.ChatSDK;
import io.reactivex.disposables.Disposable;

/**
 * Created by ben on 4/26/18.
 */

public class CrashReporter {

    private WeakReference<DisposableList> list;

    public CrashReporter (DisposableList list) {
        this.list = new WeakReference<DisposableList>(list);
    }

    public CrashReporter () {}

    public void onSubscribe(Disposable d) {
        if (list != null) {
            list.get().add(d);
        }
    }

    public void onNext(Object o) {

    }

    public void onError(Throwable e) {
        ChatSDK.logError(e);
    }

    public void onComplete() {

    }

}

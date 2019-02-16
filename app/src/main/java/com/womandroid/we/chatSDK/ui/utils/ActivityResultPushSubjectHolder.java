package com.womandroid.we.chatSDK.ui.utils;

import com.womandroid.we.chatSDK.core.utils.ActivityResult;
import io.reactivex.subjects.PublishSubject;

/**
 * We have to use this because in some versions of Android when the camera
 * displays the chat activity is destroyed so we lose the publish subject
 */
public class ActivityResultPushSubjectHolder {

    static PublishSubject<ActivityResult> instance = PublishSubject.create();

    public static PublishSubject<ActivityResult> shared () {
        return instance;
    }


}

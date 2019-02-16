package com.womandroid.we.chatSDK.core.interfaces;

import com.womandroid.we.chatSDK.core.dao.Thread;

public interface LocalNotificationHandler {
    boolean showLocalNotification(Thread thread);
}

package com.womandroid.we.chatSDK.firebase.push;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.legacy.content.WakefulBroadcastReceiver;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.InterfaceManager;
import com.womandroid.we.chatSDK.core.utils.AppBackgroundMonitor;

/**
 * Created by ben on 5/10/18.
 */

// We want to use this receiver if the app has been killed or if it's in the background
public class DefaultBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        if(!ChatSDK.config().inboundPushHandlingEnabled) {
            return;
        }

        final String threadEntityID = extras.getString(InterfaceManager.THREAD_ENTITY_ID);
        final String userEntityID = extras.getString(InterfaceManager.USER_ENTITY_ID);
        final String title = extras.getString(InterfaceManager.PUSH_TITLE);
        final String body = extras.getString(InterfaceManager.PUSH_BODY);

        // Only show the notification if the user is offline
        // This will be the case if the app
        // If the app is in the background
        Intent appIntent = null;
        if (ChatSDK.auth() == null || !ChatSDK.auth().userAuthenticatedThisSession() || ChatSDK.config().backgroundPushTestModeEnabled) {
            appIntent = new Intent(context, ChatSDK.ui().getLoginActivity());
        } else if (AppBackgroundMonitor.shared().inBackground() && ChatSDK.auth().userAuthenticatedThisSession()) {
            appIntent = new Intent(context, ChatSDK.ui().getChatActivity());
        }
        if (appIntent != null) {
            appIntent.putExtra(InterfaceManager.THREAD_ENTITY_ID, threadEntityID);
            appIntent.setAction(threadEntityID);
//            appIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ChatSDK.ui().notificationDisplayHandler().createMessageNotification(context, appIntent, userEntityID, title, body);
        }

    }

}

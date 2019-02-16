package com.womandroid.we.chatSDK.firebase.push;

import java.util.ArrayList;

import com.womandroid.we.chatSDK.firebase.push.InstanceIdService;

/**
 * Created by ben on 9/13/17.
 */

public class TokenChangeConnector {

    private static final com.womandroid.we.chatSDK.firebase.push.TokenChangeConnector instance = new com.womandroid.we.chatSDK.firebase.push.TokenChangeConnector();

    private ArrayList<InstanceIdService.TokenChangeListener> listeners = new ArrayList<>();

    public static com.womandroid.we.chatSDK.firebase.push.TokenChangeConnector shared () {
        return instance;
    }

    public void addListener (InstanceIdService.TokenChangeListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener (InstanceIdService.TokenChangeListener listener) {
        if(listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public void updated (String token) {
        for(InstanceIdService.TokenChangeListener l : listeners) {
            l.updated(token);
        }
    }

}

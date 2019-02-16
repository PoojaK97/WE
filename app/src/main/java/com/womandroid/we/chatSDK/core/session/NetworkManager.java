package com.womandroid.we.chatSDK.core.session;

import com.womandroid.we.chatSDK.core.base.BaseNetworkAdapter;

/**
 * Created by benjaminsmiley-andrews on 28/04/2017.
 */

public class NetworkManager {

    private final static NetworkManager instance = new NetworkManager();
    public BaseNetworkAdapter a;

    protected NetworkManager() {}

    public static NetworkManager shared() {
        return instance;
    }

}

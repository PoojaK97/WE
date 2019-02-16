package com.womandroid.we.chatSDK.profile;

import com.womandroid.we.chatSDK.core.session.NetworkManager;

/**
 * Created by Pepe on 01/12/19.
 */

public class ProfilePicturesModule {

    public static void activate () {
        NetworkManager.shared().a.profilePictures = new BaseProfilePicturesHandler();
    }

}

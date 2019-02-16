package com.womandroid.we.chatSDK.core.handlers;

import android.app.Activity;
import android.content.Intent;

import com.womandroid.we.chatSDK.core.types.AccountDetails;
import io.reactivex.Completable;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface SocialLoginHandler {

    Completable loginWithFacebook(Activity activity);
    Completable loginWithTwitter(Activity activity);
    Completable loginWithGoogle(Activity activity);

    // This should be called by the activity
    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean accountTypeEnabled(AccountDetails.Type type);
    void logout();

}

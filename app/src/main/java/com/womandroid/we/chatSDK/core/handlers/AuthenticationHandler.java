package com.womandroid.we.chatSDK.core.handlers;

import java.util.Map;

import com.womandroid.we.chatSDK.core.types.AccountDetails;
import io.reactivex.Completable;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface AuthenticationHandler {

    /**
     * Check to see if the user is already authenticated
     */
    Completable authenticateWithCachedToken();

    /**
    * Authenticate with Firebase
    */
    Completable authenticate(AccountDetails details);

    /**
    * Checks whether the user is authenticated
    */
    Boolean userAuthenticated();

    /**
     * Has been authenticated this session
     */
    Boolean userAuthenticatedThisSession();

    /**
    * DidLogout the user from the current account
    */
    Completable logout();

    /**
    * Says which networks are available this can be setup in bFirebaseDefines
    * if you set the API key to @"" for Twitter Facebook or Google then it's automatically
    * disabled
    */
    Boolean accountTypeEnabled(AccountDetails.Type type);

    /**
    * Get the user's stored login credentials
    */
    Map<String, ?> getLoginInfo();

    /**
    * Set the user's stored login credentials
    */
    void setLoginInfo(Map<String, Object> info);

    /**
    * Get the current user's authentication id
    */
    String getCurrentUserEntityID();

    // TODO: Implement something like this
    /**
    * The view controller that should be displayed when the user isn't logged in
    */

    void addLoginInfoData(String key, Object value);

    Completable changePassword(String email, String oldPassword, final String newPassword);
    Completable sendPasswordResetMail(String email);


}

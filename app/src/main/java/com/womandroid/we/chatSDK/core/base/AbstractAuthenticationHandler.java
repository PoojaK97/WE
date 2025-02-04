package com.womandroid.we.chatSDK.core.base;

import android.content.SharedPreferences;

import java.util.Map;

import com.womandroid.we.chatSDK.core.enums.AuthStatus;
import com.womandroid.we.chatSDK.core.handlers.AuthenticationHandler;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.AuthKeys;

/**
 * Created by benjaminsmiley-andrews on 03/05/2017.
 */

public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {

    public static String provider = "";
    protected boolean authenticatedThisSession = false;

    private AuthStatus authStatus = AuthStatus.IDLE;

    public AuthStatus getAuthStatus () {
        return authStatus;
    }

    public void setAuthStatus (AuthStatus status) {
        authStatus = status;
    }

    public boolean isAuthenticating () {
        return authStatus != AuthStatus.IDLE;
    }

    protected void setAuthStateToIdle() {
        authStatus = AuthStatus.IDLE;
    }

    public Boolean userAuthenticatedThisSession () {
        return userAuthenticated() && authenticatedThisSession;
    }

    /**
     * Currently supporting only string and integers. Long and other values can be added later on.
     */
    public void setLoginInfo(Map<String, Object> values) {

        SharedPreferences.Editor keyValuesEditor = ChatSDK.shared().getPreferences().edit();

        for (String s : values.keySet()) {
            if (values.get(s) instanceof Integer)
                keyValuesEditor.putInt(s, (Integer) values.get(s));
            else if (values.get(s) instanceof String)
                keyValuesEditor.putString(s, (String) values.get(s));
            else if (values.get(s) instanceof Boolean)
                keyValuesEditor.putBoolean(s, (Boolean) values.get(s));
        }

        keyValuesEditor.apply();
    }

    public void addLoginInfoData (String key, Object value) {
        SharedPreferences.Editor keyValuesEditor = ChatSDK.shared().getPreferences().edit();
        if (value instanceof Integer) {
            keyValuesEditor.putInt(key, (Integer) value);
        }
        else if (value instanceof String) {
            keyValuesEditor.putString(key, (String) value);
        }

        keyValuesEditor.apply();
    }

    public void removeLoginInfo (String key) {
        SharedPreferences.Editor keyValuesEditor = ChatSDK.shared().getPreferences().edit();
        keyValuesEditor.remove(key);
        keyValuesEditor.apply();
    }

    /**
     * @return the save auth id saved in the preference manager.
     * The preference manager is initialized when the NetworkManager.Init(context) is called.
     */
    public String getCurrentUserEntityID() {
        return (String) getLoginInfo().get(AuthKeys.CurrentUserID);
    }

    public Map<String, ?> getLoginInfo() {
        return ChatSDK.shared().getPreferences().getAll();
    }



}

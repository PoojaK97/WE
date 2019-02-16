package com.womandroid.we.chatSDK.ui;

import android.content.Intent;

import java.lang.ref.WeakReference;

import com.womandroid.we.chatSDK.core.interfaces.ChatOption;
import com.womandroid.we.chatSDK.core.interfaces.ChatOptionsDelegate;
import com.womandroid.we.chatSDK.core.interfaces.ChatOptionsHandler;
import com.womandroid.we.chatSDK.core.session.ChatSDK;

/**
 * Created by ben on 10/11/17.
 */

public abstract class AbstractChatOptionsHandler implements ChatOptionsHandler {

    protected WeakReference<ChatOptionsDelegate> delegate;

    public AbstractChatOptionsHandler (ChatOptionsDelegate delegate) {
        this.delegate = new WeakReference<>(delegate);
    }

    public void executeOption (ChatOption option) {
        if(delegate != null) {
            delegate.get().executeChatOption(option);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        for(ChatOption option : ChatSDK.ui().getChatOptions()) {

        }
    }

}

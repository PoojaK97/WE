package com.womandroid.we.chatSDK.ui.chat;

import com.womandroid.we.chatSDK.core.audio.Recording;

/**
 * Created by ben on 10/11/17.
 */

public interface TextInputDelegate {

    void showOptions();
    void hideOptions();
    void onSendPressed(String text);
    void startTyping();
    void sendAudio(Recording recording);
    void stopTyping();
    void onKeyboardShow();
    void onKeyboardHide();

}

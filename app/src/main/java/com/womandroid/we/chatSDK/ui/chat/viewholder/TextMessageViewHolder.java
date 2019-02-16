package com.womandroid.we.chatSDK.ui.chat.viewholder;

import android.app.Activity;
import android.view.View;

import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.ui.chat.BaseMessageViewHolder;

public class TextMessageViewHolder extends BaseMessageViewHolder {

    public TextMessageViewHolder(View itemView, Activity activity) {
        super(itemView, activity);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);

        messageTextView.setText(message.getText() == null ? "" : message.getText());
        setBubbleHidden(false);
        setTextHidden(false);

//        messageTextView.setText("HelloHelloHelloHel34523_loHelloHelloHelloHelloHelloHelloHelloHelloHello.png");
//        setIconHidden(false);

    }
}
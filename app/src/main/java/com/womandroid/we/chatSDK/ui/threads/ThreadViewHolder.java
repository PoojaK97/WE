package com.womandroid.we.chatSDK.ui.threads;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.womandroid.we.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by benjaminsmiley-andrews on 07/06/2017.
 */

public class ThreadViewHolder extends RecyclerView.ViewHolder {

    public TextView nameTextView;
    public TextView dateTextView;
    public TextView lastMessageTextView;
    public TextView unreadMessageCountTextView;
    public SimpleDraweeView imageView;
    public View indicator;

    public ThreadViewHolder(View itemView) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.chat_sdk_txt);
        lastMessageTextView = itemView.findViewById(R.id.txt_last_message);
        dateTextView = itemView.findViewById(R.id.txt_last_message_date);
        imageView = itemView.findViewById(R.id.img_thread_image);
        unreadMessageCountTextView = itemView.findViewById(R.id.txt_unread_messages);
        indicator = itemView.findViewById(R.id.chat_sdk_indicator);

    }

    public void showUnreadIndicator(){
        indicator.setVisibility(View.VISIBLE);
    }

    public void hideUnreadIndicator(){
        indicator.setVisibility(View.GONE);
    }




}
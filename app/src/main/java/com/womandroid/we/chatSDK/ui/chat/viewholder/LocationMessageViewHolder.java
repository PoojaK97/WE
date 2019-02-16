package com.womandroid.we.chatSDK.ui.chat.viewholder;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.utils.GoogleUtils;
import com.womandroid.we.chatSDK.ui.chat.BaseMessageViewHolder;
import com.womandroid.we.chatSDK.ui.chat.LocationMessageOnClickHandler;

public class LocationMessageViewHolder extends BaseMessageViewHolder {
    public LocationMessageViewHolder(View itemView, Activity activity) {
        super(itemView, activity);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);

        setImageHidden(false);

        int viewWidth = maxWidth();
        int viewHeight = maxHeight();

        LatLng latLng = getLatLng();
        messageImageView.setImageURI(GoogleUtils.getMapImageURL(latLng, viewWidth, viewHeight));

    }

    @Override
    public void onClick (View v) {
        super.onClick(v);
        if (message != null) {
            LocationMessageOnClickHandler.onClick(activity, getLatLng());
        }
    }

    public LatLng getLatLng() {
        double longitude = message.doubleForKey(Keys.MessageLongitude);
        double latitude = message.doubleForKey(Keys.MessageLatitude);
        return new LatLng(latitude, longitude);
    }

}

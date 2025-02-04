package com.womandroid.we.chatSDK.ui.chat.viewholder;

import android.app.Activity;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.ui.chat.BaseMessageViewHolder;
import com.womandroid.we.chatSDK.ui.chat.ImageMessageOnClickHandler;

public class ImageMessageViewHolder extends BaseMessageViewHolder {
    public ImageMessageViewHolder(View itemView, Activity activity) {
        super(itemView, activity);
    }

    @Override
    public void setMessage(Message message) {
        super.setMessage(message);

        setImageHidden(false);

        int viewWidth = maxWidth();
        int viewHeight = maxHeight();

        String url = getImageURL();

        if (url != null && url.length() > 0) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                    .setResizeOptions(new ResizeOptions(viewWidth, viewHeight))
                    .build();

            messageImageView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setOldController(messageImageView.getController())
                            .setImageRequest(request)
                            .build());
        } else {
            // Loads the placeholder
            messageImageView.setActualImageResource(R.drawable.icn_200_image_message_loading);
        }
    }

    @Override
    public void onClick (View v) {
        super.onClick(v);
        if (message != null) {
            ImageMessageOnClickHandler.onClick(activity, v, getImageURL());
        }
    }

    public String getImageURL () {
        return message.stringForKey(Keys.MessageImageURL);
    }
}

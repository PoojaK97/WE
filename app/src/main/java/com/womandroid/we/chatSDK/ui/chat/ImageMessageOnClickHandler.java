package com.womandroid.we.chatSDK.ui.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.StringUtils;

import com.womandroid.we.chatSDK.core.utils.ImageBuilder;
import com.womandroid.we.chatSDK.core.utils.PermissionRequestHandler;
import com.womandroid.we.chatSDK.ui.main.BaseActivity;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;


/**
 * Created by benjaminsmiley-andrews on 20/06/2017.
 */

public class ImageMessageOnClickHandler {

    public static void onClick (Activity activity, View view, String url) {
        BaseActivity.hideSoftKeyboard(activity);

        if (StringUtils.isNotBlank(url)) {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(com.womandroid.we.R.layout.chat_sdk_popup_touch_image, null);

            final PopupWindow imagePopup = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

            imagePopup.setContentView(popupView);
            imagePopup.setBackgroundDrawable(new BitmapDrawable());
            imagePopup.setOutsideTouchable(true);
            imagePopup.setAnimationStyle(com.womandroid.we.R.style.ImagePopupAnimation);

            final PhotoView imageView = popupView.findViewById(com.womandroid.we.R.id.photo_view);
            final ProgressBar progressBar = popupView.findViewById(com.womandroid.we.R.id.chat_sdk_popup_image_progressbar);
            final FloatingActionButton saveButton = popupView.findViewById(com.womandroid.we.R.id.floating_button);

            saveButton.hide();

            progressBar.setVisibility(View.VISIBLE);

            Disposable d = ImageBuilder.bitmapForURL(activity, url)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> progressBar.setVisibility(View.INVISIBLE))
                    .subscribe(bitmap -> {
                        imageView.setImageBitmap(bitmap);
                        saveButton.show();
                        saveButton.setOnClickListener(v1 -> PermissionRequestHandler.shared().requestWriteExternalStorage(activity).subscribe(() -> {
                            if (bitmap != null) {
                                String bitmapURL = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "" , "");
                                if (bitmapURL != null) {
                                    ToastHelper.show(activity, activity.getString(com.womandroid.we.R.string.image_saved));
                                }
                                else {
                                    ToastHelper.show(activity, activity.getString(com.womandroid.we.R.string.image_save_failed));
                                }
                            }
                        }, throwable -> ToastHelper.show(activity, throwable.getLocalizedMessage())));


                    }, throwable -> {
                        ToastHelper.show(activity, com.womandroid.we.R.string.unable_to_fetch_image);
                        imagePopup.dismiss();
                    });

            imagePopup.showAtLocation(view, Gravity.CENTER, 0, 0);

        }
    }


}

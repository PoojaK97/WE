package com.womandroid.we.chatSDK.ui.helpers;

import android.content.Intent;
import android.view.View;

import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created by benjaminsmiley-andrews on 05/07/2017.
 */

@Deprecated
public class ProfilePictureChooserOnClickListener implements View.OnClickListener {

    public static final int PROFILE_PIC = 100;

    WeakReference<AppCompatActivity> activity;
    WeakReference<Fragment> fragment;

    public ProfilePictureChooserOnClickListener(AppCompatActivity activity) {
        this(activity, null);
    }

    public ProfilePictureChooserOnClickListener(AppCompatActivity activity, Fragment fragment) {
        this.activity = new WeakReference<>(activity);
        this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        if(fragment.get() != null) {
            activity.get().startActivityFromFragment(fragment.get(), Intent.createChooser(intent,
                    "Complete action using"), PROFILE_PIC);
        }
        else {
            activity.get().startActivityForResult(Intent.createChooser(intent,
                    "Complete action using"), PROFILE_PIC);
        }

    }

}

/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.ui.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.ui.main.BaseActivity;

/**
 * Created by itzik on 6/17/2014.
 */
public abstract class BaseFragment extends DialogFragment {

    protected ProgressDialog progressDialog;

    protected View mainView;
    protected boolean tabIsVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setupTouchUIToDismissKeyboard(View view, final Integer... exceptIDs) {
        BaseActivity.setupTouchUIToDismissKeyboard(view, (v, event) -> {
            BaseActivity.hideSoftKeyboard(getActivity());
            return false;
        }, exceptIDs);
    }

    protected void showOrUpdateProgressDialog(String message) {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(getActivity());

        if (!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(message);
            progressDialog.show();
        }
        else {
            progressDialog.setMessage(message);
        }
    }

    public void setTabVisibility (boolean isVisible) {
        tabIsVisible = isVisible;
    }

    protected void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            ChatSDK.logError(e);
            // For handling orientation changed.
        }
    }

    abstract public void clearData ();
    public void safeReloadData () {
        if(getView() != null && ChatSDK.auth().userAuthenticated()) {
            reloadData();
        }
    }
    public abstract void reloadData();

}



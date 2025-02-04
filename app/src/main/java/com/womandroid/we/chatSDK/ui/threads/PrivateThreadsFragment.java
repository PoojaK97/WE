/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.ui.threads;

import android.view.MenuItem;

import java.util.List;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.interfaces.ThreadType;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.ui.helpers.DialogUtils;
import com.womandroid.we.chatSDK.ui.threads.ThreadsFragment;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import com.womandroid.we.chatSDK.core.dao.Thread;

/**
 * Created by itzik on 6/17/2014.
 */
public class PrivateThreadsFragment extends ThreadsFragment {

    @Override
    public void initViews() {
        super.initViews();

        Disposable d = adapter.onLongClickObservable().subscribe(thread -> DialogUtils.showToastDialog(getContext(), "", getResources().getString(R.string.alert_delete_thread), getResources().getString(R.string.delete),
                getResources().getString(R.string.cancel), null, () -> {
                    ChatSDK.thread().deleteThread(thread)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                }

                                @Override
                                public void onComplete() {
                                    adapter.clearData();
                                    reloadData();
                                    ToastHelper.show(getContext(), getString(R.string.delete_thread_success_toast));
                                }

                                @Override
                                public void onError(Throwable e) {
                                    ToastHelper.show(getContext(), getString(R.string.delete_thread_fail_toast));
                                }
                            });
                    return null;
                }));
    }

    @Override
    protected Predicate<NetworkEvent> mainEventFilter() {
        return NetworkEvent.filterPrivateThreadsUpdated();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        /* Cant use switch in the library*/
        int id = item.getItemId();

        if (id == R.id.action_chat_sdk_add) {
            ChatSDK.ui().startSelectContactsActivity(getContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected List<Thread> getThreads() {
        return ChatSDK.thread().getThreads(ThreadType.Private);
    }

}

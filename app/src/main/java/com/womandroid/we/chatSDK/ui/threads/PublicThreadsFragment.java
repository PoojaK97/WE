/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.ui.threads;

import android.app.AlertDialog;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.interfaces.ThreadType;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

/**
 * Created by itzik on 6/17/2014.
 */
public class PublicThreadsFragment extends ThreadsFragment {


    @Override
    public Predicate<NetworkEvent> mainEventFilter() {
        return NetworkEvent.filterPublicThreadsUpdated();
    }

    @Override
    public boolean allowThreadCreation () {
        return ChatSDK.config().publicRoomCreationEnabled;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        /* Cant use switch in the library*/
        int id = item.getItemId();

        if (id == com.womandroid.we.R.id.action_chat_sdk_add)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
            builder.setTitle(getString(com.womandroid.we.R.string.add_public_chat_dialog_title));

            // Set up the input
            final EditText input = new EditText(this.getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton(getString(com.womandroid.we.R.string.create), (dialog, which) -> {

                showOrUpdateProgressDialog(getString(com.womandroid.we.R.string.add_public_chat_dialog_progress_message));
                final String threadName = input.getText().toString();

                ChatSDK.publicThread().createPublicThreadWithName(threadName)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((thread, throwable) -> {
                            if (throwable == null) {
                                dismissProgressDialog();
                                adapter.addRow(thread);

                                ToastHelper.show(getContext(), String.format(getString(com.womandroid.we.R.string.public_thread__is_created), threadName));

                                ChatSDK.ui().startChatActivityForID(getContext(), thread.getEntityID());
                            }
                            else {
                                ChatSDK.logError(throwable);
                                Toast.makeText(com.womandroid.we.chatSDK.ui.threads.PublicThreadsFragment.this.getContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                dismissProgressDialog();                            }
                        });

            });
            builder.setNegativeButton(com.womandroid.we.R.string.cancel, (dialog, which) -> dialog.cancel());

            builder.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected List<Thread> getThreads() {
        return ChatSDK.thread().getThreads(ThreadType.Public);
    }
}

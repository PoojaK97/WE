/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.ui.threads;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.interfaces.ThreadType;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.utils.Strings;
import com.womandroid.we.chatSDK.ui.threads.ThreadImageBuilder;
import com.womandroid.we.chatSDK.ui.threads.ThreadSorter;
import com.womandroid.we.chatSDK.ui.threads.ThreadViewHolder;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ThreadsListAdapter extends RecyclerView.Adapter<ThreadViewHolder> {

    public static int ThreadCellType = 0;

    protected WeakReference<Context> context;

    protected List<Thread> threads = new ArrayList<>();

    protected HashMap<Thread, String> typing = new HashMap<>();
    protected PublishSubject<Thread> onClickSubject = PublishSubject.create();
    protected PublishSubject<Thread> onLongClickSubject = PublishSubject.create();

    public ThreadsListAdapter(Context context) {
        this.context = new WeakReference(context);
    }

    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(com.womandroid.we.R.layout.chat_sdk_row_thread, null);
        return new ThreadViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final ThreadViewHolder holder, int position) {

        final Thread thread = threads.get(position);

        holder.nameTextView.setText(Strings.nameForThread(thread));

        holder.itemView.setOnClickListener(view -> onClickSubject.onNext(thread));

        holder.itemView.setOnLongClickListener(view -> {
            onLongClickSubject.onNext(thread);
            return true;
        });

        Date lastMessageAddedDate = thread.getLastMessageAddedDate();
        if (lastMessageAddedDate != null) {
            holder.dateTextView.setText(getLastMessageDateAsString(lastMessageAddedDate));

            Message message = thread.getLastMessage();
            if (message == null) {
                List<Message> messages = thread.getMessagesWithOrder(DaoCore.ORDER_DESC, 1);
                if (messages.size() > 0) {
                    message = messages.get(0);
                    thread.setLastMessage(message);
                    thread.update();
                }
            }


            holder.lastMessageTextView.setText(getLastMessageText(message));
        }

        if (typing.get(thread) != null) {
            holder.lastMessageTextView.setText(String.format(context.get().getString(com.womandroid.we.R.string.__typing), typing.get(thread)));
        }

        int unreadMessageCount = thread.getUnreadMessagesCount();

        if (unreadMessageCount != 0 && (thread.typeIs(ThreadType.Private) || ChatSDK.config().unreadMessagesCountForPublicChatRoomsEnabled)) {

            holder.unreadMessageCountTextView.setText(String.valueOf(unreadMessageCount));
            holder.unreadMessageCountTextView.setVisibility(View.VISIBLE);

            holder.showUnreadIndicator();
        }
        else {
            holder.hideUnreadIndicator();
            holder.unreadMessageCountTextView.setVisibility(View.INVISIBLE);
        }

        ThreadImageBuilder.load(holder.imageView, thread);
    }

    public String getLastMessageDateAsString (Date date) {
        if (date != null) {
            return Strings.dateTime(date);
        }
        return null;
    }

    public String getLastMessageText (Message lastMessage) {
        String messageText = Strings.t(com.womandroid.we.R.string.no_messages);
        if (lastMessage != null) {

            messageText = Strings.payloadAsString(lastMessage);
        }
        return messageText;
    }

    @Override
    public int getItemViewType(int position) {
        return ThreadCellType;
    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

    public boolean addRow (Thread thread, boolean notify) {
        for (Thread t : threads) {
            if (t.getEntityID().equals(thread.getEntityID())) {
                return false;
            }
        }

        threads.add(thread);
        if (notify) {
            notifyDataSetChanged();
        }
        return true;
    }

    public void addRow(Thread thread) {
        addRow(thread, true);
    }

    public void setTyping (Thread thread, String message) {
        if (message != null) {
            typing.put(thread, message);
        }
        else {
            typing.remove(thread);
        }
    }

    protected void sort() {
        Collections.sort(threads, new ThreadSorter());
    }

    public void clearData () {
        clearData(true);
    }

    public void clearData (boolean notify) {
        threads.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public Observable<Thread> onClickObservable () {
        return onClickSubject;
    }

    public Observable<Thread> onLongClickObservable () {
        return onLongClickSubject;
    }


    public void updateThreads (List<Thread> threads) {
        boolean added = false;
        for (Thread t : threads) {
            added = addRow(t, false) || added;
        }
        // Maybe the last message has changed. I think this can lead to a race condition
        // Which causes the thread not to update when a new message comes in
//        if (added) {
            sort();
            notifyDataSetChanged();
//        }
    }

    public void setThreads(List<Thread> threads) {
        clearData(false);
        updateThreads(threads);
    }


}

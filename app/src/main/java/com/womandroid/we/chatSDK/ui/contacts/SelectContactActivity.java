/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.ui.contacts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.events.EventType;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.interfaces.UserListItem;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.InterfaceManager;
import com.womandroid.we.chatSDK.core.session.StorageManager;
import com.womandroid.we.chatSDK.core.utils.UserListItemConverter;
import com.womandroid.we.chatSDK.ui.chat.ChatActivity;
import com.womandroid.we.chatSDK.ui.contacts.UsersListAdapter;
import com.womandroid.we.chatSDK.ui.main.BaseActivity;
import com.womandroid.we.chatSDK.ui.search.SearchActivity;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by itzik on 6/17/2014.
 */
public class SelectContactActivity extends BaseActivity {

    public static final int MODE_NEW_CONVERSATION = 1991;
    public static final int MODE_ADD_TO_CONVERSATION = 1992;

    public static final String MODE = "mode";

    protected RecyclerView recyclerView;
    protected UsersListAdapter adapter;
    protected Button btnStartChat;
    protected TextView txtSearch;
    protected ImageView imgSearch;

    /** Default value - MODE_NEW_CONVERSATION*/
    private int mode = MODE_NEW_CONVERSATION;

    /** For add to conversation mode.*/
    protected String threadEntityID = "";

    /** For add to conversation mode.*/
    protected Thread thread;

    /** Set true if you want slide down animation for this context exit. */
    protected boolean animateExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();

        if (savedInstanceState != null)
        {
            getDataFromBundle(savedInstanceState);
        }
        else
        {
            if (getIntent().getExtras() != null)
            {
                getDataFromBundle(getIntent().getExtras());
            }
        }

        // Refresh the list when the contacts change
        ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterContactsChanged())
                .subscribe(networkEvent -> loadData());

        ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.UserMetaUpdated))
                .subscribe(networkEvent -> loadData());

        setContentView(activityLayout());

        initViews();
    }

    protected void getDataFromBundle(Bundle bundle){
        mode = bundle.getInt(MODE, mode);
        threadEntityID = bundle.getString(InterfaceManager.THREAD_ENTITY_ID, threadEntityID);
        animateExit = bundle.getBoolean(ChatActivity.ANIMATE_EXIT, animateExit);
    }

    protected void initActionBar(){
        ActionBar ab = getSupportActionBar();
        if (ab!=null)
        {
            ab.setTitle(getString(R.string.pick_friends));
            ab.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(InterfaceManager.THREAD_ENTITY_ID, threadEntityID);
        outState.putInt(MODE, mode);
        outState.putBoolean(ChatActivity.ANIMATE_EXIT, animateExit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return true;
    }

    protected @LayoutRes int activityLayout() {
        return R.layout.chat_sdk_activity_pick_friends;
    }

    protected void initViews() {
        recyclerView = findViewById(R.id.chat_sdk_list_contacts);
        txtSearch = findViewById(R.id.chat_sdk_et_search);
        imgSearch = findViewById(R.id.chat_sdk_search_image);
        btnStartChat = findViewById(R.id.chat_sdk_btn_add_contacts);

        if (mode == MODE_ADD_TO_CONVERSATION) {
            btnStartChat.setText(getResources().getString(R.string.add_users));
        }

    }

    protected void initList() {
        boolean enableMultiSelect = ChatSDK.config().groupsEnabled;
        adapter = new UsersListAdapter(enableMultiSelect);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        loadData();

        adapter.getItemClicks().subscribe(item -> {
            if(item instanceof User) {
                if (ChatSDK.config().groupsEnabled) {
                    adapter.toggleSelection(item);
                }
                else {
                    UserListItem user = (UserListItem) item;
                    createAndOpenThread("", (User) user, ChatSDK.currentUser());
                }
            }
        });

    }

    protected void createAndOpenThread (String name, User... users) {
        createAndOpenThread(name, Arrays.asList(users));
    }

    protected Single<Thread> createAndOpenThread (String name, List<User> users) {
        return ChatSDK.thread().createThread(name, users)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess(thread -> {
                    if (thread != null) {
                        ChatSDK.ui().startChatActivityForID(getApplicationContext(), thread.getEntityID());
                    }
                }).doOnError(throwable -> ToastHelper.show(getApplicationContext(), R.string.create_thread_with_users_fail_toast));
    }

    protected void loadData () {
        final List<User> list = ChatSDK.contact().contacts();

        // Removing the users that is already inside the thread.
        if (mode == MODE_ADD_TO_CONVERSATION && !threadEntityID.equals("")){
            thread = StorageManager.shared().fetchThreadWithEntityID(threadEntityID);
            List<User> threadUser = thread.getUsers();
            list.removeAll(threadUser);
        }

        List<UserListItem> items = new ArrayList<>();
        for(User u : list) {
            items.add(u);
        }

        adapter.setUsers(items, true);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initList();

        btnStartChat.setOnClickListener(v -> {

            if (adapter.getSelectedCount() == 0) {
                showToast(getString(R.string.pick_friends_activity_no_users_selected_toast));
                return;
            }

            if (mode == MODE_ADD_TO_CONVERSATION) {
                showProgressDialog( getString(R.string.pick_friends_activity_prog_dialog_add_to_convo_message));
            }
            else if (mode == MODE_NEW_CONVERSATION) {
                showProgressDialog(getString(R.string.pick_friends_activity_prog_dialog_open_new_convo_message));
            }

            final ArrayList<User> users = new ArrayList<>();

            users.addAll(UserListItemConverter.toUserList(adapter.getSelectedUsers()));

            if (mode == MODE_NEW_CONVERSATION) {
                users.add(ChatSDK.currentUser());
                // If there are more than 2 users then show a dialog to enter the name
                if(users.size() > 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(com.womandroid.we.chatSDK.ui.contacts.SelectContactActivity.this);
                    builder.setTitle(getString(R.string.pick_friends_activity_prog_group_name_dialog));

                    // Set up the input
                    final EditText input = new EditText(com.womandroid.we.chatSDK.ui.contacts.SelectContactActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                    builder.setView(input);

                    // Set up the buttons
                    builder.setPositiveButton(getString(R.string.create), (dialog, which) -> com.womandroid.we.chatSDK.ui.contacts.SelectContactActivity.this.createAndOpenThread(input.getText().toString(), users)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((thread, throwable) -> {
                                dismissProgressDialog();
                                finish();
                            }));
                    builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.cancel();
                        dismissProgressDialog();
                    });

                    builder.show();

                }
                else {
                    createAndOpenThread("", users)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe((thread, throwable) -> {
                                dismissProgressDialog();
                                finish();
                            });
                }
            }
            else if (mode == MODE_ADD_TO_CONVERSATION){

                ChatSDK.thread().addUsersToThread(thread, users)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(() -> {
                            setResult(AppCompatActivity.RESULT_OK);
                            dismissProgressDialog();
                            finish();
                            if (animateExit) {
                                overridePendingTransition(R.anim.dummy, R.anim.slide_top_bottom_out);
                            }
                        }, throwable -> {
                            ChatSDK.logError(throwable);
                            dismissProgressDialog();
                            setResult(AppCompatActivity.RESULT_CANCELED);
                            finish();
                        });
            }
        });

        View.OnClickListener searchClickListener = v -> SearchActivity.startSearchActivity(com.womandroid.we.chatSDK.ui.contacts.SelectContactActivity.this);

        txtSearch.setOnClickListener(searchClickListener);
        imgSearch.setOnClickListener(searchClickListener);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (animateExit) {
            overridePendingTransition(R.anim.dummy, R.anim.slide_top_bottom_out);
        }
    }


}

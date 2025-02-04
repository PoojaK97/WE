package com.womandroid.we.chatSDK.ui.profile;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.InterfaceManager;
import com.womandroid.we.chatSDK.core.session.StorageManager;
import com.womandroid.we.chatSDK.core.utils.DisposableList;
import com.womandroid.we.chatSDK.ui.main.BaseActivity;
import com.womandroid.we.chatSDK.ui.profile.ProfileFragment;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by ben on 8/23/17.
 */

public class ProfileActivity extends BaseActivity {

    protected User user;
    protected boolean startingChat = false;
    protected MenuItem chatMenuItem;

    private DisposableList disposableList = new DisposableList();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_sdk_activity_profile);

        String userEntityID = getIntent().getStringExtra(InterfaceManager.USER_ENTITY_ID);

        if (userEntityID != null && !userEntityID.isEmpty()) {
            user =  StorageManager.shared().fetchUserWithEntityID(userEntityID);
            if (user != null) {
                ProfileFragment fragment = (ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.profile_fragment);
                fragment.setUser(user);
                fragment.updateInterface();
                return;
            }
        }

        ToastHelper.show(this, R.string.user_entity_id_not_set);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        chatMenuItem = menu.add(Menu.NONE, R.id.action_chat_sdk_chat, 1, getString(R.string.action_chat));
        chatMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        chatMenuItem.setIcon(R.drawable.icn_24_chat);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* Cant use switch in the library*/
        int id = item.getItemId();

        if (id == R.id.action_chat_sdk_chat) {
            startChat();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startChat () {

        if (startingChat) {
            return;
        }

        startingChat = true;

        showProgressDialog(getString(R.string.creating_thread));


        disposableList.add(ChatSDK.thread().createThread("", user, ChatSDK.currentUser())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    dismissProgressDialog();
                    startingChat = false;
                })
                .subscribe(thread -> {
                    ChatSDK.ui().startChatActivityForID(getApplicationContext(), thread.getEntityID());
                }, throwable -> {
                    ToastHelper.show(getApplicationContext(), throwable.getLocalizedMessage());
                }));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposableList.dispose();
    }

}

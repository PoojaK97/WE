/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.ui.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.events.EventType;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.StorageManager;
import com.womandroid.we.chatSDK.core.utils.CrashReportingCompletableObserver;
import com.womandroid.we.chatSDK.core.utils.DisposableList;
import com.womandroid.we.chatSDK.core.utils.UserListItemConverter;
import com.womandroid.we.chatSDK.ui.main.BaseFragment;
import com.womandroid.we.chatSDK.ui.search.SearchActivity;
import com.womandroid.we.chatSDK.ui.utils.ToastHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by itzik on 6/17/2014.
 */
public class ContactsFragment extends BaseFragment {

    /**
     * Loading all the current user contacts.
     */
    public static final int MODE_LOAD_CONTACTS = 1991;

    /**
     * Loading all users for given thread id mode
     */
    public static final int MODE_LOAD_THREAD_USERS = 1992;

    /**
     * Using the users that was given to the fragment in to initializer;
     */
    public static final int MODE_USE_SOURCE = 1995;

    public static final int MODE_LOAD_CONTACT_THAT_NOT_IN_THREAD = 1996;

    /**
     * Open profile context when user is clicked.
     */
    public static final int CLICK_MODE_SHOW_PROFILE = 0;
    /**
     * When a user clicked he will be added to the current thread.
     */
    public static final int CLICK_MODE_ADD_USER_TO_THREAD = 1;

    public static final String LOADING_MODE = "Loading_Mode";
    public static final String CLICK_MODE = "Click_Mode";
    public static final String IS_DIALOG = "Is_Dialog";

    /**
     * The text color that the adapter will use, Use -1 to set adapter to default color.
     */
    protected int textColor = -1991;

    protected UsersListAdapter adapter;
    protected ProgressBar progressBar;
    protected RecyclerView recyclerView;

    private boolean showProfileActivityTransitionStarted = false;

    protected Disposable listOnClickListenerDisposable;

    protected DisposableList disposables = new DisposableList();

    /**
     * Users that will be used to fill the adapter, This could be set manually or it will be filled when loading users for
     * {@link #loadingMode}
     */
    protected List<User> sourceUsers = new ArrayList<>();

    /**
     * Used when the fragment is shown as a dialog
     */
    protected String title = "";

    /**
     * Determine which users will be loaded to this fragment.
     *
     * @see #MODE_LOAD_CONTACT_THAT_NOT_IN_THREAD,
     * #MODE_LOAD_CONTACTS
     * #MODE_LOAD_FOLLOWERS
     * #MODE_LOAD_FOLLOWS
     * #MODE_LOAD_THREAD_USERS
     * #MODE_USE_SOURCE
     */
    protected int loadingMode = MODE_LOAD_CONTACTS;

    /**
     * Determine what happen after a user is clicked.
     *
     * @see #CLICK_MODE_ADD_USER_TO_THREAD
     * #CLICK_MODE_SHARE_CONTENT
     * #CLICK_MODE_SHOW_PROFILEs
     */
    protected int clickMode;

    protected Object extraData = "";

    /**
     * Set to false if you dont want any menu item to be inflated for this fragment.
     * This should be set before the fragment transaction,
     * if you extends the fragment you can call it in {@link #onCreate(Bundle)}
     * <B>see </B>{@link #setInflateMenu(boolean inflate)}
     */
    protected boolean inflateMenu = true;

    /**
     * When isDialog = true the dialog will always show the list of users given to him or pulled by the thread id.
     */
    protected boolean isDialog = false;

    public static com.womandroid.we.chatSDK.ui.contacts.ContactsFragment newInstance() {
        com.womandroid.we.chatSDK.ui.contacts.ContactsFragment f = new com.womandroid.we.chatSDK.ui.contacts.ContactsFragment();
        f.setLoadingMode(MODE_LOAD_CONTACTS);
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    public static com.womandroid.we.chatSDK.ui.contacts.ContactsFragment newInstance(int loadingMode, int clickMode, Object extraData) {
        com.womandroid.we.chatSDK.ui.contacts.ContactsFragment f = new com.womandroid.we.chatSDK.ui.contacts.ContactsFragment();
        f.setLoadingMode(loadingMode);
        f.setClickMode(clickMode);
        f.setExtraData(extraData);
        return f;
    }

    /**
     * Creates a new contact dialog.
     *
     * @param threadID - The id of the thread that his users is the want you want to show.
     * @param title    - The title of the dialog.
     */
    public static com.womandroid.we.chatSDK.ui.contacts.ContactsFragment newThreadUsersDialogInstance(String threadID, String title) {
        com.womandroid.we.chatSDK.ui.contacts.ContactsFragment f = new com.womandroid.we.chatSDK.ui.contacts.ContactsFragment();
        f.setTitle(title);

        f.setLoadingMode(MODE_LOAD_THREAD_USERS);
        f.setDialog();
        f.setExtraData(threadID);
        Bundle b = new Bundle();
        f.setArguments(b);

        return f;
    }

    public void setDialog() {
        this.isDialog = true;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLoadingMode(int loadingMode) {
        this.loadingMode = loadingMode;
    }

    public void setExtraData(Object extraData) {
        this.extraData = extraData;
    }

    public void setClickMode(int clickMode) {
        this.clickMode = clickMode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            loadingMode = savedInstanceState.getInt(LOADING_MODE);
            clickMode = savedInstanceState.getInt(CLICK_MODE);
            isDialog = savedInstanceState.getBoolean(IS_DIALOG);
        }

        if (!isDialog) {
            setHasOptionsMenu(true);
            setRetainInstance(true);
        }

        disposables.add(ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterContactsChanged())
                .subscribe(networkEvent -> loadData(false)));

        disposables.add(ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.UserPresenceUpdated))
                .subscribe(networkEvent -> loadData(true)));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isDialog) {
            if (title.equals("")) {
                getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            } else {
                getDialog().setTitle(title);
            }
        }

        mainView = inflater.inflate(activityLayout(), null);

        initViews();

        loadData(true);

        return mainView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(LOADING_MODE, loadingMode);
        outState.putBoolean(IS_DIALOG, isDialog);
    }

    protected @LayoutRes
    int activityLayout() {
        return R.layout.chat_sdk_fragment_contacts;
    }

    public void initViews() {
        recyclerView = mainView.findViewById(R.id.chat_sdk_list_contacts);

        progressBar = mainView.findViewById(R.id.chat_sdk_progressbar);

        // Create the adapter only if null this is here so we wont
        // override the adapter given from the extended class with setAdapter.
        adapter = new UsersListAdapter();

        setTextColor(textColor);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (!inflateMenu)
            return;

        MenuItem item =
                menu.add(Menu.NONE, R.id.action_chat_sdk_add, 10, "Add Chat");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_plus);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* Cant use switch in the library*/
        int id = item.getItemId();

        // Each user that will be found in the search context will be automatically added as a contact.
        if (id == R.id.action_chat_sdk_add) {
            SearchActivity.startSearchActivity(getActivity());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadData(final boolean force) {

        final ArrayList<User> originalUserList = new ArrayList<>();
        originalUserList.addAll(sourceUsers);

        Disposable d = reloadUsers().observeOn(AndroidSchedulers.mainThread()).subscribe(() -> {
            if (!originalUserList.equals(sourceUsers) || force) {
                adapter.setUsers(UserListItemConverter.toUserItemList(sourceUsers), true);
                Timber.v("Update Contact List");
            }
            setupListClickMode();
        }, throwable -> ChatSDK.logError(throwable));
    }

    @Override
    public void clearData() {
        if (adapter != null) {
            adapter.clear();
        }
    }

    protected void setupListClickMode() {
        if (listOnClickListenerDisposable != null) {
            listOnClickListenerDisposable.dispose();
        }
        listOnClickListenerDisposable = adapter.getItemClicks().subscribe(o -> {
            if (o instanceof User) {
                final User clickedUser = (User) o;

                switch (clickMode) {
                    case CLICK_MODE_ADD_USER_TO_THREAD:

                        Thread thread = null;
                        if (extraData instanceof Long) {
                            thread = StorageManager.shared().fetchThreadWithID((Long) extraData);
                        } else if (extraData instanceof String) {
                            thread = StorageManager.shared().fetchThreadWithEntityID((String) extraData);
                        }

                        if (thread != null) {
                            ChatSDK.thread().addUsersToThread(thread, clickedUser)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(() -> {
                                        ToastHelper.show(getContext(), getString(R.string.abstract_contact_fragment_user_added_to_thread_toast_success) + clickedUser.getName());
                                        if (isDialog) {
                                            getDialog().dismiss();
                                        }
                                    }, throwable -> {
                                        ChatSDK.logError(throwable);
                                        ToastHelper.show(getContext(), getString(R.string.abstract_contact_fragment_user_added_to_thread_toast_fail));
                                    });
                        }
                        break;
                    case CLICK_MODE_SHOW_PROFILE:
                    default: {
                        if (!showProfileActivityTransitionStarted) {
                            ChatSDK.ui().startProfileActivity(getContext(), clickedUser.getEntityID());
                            showProfileActivityTransitionStarted = true;
                        }
                    }
                }
            }
        });
    }

    protected Completable reloadUsers() {
        return Completable.create(e -> {
            if (loadingMode != MODE_USE_SOURCE) {

                sourceUsers.clear();
                // If this is not a dialog we will load the contacts of the user.
                switch (loadingMode) {
                    case MODE_LOAD_CONTACTS:
                        sourceUsers.addAll(ChatSDK.contact().contacts());
                        Timber.d("Contacts: " + sourceUsers.size());
                        break;

                    case MODE_LOAD_THREAD_USERS:
                        Thread thread = DaoCore.fetchEntityWithEntityID(Thread.class, extraData);

                        // Remove the current user from the list.
                        List<User> users = thread.getUsers();
                        users.remove(ChatSDK.currentUser());

                        sourceUsers.addAll(users);
                        break;

                    case MODE_LOAD_CONTACT_THAT_NOT_IN_THREAD:
                        List<User> users1 = ChatSDK.contact().contacts();
                        thread = StorageManager.shared().fetchThreadWithID((Long) extraData);
                        List<User> threadUser = thread.getUsers();
                        users1.removeAll(threadUser);
                        sourceUsers.addAll(users1);
                        break;
                }
            }
            e.onComplete();
        }).subscribeOn(Schedulers.single());
    }

    @Override
    public void onResume() {
        super.onResume();
        showProfileActivityTransitionStarted = false;
        loadData(true);
    }

    @Override
    public void reloadData() {
        reloadUsers().subscribe(new CrashReportingCompletableObserver());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void setInflateMenu(boolean inflateMenu) {
        this.inflateMenu = inflateMenu;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.dispose();
    }

}

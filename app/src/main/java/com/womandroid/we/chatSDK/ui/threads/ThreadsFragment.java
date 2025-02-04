package com.womandroid.we.chatSDK.ui.threads;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.events.EventType;
import com.womandroid.we.chatSDK.core.events.NetworkEvent;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.utils.DisposableList;
import com.womandroid.we.chatSDK.ui.main.BaseFragment;
import com.womandroid.we.chatSDK.ui.threads.ThreadsListAdapter;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

public abstract class ThreadsFragment extends BaseFragment {

    protected RecyclerView listThreads;
    protected EditText searchField;
    protected ThreadsListAdapter adapter;
    protected String filter;
    protected MenuItem addMenuItem;

    private DisposableList disposableList = new DisposableList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        disposableList.add(ChatSDK.events().sourceOnMain()
                .filter(mainEventFilter())
                .subscribe(networkEvent -> {
                    if (tabIsVisible) {
                        reloadData();
                    }
                }));

        disposableList.add(ChatSDK.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.TypingStateChanged))
                .subscribe(networkEvent -> {
                    if (tabIsVisible) {
                        adapter.setTyping(networkEvent.thread, networkEvent.text);
                        adapter.notifyDataSetChanged();
                    }
                }));

        reloadData();

        mainView = inflater.inflate(activityLayout(), null);

        initViews();

        return mainView;
    }

    protected abstract Predicate<NetworkEvent> mainEventFilter ();

    protected  @LayoutRes int activityLayout () {
        return R.layout.chat_sdk_activity_threads;
    }

    public void initViews() {
        searchField = mainView.findViewById(R.id.search_field);
        listThreads = mainView.findViewById(R.id.list_threads);

        adapter = new ThreadsListAdapter(getActivity());

        listThreads.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        listThreads.setAdapter(adapter);

        Disposable d = adapter.onClickObservable().subscribe(thread -> {
            ChatSDK.ui().startChatActivityForID(getContext(), thread.getEntityID());
        });
    }

    protected boolean allowThreadCreation () {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (allowThreadCreation()) {
            addMenuItem = menu.add(Menu.NONE, R.id.action_chat_sdk_add, 10, getString(R.string.thread_fragment_add_item_text));
            addMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            addMenuItem.setIcon(R.drawable.ic_plus);
        }
    }

    // Override this in the subclass to handle the plus button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadData();

        if (searchField != null) {
            searchField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filter = searchField.getText().toString();
                    reloadData();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    @Override
    public void clearData() {
        if (adapter != null) {
            adapter.clearData();
        }
    }

    public void setTabVisibility (boolean isVisible) {
        super.setTabVisibility(isVisible);
        reloadData();
    }

    @Override
    public void reloadData() {
        if (adapter != null) {
            adapter.clearData();
            List<Thread> threads = filter(getThreads());
            adapter.updateThreads(threads);
        }
    }

    protected abstract List<Thread> getThreads ();

    public List<Thread> filter (List<Thread> threads) {
        if (filter == null || filter.isEmpty()) {
            return threads;
        }

        List<Thread> filteredThreads = new ArrayList<>();
        for (Thread t : threads) {
            if (t.getName() != null && t.getName().toLowerCase().contains(filter.toLowerCase())) {
                filteredThreads.add(t);
            }
            else {
                for (User u : t.getUsers()) {
                    if (u.getName() != null && u.getName().toLowerCase().contains(filter.toLowerCase())) {
                        filteredThreads.add(t);
                        break;
                    }
                }
            }
        }
        return filteredThreads;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        disposableList.dispose();
    }

}

package com.womandroid.we.chatSDK.core.base;

import java.util.ArrayList;

import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.handlers.SearchHandler;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import io.reactivex.Observable;

public abstract class AbstractSearchHandler implements SearchHandler {

    public Observable<User> usersForIndex(final String value) {
        return usersForIndex(value, ChatSDK.config().contactsToLoadPerBatch);
    }

    public Observable<User> usersForIndexes(final String value, final String... indexes) {
        return usersForIndexes(value, ChatSDK.config().contactsToLoadPerBatch, indexes);
    }

    public Observable<User> usersForIndex(final String value, final String index) {
        return usersForIndex(value, ChatSDK.config().contactsToLoadPerBatch, index);
    }

    public Observable<User> usersForIndexes(String value, int limit, String... indexes) {
        ArrayList<Observable<User>> observables = new ArrayList<>();
        for (String index : indexes) {
            observables.add(usersForIndex(value, limit, index));
        }
        return Observable.merge(observables);
    }


}

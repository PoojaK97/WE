package com.womandroid.we.chatSDK.firebase;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import com.womandroid.we.chatSDK.core.base.BaseContactHandler;
import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.types.ConnectionType;
import com.womandroid.we.chatSDK.firebase.FirebasePaths;
import io.reactivex.Completable;

public class FirebaseContactHandler extends BaseContactHandler {

    @Override
    public Completable addContact(User user, ConnectionType type) {
        return Completable.create(emitter -> {
            DatabaseReference ref = FirebasePaths.userContactsRef(ChatSDK.currentUserID()).child(user.getEntityID());
            HashMap<String, Integer> data = new HashMap<>();
            data.put(Keys.Type, type.ordinal());
            ref.setValue(data, (databaseError, databaseReference) -> {
                if (databaseError == null) {
                    emitter.onComplete();
                } else {
                    emitter.onError(databaseError.toException());
                }
            });
        });
    }

    @Override
    public Completable deleteContact(User user, ConnectionType type) {
        return Completable.create(emitter -> {
            DatabaseReference ref = FirebasePaths.userContactsRef(ChatSDK.currentUserID()).child(user.getEntityID());
            ref.removeValue((databaseError, databaseReference) -> {
                if (databaseError == null) {
                    emitter.onComplete();
                } else {
                    emitter.onError(databaseError.toException());
                }
            });
        });
    }

}

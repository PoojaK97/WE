package com.womandroid.we.chatSDK.firebase;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.handlers.PublicThreadHandler;
import com.womandroid.we.chatSDK.core.interfaces.ThreadType;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.StorageManager;
import com.womandroid.we.chatSDK.firebase.wrappers.ThreadWrapper;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by benjaminsmiley-andrews on 24/05/2017.
 */

public class FirebasePublicThreadHandler implements PublicThreadHandler {

    public Single<Thread> createPublicThreadWithName(final String name) {
        return createPublicThreadWithName(name, null);
    }

    public Single<Thread> createPublicThreadWithName(final String name, final String entityID) {
        return Single.create((SingleOnSubscribe<Thread>) e -> {

            // If the entity ID is set, see if the thread exists and return it if it does
            if (entityID != null) {
                Thread t = StorageManager.shared().fetchThreadWithEntityID(entityID);
                if (t != null) {
                    e.onSuccess(t);
                    return;
                }
            }

            // Crating the new thread.
            // This thread would not be saved to the local db until it is successfully uploaded to the firebase server.
            final Thread thread = new Thread();

            User currentUser = ChatSDK.currentUser();
            thread.setCreator(currentUser);
            thread.setCreatorEntityId(currentUser.getEntityID());
            thread.setType(ThreadType.PublicGroup);
            thread.setName(name);
            thread.setEntityID(entityID);

            // Add the path and API key
            // This allows you to restrict public threads to a particular
            // API key or root key
            thread.setRootKey(ChatSDK.config().firebaseRootPath);

            // Save the entity to the local db.
            DaoCore.createEntity(thread);

            ThreadWrapper wrapper = new ThreadWrapper(thread);

            wrapper.push().doOnError(throwable -> {
                DaoCore.deleteEntity(thread);
                e.onError(throwable);
            }).subscribe(() -> {
                DaoCore.updateEntity(thread);

                // Add the thread to the list of public threads
                DatabaseReference publicThreadRef = FirebasePaths.publicThreadsRef()
                        .child(thread.getEntityID());

                HashMap<String, Object> value = new HashMap<>();
                value.put(Keys.Null, "");

                publicThreadRef.setValue(value, (databaseError, databaseReference) -> {
                    if(databaseError == null) {
                        e.onSuccess(thread);
                    }
                    else {
                        DaoCore.deleteEntity(thread);
                        e.onError(databaseError.toException());
                    }
                });
            });

        }).subscribeOn(Schedulers.single());
    }


}

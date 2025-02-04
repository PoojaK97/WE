/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:35 PM
 */

package com.womandroid.we.chatSDK.firebase.wrappers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

import com.womandroid.we.R;
import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.dao.Keys;
import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.session.ChatSDK;
import com.womandroid.we.chatSDK.core.session.StorageManager;
import com.womandroid.we.chatSDK.core.types.MessageSendStatus;
import com.womandroid.we.chatSDK.core.types.ReadStatus;
import com.womandroid.we.chatSDK.firebase.FirebaseEntity;
import com.womandroid.we.chatSDK.firebase.FirebasePaths;
import com.womandroid.we.chatSDK.firebase.wrappers.ThreadWrapper;
import com.womandroid.we.chatSDK.firebase.wrappers.UserWrapper;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class MessageWrapper  {

    private Message model;

    public MessageWrapper(Message model){
        this.model = model;
    }

    public MessageWrapper(DataSnapshot snapshot){
        this.model = StorageManager.shared().fetchOrCreateEntityWithEntityID(Message.class, snapshot.getKey());
        deserialize(snapshot);
    }

    Map<String, Object> serialize() {
        Map<String, Object> values = new HashMap<String, Object>();

        values.put(Keys.JSON, model.getMetaValuesAsMap());
        values.put(Keys.Date, ServerValue.TIMESTAMP);
        values.put(Keys.Type, model.getType());
        values.put(Keys.UserFirebaseId, model.getSender().getEntityID());
        values.put(FirebasePaths.ReadPath, initialReadReceipts());

        return values;
    }

    private HashMap<String, HashMap<String, Integer>> initialReadReceipts () {
        HashMap<String, HashMap<String, Integer>> map = new HashMap<>();

        for(User u : getModel().getThread().getUsers()) {
            if(!u.isMe()) {
                HashMap<String, Integer> status = new HashMap<>();
                status.put(Keys.Status, ReadStatus.none().getValue());
                map.put(u.getEntityID(), status);
            }
        }

        return map;
    }

    private boolean contains (Map<String, Object> value, String key) {
        return value.containsKey(key) && !value.get(key).equals("");
    }

    private String string (Map<String, Object> value, String key) {
        if(contains(value, key)) {
            return (String) value.get(key);
        }
        return null;
    }

    private HashMap<String, Object> map (Map<String, Object> value, String key) {
        if(contains(value, key)) {
            if (value.get(key) instanceof HashMap) {
                return (HashMap<String, Object>) value.get(key);
            }
        }
        return null;
    }

    private Long long_ (Map<String, Object> value, String key) {
        if(contains(value, key)) {
            Object o = value.get(key);
            if(o instanceof Long) {
                return (Long) value.get(key);
            }
            if(o instanceof Integer) {
                return ((Integer) value.get(key)).longValue();
            }
        }
        return null;
    }

    @SuppressWarnings("all") void deserialize(DataSnapshot snapshot) {

        Map<String, Object> value = (Map<String, Object>) snapshot.getValue();
        //if (DEBUG) Timber.v("deserialize, Value: %s", value);
        if (value == null) return;

        Object json = snapshot.child(Keys.JSON).getValue();

        if (json != null && json instanceof HashMap) {
            model.setMetaValues((HashMap) json);
        }
        else {
            model.setText("");
        }

        Long type = long_(value, Keys.Type);
        if(type != null) {
            model.setType(type.intValue());
        }

        Long date = long_(value, Keys.Date);
        if(date != null) {
            // If the server time of the message is too different to local time
            // set the status to none, which causes the message to be refreshed
            // in the chat view.
            if (this.getModel().getDate() == null || Math.abs(this.getModel().getDate().toDate().getTime() - date) > 1000) {
                model.setMessageStatus(MessageSendStatus.None);
            }
            model.setDate(new DateTime(date));
        }

        String senderID = string(value, Keys.UserFirebaseId);
        if(senderID != null) {
            User user = DaoCore.fetchEntityWithEntityID(User.class, senderID);
            if (user == null)
            {
                user = StorageManager.shared().fetchOrCreateEntityWithEntityID(User.class, senderID);
                UserWrapper.initWithModel(user).once();
            }

            model.setSender(user);

        }

        // Get read information
        HashMap<String, Object> readMap = map(value, Keys.Read);
        if (readMap != null) {
            updateReadReceipts(readMap);
        }

        model.update();
    }

    public void updateReadReceipts (HashMap<String, Object> map) {
        for(String key : map.keySet()) {

            User user = StorageManager.shared().fetchOrCreateEntityWithEntityID(User.class, key);

            Object innerMap = map.get(key);

            if(innerMap != null && innerMap instanceof HashMap) {

                Map<String, Object> statusMap = (Map) innerMap;
                long status = ReadStatus.None;
                long date = 0;

                if(statusMap.get(Keys.Status) instanceof Long) {
                    status = (Long) statusMap.get(Keys.Status);
                }
                if(statusMap.get(Keys.Date) instanceof Long) {
                    date = (Long) statusMap.get(Keys.Date);
                }

                model.setUserReadStatus(user, new ReadStatus((int) status), new DateTime(date));
            }
        }

    }

    public Completable push() {
        return Completable.create(e -> {

            // Getting the message ref. Will be created if not exist.
            final DatabaseReference ref = ref();
            model.setEntityID(ref.getKey());
            DaoCore.updateEntity(model);

            ref.setValue(serialize(), ServerValue.TIMESTAMP, (firebaseError, firebase) -> {
                if (firebaseError == null) {
                    e.onComplete();
                } else {
                    e.onError(firebaseError.toException());
                }
            });
        });
    }
    
    public Completable send() {
        return Completable.create(e -> {
            if(model.getThread() != null) {
                push().concatWith(new ThreadWrapper(model.getThread()).pushLastMessage(lastMessageData())).subscribe(() -> {
                    FirebaseEntity.pushThreadMessagesUpdated(model.getThread().getEntityID());
                    model.setMessageStatus(MessageSendStatus.Sent);
                    model.update();
                    e.onComplete();
                }, e::onError);
            }
            else {
                e.onError(new Throwable(ChatSDK.shared().context().getString(R.string.message_doesnt_have_a_thread)));
            }
        }).subscribeOn(Schedulers.single());

    }

    public HashMap<String, Object> lastMessageData () {
        HashMap<String, Object> map = new HashMap<>();
        map.put(Keys.Type, model.getType());
        map.put(Keys.Date, ServerValue.TIMESTAMP);
        map.put(Keys.UserFirebaseId, model.getSender().getEntityID());
        map.put(Keys.UserName, model.getSender().getName());
        return map;
    }

    public Completable delete () {
        return Completable.create(e -> {
            DatabaseReference ref = FirebasePaths.threadMessagesRef(model.getThread().getEntityID()).child(model.getEntityID());
            ref.removeValue((databaseError, databaseReference) -> {
                if (databaseError == null) {
                    e.onComplete();
                }
                else {
                    e.onError(new Throwable(databaseError.getMessage()));
                }
            });
        });
    }
    
    private DatabaseReference ref(){
        if (StringUtils.isNotEmpty(model.getEntityID()))
        {
            return FirebasePaths.threadMessagesRef(model.getThread().getEntityID()).child(model.getEntityID());
        }
        else
        {
            return FirebasePaths.threadMessagesRef(model.getThread().getEntityID()).push();
        }
    }

    public Message getModel() {
        return model;
    }

}

package com.womandroid.we.chatSDK.core.events;

import com.womandroid.we.chatSDK.core.dao.Message;
import com.womandroid.we.chatSDK.core.dao.Thread;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.interfaces.ThreadType;
import io.reactivex.functions.Predicate;

/**
 * Created by benjaminsmiley-andrews on 16/05/2017.
 */

public class NetworkEvent {

    final public EventType type;
    public Message message;
    public Thread thread;
    public User user;
    public String text;

    public NetworkEvent(EventType type) {
        this.type = type;
    }

    public NetworkEvent(EventType type, Thread thread) {
        this(type, thread, null, null);
    }

    public NetworkEvent(EventType type, Thread thread, Message message) {
        this(type, thread, message, null);
    }

    public NetworkEvent(EventType type, Thread thread, Message message, User user) {
        this.type = type;
        this.thread = thread;
        this.message = message;
        this.user = user;
    }

    public static NetworkEvent threadAdded (Thread thread) {
        return new NetworkEvent(EventType.ThreadAdded, thread);
    }

    public static NetworkEvent threadRemoved (Thread thread) {
        return new NetworkEvent(EventType.ThreadRemoved, thread);
    }

    public static NetworkEvent followerAdded () {
        return new NetworkEvent(EventType.FollowerAdded);
    }

    public static NetworkEvent followerRemoved () {
        return new NetworkEvent(EventType.FollowerRemoved);
    }

    public static NetworkEvent followingAdded () {
        return new NetworkEvent(EventType.FollowingAdded);
    }

    public static NetworkEvent followingRemoved () {
        return new NetworkEvent(EventType.FollowingRemoved);
    }

    public static NetworkEvent threadDetailsUpdated (Thread thread) {
        return new NetworkEvent(EventType.ThreadDetailsUpdated, thread);
    }

    public static NetworkEvent threadLastMessageUpdated (Thread thread) {
        return new NetworkEvent(EventType.ThreadLastMessageUpdated, thread);
    }

    public static NetworkEvent threadMetaUpdated (Thread thread) {
        return new NetworkEvent(EventType.ThreadMetaUpdated, thread);
    }

    public static NetworkEvent messageAdded (Thread thread, Message message) {
        return new NetworkEvent(EventType.MessageAdded, thread, message);
    }

    public static NetworkEvent messageRemoved (Thread thread, Message message) {
        return new NetworkEvent(EventType.MessageRemoved, thread, message);
    }

    public static NetworkEvent threadUsersChanged (Thread thread, User user) {
        return new NetworkEvent(EventType.ThreadUsersChanged, thread, null, user);
    }


    public static NetworkEvent userMetaUpdated (User user) {
        return new NetworkEvent(EventType.UserMetaUpdated, null, null, user);
    }

    public static NetworkEvent userPresenceUpdated (User user) {
        return new NetworkEvent(EventType.UserPresenceUpdated, null, null, user);
    }

    public static NetworkEvent contactAdded (User user) {
        return new NetworkEvent(EventType.ContactAdded, null, null, user);
    }

    public static NetworkEvent contactDeleted (User user) {
        return new NetworkEvent(EventType.ContactDeleted, null, null, user);
    }

    public static NetworkEvent contactChanged (User user) {
        return new NetworkEvent(EventType.ContactChanged, null, null, user);
    }

    public static NetworkEvent contactsUpdated () {
        return new NetworkEvent(EventType.ContactsUpdated);
    }

    public static NetworkEvent threadRead (Thread thread) {
        return new NetworkEvent(EventType.ThreadRead, thread);
    }

    public static NetworkEvent threadReadReceiptUpdated (Thread thread, Message message) {
        return new NetworkEvent(EventType.ThreadReadReceiptUpdated, thread, message);
    }

    public static NetworkEvent typingStateChanged (String message, Thread thread) {
        NetworkEvent event = new NetworkEvent(EventType.TypingStateChanged);
        event.text = message;
        event.thread = thread;
        return event;
    }

    public static NetworkEvent logout () {
        return new NetworkEvent(EventType.Logout);
    }

//    public Predicate<NetworkEvent> filter () {
//        return new Predicate<NetworkEvent>() {
//            @Override
//            public boolean test(NetworkEvent networkEvent) throws Exception {
//                return networkEvent.type == type;
//            }
//        };
//    }

    public static Predicate<NetworkEvent> filterType (final EventType type) {
        return networkEvent -> networkEvent.type == type;
    }

    public static Predicate<NetworkEvent> filterType (final EventType... types) {
        return networkEvent -> {
            for(EventType type: types) {
                if(networkEvent.type == type)
                    return true;
            }
            return false;
        };
    }

    public static Predicate<NetworkEvent> filterThreadEntityID (final String entityID) {
        return networkEvent -> {
            if(networkEvent.thread != null) {
                if (networkEvent.thread.getEntityID().equals(entityID)) {
                    return true;
                }
            }
            return false;
        };
    }

    public static Predicate<NetworkEvent> filterThreadType (final int type) {
        return networkEvent -> {
            if(networkEvent.thread != null) {
                Thread thread = networkEvent.thread;
                return thread.typeIs(type);
            }
            return false;
        };
    }

    public static Predicate<NetworkEvent> threadsUpdated () {
        return filterType(
                EventType.ThreadDetailsUpdated,
                EventType.ThreadAdded,
                EventType.ThreadRemoved,
                EventType.ThreadLastMessageUpdated,
                EventType.ThreadUsersChanged,
                EventType.MessageRemoved,
                EventType.UserMetaUpdated // Be careful to check that the user is a member of the thread...
        );
    }


    public static Predicate<NetworkEvent> filterPrivateThreadsUpdated () {
        return networkEvent -> threadsUpdated().test(networkEvent) && filterThreadType(ThreadType.Private).test(networkEvent);
     }

    public static Predicate<NetworkEvent> filterPublicThreadsUpdated () {
        return networkEvent -> threadsUpdated().test(networkEvent) && filterThreadType(ThreadType.Public).test(networkEvent);
    }

    public static Predicate<NetworkEvent> filterContactsChanged () {
        return filterType(
                EventType.ContactChanged,
                EventType.ContactAdded,
                EventType.ContactDeleted,
                EventType.ContactsUpdated
        );
    }

    public static Predicate<NetworkEvent> threadUsersUpdated () {
        return filterType(
                EventType.ThreadUsersChanged,
                EventType.UserPresenceUpdated
        );
    }

}

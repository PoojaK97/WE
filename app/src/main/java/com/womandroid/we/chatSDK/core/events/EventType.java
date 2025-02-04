package com.womandroid.we.chatSDK.core.events;

/**
 * Created by benjaminsmiley-andrews on 16/05/2017.
 */

public enum EventType {

    ThreadAdded,
    ThreadRemoved,
    FollowerAdded,
    FollowerRemoved,
    FollowingAdded,
    FollowingRemoved,
    ThreadDetailsUpdated,
    ThreadLastMessageUpdated,
    ThreadMetaUpdated,
    MessageAdded,
    MessageRemoved,
    ThreadUsersChanged,
    UserMetaUpdated,
    UserPresenceUpdated,
    ContactAdded,
    ContactDeleted,
    ContactChanged,
    ContactsUpdated,
    TypingStateChanged,
    Logout,
    ThreadRead,
    ThreadReadReceiptUpdated,
}

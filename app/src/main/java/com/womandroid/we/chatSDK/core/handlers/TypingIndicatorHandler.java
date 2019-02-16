package com.womandroid.we.chatSDK.core.handlers;

import io.reactivex.Completable;
import com.womandroid.we.chatSDK.core.dao.Thread;

/**
 * Created by SimonSmiley-Andrews on 01/05/2017.
 */

public interface TypingIndicatorHandler {

    enum State {
        /**
         * User is actively participating in the chat session.
         */
        active,
        /**
         * User is composing a message.
         */
        composing,
        /**
         * User had been composing but now has stopped.
         */
        paused,
        /**
         * User has not been actively participating in the chat session.
         */
        inactive,
        /**
         * User has effectively ended their participation in the chat session.
         */
        gone
    }

    void typingOn(Thread thread);
    void typingOff(Thread thread);
    Completable setChatState(State state, Thread thread);
//
//    -(RXPromise *) setChatState: (bChatState) state forThread: (id<PThread>) thread;
}

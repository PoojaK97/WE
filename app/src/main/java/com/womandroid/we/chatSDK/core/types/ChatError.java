/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.womandroid.we.chatSDK.core.types;

/**
 * Created by braunster on 27/06/14.
 */
public class ChatError extends Throwable {

    public Object tag = null;
    public int code = -1;

    public ChatError(int code){
        this(code, Message.getMessageForCode(code));
    }

    public ChatError(int code, String message){
        super(message);
        this.code = code;
    }

    public ChatError(int code, Object tag){
        this(code);
        this.tag = tag;
    }

    public ChatError(int code, String message, Object tag){
        this(code, message);
        this.tag = tag;
    }

    /*Static Initializer's*/
    public static ChatError getError(int code, String message){
        return new ChatError(code, message);
    }

    public static ChatError getError(int code) {
        return new ChatError(code);
    }

    public static ChatError getNoPathError() {
        return new ChatError(Code.NO_PATH);
    }

    public static ChatError getExceptionError(Exception e){
        return new ChatError(Code.EXCEPTION, e.getMessage(), e);
    }

    public static ChatError getExceptionError(Exception e, String message){
        return new ChatError(Code.EXCEPTION, message, e);
    }

    public static final class Code {

        // General Errors
        public static final int EXCEPTION = 12; // When an exception occurred that cause the error.
        public static final int NO_PATH = 13; // When an entity does not have valid path to push to.
        public static final int NETWORK_ERROR = 14;
        public static final int NULL = 16; // If something that related to the wanted method was null.
        public static final int NO_LOGIN_INFO = 17; // When there is no available login info to use when login.
        public static final int NO_LOGIN_TYPE = 20;
        public static final int EXPIRED_TOKEN = 21;
        public static final int OPERATION_FAILED = 23;
        public static final int PERMISSION_DENIED = 24;
        public static final int DISCONNECTED = 25;
        public static final int INVALID_TOKEN = 26;
        public static final int MAX_RETRIES = 27;
        public static final int OVERRIDDEN_BY_SET = 28;
        public static final int UNAVAILABLE = 29;
        public static final int UNKNOWN_ERROR = 30;
        public static final int USER_CODE_EXCEPTION = 31;
        public static final int WRITE_CANCELED = 32;
        public static final int FIREBASE_STORAGE_EXCEPTION = 33;

        public static final int AUTH_IN_PROCESS = 105;
        public static final int NO_AUTH_DATA = 106;

    }

    public static final class Message{
        static String getMessageForCode(int code){
            switch (code)
            {
                case Code.EXCEPTION:
                    return ExcpetionOccurred;

                case Code.NO_LOGIN_INFO:
                    return NoLoginInfo;

                case Code.NO_PATH:
                    return NoPath;

                default: return "";
            }
        }

        public static final String ExcpetionOccurred = "Exception occurred";
        public static final String NoLoginInfo = "No older login data is save in the preferences.";
        public static final String NoPath = "CoreEntity Path is null";
        public static final String Tagged = "Tagged";
    }


    @Override
    public String toString() {
        return String.format("ChatError, Code: %s, CoreMessage: %s, Tag: %s", code, getMessage(), tag);
    }
}

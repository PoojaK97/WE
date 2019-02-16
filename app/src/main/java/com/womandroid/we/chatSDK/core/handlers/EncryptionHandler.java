package com.womandroid.we.chatSDK.core.handlers;

import com.womandroid.we.chatSDK.core.dao.Message;

public interface EncryptionHandler {

    void encrypt(Message message);
    void decrypt(Message message);
}
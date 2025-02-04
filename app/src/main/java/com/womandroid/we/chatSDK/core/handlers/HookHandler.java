package com.womandroid.we.chatSDK.core.handlers;

import java.util.HashMap;

import com.womandroid.we.chatSDK.core.hook.Hook;
import io.reactivex.Completable;

/**
 * Created by ben on 9/13/17.
 */

public interface HookHandler {

    void addHook(Hook hook, String name);
    void removeHook(Hook hook, String name);
    Completable executeHook(String name, HashMap<String, Object> data);

}

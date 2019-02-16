package com.womandroid.we.chatSDK.core.base;

import org.apache.commons.lang3.StringUtils;

import com.womandroid.we.chatSDK.core.dao.DaoCore;
import com.womandroid.we.chatSDK.core.dao.User;
import com.womandroid.we.chatSDK.core.handlers.CoreHandler;
import com.womandroid.we.chatSDK.core.session.ChatSDK;

/**
 * Created by benjaminsmiley-andrews on 03/05/2017.
 */

public abstract class AbstractCoreHandler implements CoreHandler {

    private User cachedUser = null;

    public User currentUserModel(){
        String entityID = ChatSDK.auth().getCurrentUserEntityID();

        if(cachedUser == null || !cachedUser.getEntityID().equals(entityID)) {
            if (StringUtils.isNotEmpty(entityID)) {
                cachedUser = DaoCore.fetchEntityWithEntityID(User.class, entityID);
            }
            else {
                cachedUser = null;
            }
        }
       return cachedUser;
    }

    @Override
    public void goOnline() {
        if (ChatSDK.lastOnline() != null) {
            ChatSDK.lastOnline().setLastOnline(currentUserModel());
        }
    }
}

package com.womandroid.we.chatSDK.core.dao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.joda.time.DateTime;

import com.womandroid.we.chatSDK.core.interfaces.CoreEntity;
import com.womandroid.we.chatSDK.core.utils.DaoDateTimeConverter;
import com.womandroid.we.chatSDK.core.dao.DaoSession;
import com.womandroid.we.chatSDK.core.dao.UserDao;
import com.womandroid.we.chatSDK.core.dao.ReadReceiptUserLinkDao;

/**
 * Created by ben on 10/5/17.
 */

@Entity
public class ReadReceiptUserLink implements CoreEntity {

    @Id
    private Long id;

    private Long messageId;
    private Long userId;
    private Integer status;

    @Convert(converter = DaoDateTimeConverter.class, columnType = Long.class)
    private DateTime date;

    @ToOne(joinProperty = "userId")
    private User user;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 642681985)
    private transient ReadReceiptUserLinkDao myDao;

    @Generated(hash = 1676661430)
    public ReadReceiptUserLink(Long id, Long messageId, Long userId, Integer status,
            DateTime date) {
        this.id = id;
        this.messageId = messageId;
        this.userId = userId;
        this.status = status;
        this.date = date;
    }

    @Generated(hash = 1437583804)
    public ReadReceiptUserLink() {
    }

    public Long getId() {
        return this.id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Integer getStatus() {
        return this.status;
    }

    @Override
    public void setEntityID(String entityID) {
        setId(Long.parseLong(entityID));
    }

    @Override
    public String getEntityID() {
        return Long.toString(id);
    }

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 859885876)
    public User getUser() {
        Long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1065606912)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            userId = user == null ? null : user.getId();
            user__resolvedKey = userId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public DateTime getDate() {
        return this.date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1250799234)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getReadReceiptUserLinkDao() : null;
    }


}

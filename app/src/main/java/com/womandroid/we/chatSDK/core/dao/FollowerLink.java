package com.womandroid.we.chatSDK.core.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your token includes here

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToOne;

import com.womandroid.we.chatSDK.core.interfaces.CoreEntity;
// KEEP INCLUDES END
import com.womandroid.we.chatSDK.core.dao.DaoSession;
import com.womandroid.we.chatSDK.core.dao.UserDao;
import com.womandroid.we.chatSDK.core.dao.FollowerLinkDao;

/**
 * CoreEntity mapped to table FOLLOWER_LINK.
 */
@Entity
public class FollowerLink implements CoreEntity {

    @Id
    private Long id;
    private Integer type;
    private Long userId;
    private Long linkOwnerUserDaoId;

    @Keep
    public class Type{
        public static final int FOLLOWER = 0, FOLLOWS = 1;
    }

    public void setEntityID (String entityID) {

    }

    public String getEntityID () {
        return id.toString();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return this.type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getLinkOwnerUserDaoId() {
        return this.linkOwnerUserDaoId;
    }

    public void setLinkOwnerUserDaoId(Long linkOwnerUserDaoId) {
        this.linkOwnerUserDaoId = linkOwnerUserDaoId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1603849730)
    public User getUser() {
        Long __key = this.userId;
        if (User__resolvedKey == null || !User__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User UserNew = targetDao.load(__key);
            synchronized (this) {
                User = UserNew;
                User__resolvedKey = __key;
            }
        }
        return User;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1829077122)
    public void setUser(User User) {
        synchronized (this) {
            this.User = User;
            userId = User == null ? null : User.getId();
            User__resolvedKey = userId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 731615395)
    public User getLinkOwnerUser() {
        Long __key = this.linkOwnerUserDaoId;
        if (linkOwnerUser__resolvedKey == null
                || !linkOwnerUser__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User linkOwnerUserNew = targetDao.load(__key);
            synchronized (this) {
                linkOwnerUser = linkOwnerUserNew;
                linkOwnerUser__resolvedKey = __key;
            }
        }
        return linkOwnerUser;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1247524206)
    public void setLinkOwnerUser(User linkOwnerUser) {
        synchronized (this) {
            this.linkOwnerUser = linkOwnerUser;
            linkOwnerUserDaoId = linkOwnerUser == null ? null
                    : linkOwnerUser.getId();
            linkOwnerUser__resolvedKey = linkOwnerUserDaoId;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 698127873)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFollowerLinkDao() : null;
    }


    @ToOne(joinProperty = "userId")
    private User User;
    @ToOne(joinProperty = "linkOwnerUserDaoId")
    private User linkOwnerUser;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1016109725)
    private transient FollowerLinkDao myDao;
    @Generated(hash = 1877452843)
    public FollowerLink(Long id, Integer type, Long userId,
            Long linkOwnerUserDaoId) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.linkOwnerUserDaoId = linkOwnerUserDaoId;
    }

    @Generated(hash = 868080140)
    public FollowerLink() {
    }


    @Generated(hash = 645180804)
    private transient Long User__resolvedKey;
    @Generated(hash = 53129048)
    private transient Long linkOwnerUser__resolvedKey;



}

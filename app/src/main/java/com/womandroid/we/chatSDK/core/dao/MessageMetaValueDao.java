package com.womandroid.we.chatSDK.core.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE_META_VALUE".
*/
public class MessageMetaValueDao extends AbstractDao<MessageMetaValue, Long> {

    public static final String TABLENAME = "MESSAGE_META_VALUE";

    /**
     * Properties of entity MessageMetaValue.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, String.class, "key", false, "KEY");
        public final static Property Value = new Property(2, String.class, "value", false, "VALUE");
        public final static Property MessageId = new Property(3, Long.class, "messageId", false, "MESSAGE_ID");
    }

    private DaoSession daoSession;

    private Query<MessageMetaValue> message_MetaValuesQuery;

    public MessageMetaValueDao(DaoConfig config) {
        super(config);
    }
    
    public MessageMetaValueDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE_META_VALUE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"KEY\" TEXT," + // 1: key
                "\"VALUE\" TEXT," + // 2: value
                "\"MESSAGE_ID\" INTEGER);"); // 3: messageId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE_META_VALUE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MessageMetaValue entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(2, key);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        Long messageId = entity.getMessageId();
        if (messageId != null) {
            stmt.bindLong(4, messageId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MessageMetaValue entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(2, key);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        Long messageId = entity.getMessageId();
        if (messageId != null) {
            stmt.bindLong(4, messageId);
        }
    }

    @Override
    protected final void attachEntity(MessageMetaValue entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MessageMetaValue readEntity(Cursor cursor, int offset) {
        MessageMetaValue entity = new MessageMetaValue( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // value
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // messageId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MessageMetaValue entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setValue(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMessageId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MessageMetaValue entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MessageMetaValue entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MessageMetaValue entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "metaValues" to-many relationship of Message. */
    public List<MessageMetaValue> _queryMessage_MetaValues(Long messageId) {
        synchronized (this) {
            if (message_MetaValuesQuery == null) {
                QueryBuilder<MessageMetaValue> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.MessageId.eq(null));
                message_MetaValuesQuery = queryBuilder.build();
            }
        }
        Query<MessageMetaValue> query = message_MetaValuesQuery.forCurrentThread();
        query.setParameter(0, messageId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getMessageDao().getAllColumns());
            builder.append(" FROM MESSAGE_META_VALUE T");
            builder.append(" LEFT JOIN MESSAGE T0 ON T.\"MESSAGE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected MessageMetaValue loadCurrentDeep(Cursor cursor, boolean lock) {
        MessageMetaValue entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Message message = loadCurrentOther(daoSession.getMessageDao(), cursor, offset);
        entity.setMessage(message);

        return entity;    
    }

    public MessageMetaValue loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<MessageMetaValue> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<MessageMetaValue> list = new ArrayList<MessageMetaValue>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<MessageMetaValue> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<MessageMetaValue> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}

package com.womandroid.we.chatSDK.core.dao;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ben on 4/13/18.
 */

public class DatabaseUpgradeHelper extends DaoMaster.OpenHelper {

    public DatabaseUpgradeHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        List<Migration> migrations = getMigrations();

        // Only run migrations past the old version
        for (Migration migration : migrations) {
            if (oldVersion < migration.getVersion()) {
                migration.runMigration(db);
            }
        }
    }

    private List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();
        migrations.add(new MigrationV2());
        migrations.add(new MigrationV3());
        migrations.add(new MigrationV4());
        migrations.add(new MigrationV5());
        migrations.add(new MigrationV6());
        migrations.add(new MigrationV7());

        // Sorting just to be safe, in case other people add migrations in the wrong order.
        Comparator<Migration> migrationComparator = (m1, m2) -> m1.getVersion().compareTo(m2.getVersion());
        Collections.sort(migrations, migrationComparator);

        return migrations;
    }

    private static class MigrationV2 implements Migration {

        @Override
        public Integer getVersion() {
            return 2;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            //UserDao.createTable(db, false);
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " ADD COLUMN " + MessageDao.Properties.NextMessageId.columnName + " LONG");
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " ADD COLUMN " + MessageDao.Properties.LastMessageId.columnName + " LONG");
        }
    }

    private static class MigrationV3 implements Migration {

        @Override
        public Integer getVersion() {
            return 3;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table

            ThreadMetaValueDao.createTable(db, true);
            UserMetaValueDao.createTable(db, true);
            db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " DROP COLUMN " + "META_DATA");
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " DROP COLUMN " + "RESOURCES");

        }
    }

    private static class MigrationV4 implements Migration {

        @Override
        public Integer getVersion() {
            return 4;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + UserDao.TABLENAME + " DROP COLUMN " + "LAST_UPDATED");
        }
    }

    private static class MigrationV5 implements Migration {

        @Override
        public Integer getVersion() {
            return 5;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            db.execSQL("ALTER TABLE " + MessageDao.TABLENAME + " DROP COLUMN " + "TEXT");
        }
    }

    private static class MigrationV6 implements Migration {

        @Override
        public Integer getVersion() {
            return 6;
        }

        @Override
        public void runMigration(Database db) {
            //Adding new table
            MessageMetaValueDao.createTable(db, true);

            ReadReceiptUserLinkDao.dropTable(db, false);
            ReadReceiptUserLinkDao.createTable(db, true);

//            db.execSQL("ALTER TABLE " + ReadReceiptUserLinkDao.TABLENAME + " DROP COLUMN " + "READRECEIPTID");

            // Clear down the message database and re synchronize
            List<Message> messages = DaoCore.fetchEntitiesOfClass(Message.class);
            for (Message m : messages) {
                DaoCore.deleteEntity(m);
            }
        }
    }

    private static class MigrationV7 implements Migration {

        @Override
        public Integer getVersion() {
            return 7;
        }

        @Override
        public void runMigration(Database db) {
            ReadReceiptUserLinkDao.dropTable(db, false);
            ReadReceiptUserLinkDao.createTable(db, true);
        }
    }


    private interface Migration {
        Integer getVersion();
        void runMigration(Database db);
    }

}

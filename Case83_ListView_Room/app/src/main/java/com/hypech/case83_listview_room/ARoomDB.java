package com.hypech.case83_listview_room;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {AUser.class, AMessage.class}, version = 1, exportSchema = false)
public abstract class ARoomDB extends RoomDatabase {
    private static final String LOG_TAG = ARoomDB.class.getSimpleName();
    private static final String DATABASE_NAME = "aRoomDB";

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static ARoomDB sInstance;
    public  static synchronized ARoomDB getDatabase(Context context) {
        if (sInstance == null) {
            Log.d(LOG_TAG, "Creating new database instance");
            sInstance = Room.databaseBuilder(context.getApplicationContext(), ARoomDB.class, ARoomDB.DATABASE_NAME)
                    .addCallback(sRoomDatabaseCallback)
                    .allowMainThreadQueries()       //in first step, we allow Main Thread update
                    .build();
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract AMessageDao getMessageDao();
    public abstract AUserDao getUserDao();          //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.

    private static Callback sRoomDatabaseCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                AUserDao userDao = sInstance.getUserDao();
                AUser u1, u2, u3, u4, u5;
                u1 = new AUser("见声", "18018018018", "sv180180", "NoAvatar");
                u2 = new AUser("室内", "18018018028", "sv180180", "NoAvatar");
                u3 = new AUser("室外", "18018018038", "sv180180", "NoAvatar");
                u4 = new AUser("嘈杂", "18018018068", "sv180180", "NoAvatar");
                u5 = new AUser("一般环境", "18018018088", "sv180180", "NoAvatar");
                userDao.insert(u1);
                userDao.insert(u2);
                userDao.insert(u3);
                userDao.insert(u4);
                userDao.insert(u5);

                AMessageDao messageDao = sInstance.getMessageDao();
                AMessage msg1 = new AMessage("5","S", "htis is the thx.htis is the thxhtis is  the thxsir.", "Home");
                AMessage msg2 = new AMessage("6","R", "6  is the thxhtis is the thxsir.", "Home");
                messageDao.insert(msg1);
                messageDao.insert(msg2);
            });
        }
    };
}
package com.hypech.case84_user_room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Entry类-->Dao-->EntryRoomDatabase(单例模式)-->Repository(从EntryRoomDatabase获得Dao)-->
// 在Repository中实现异步-->AndroidViewModel通过Repository访问数据库CRUD
@Database(entities={User.class}, version =1, exportSchema = false)
public abstract class UserDB extends RoomDatabase {
    public abstract UserDao getUserDao();
    private static UserDB INSTANCE;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    static synchronized UserDB getDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UserDB.class, "uChatDB")
                    .addCallback(sRoomDatabaseCallback)
                    .allowMainThreadQueries()       //in first step, we allow Main Thread update
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                User user = new User("18018018018", "seeingVoice", "18018018018", "js180180", "NoAvatar");

                UserDao dao = INSTANCE.getUserDao();
                dao.insert(user);
            });
        }
    };
}
package com.reapex.sv.entitydaodb;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Chat.class}, version = 1, exportSchema = false)
public abstract class UChatDB extends RoomDatabase {

    public abstract UserDao getUserDao();          //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.
    public abstract ChatDao getChatDao();          //Define the DAOs that work with the database. Provide an abstract "getter" method for each @Dao.
    private static volatile UChatDB INSTANCE;   //Create the WordRoomDatabase as a singleton

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //    public static RoomDb getDatabase(Context context) {
    public static synchronized UChatDB getDatabase(Context context){
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), UChatDB.class, "UChatDB")
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
                UserDao dao = INSTANCE.getUserDao();
                User user = new User("18018018018", "seeingVoice", "18018018018", "js180180", "NoAvatar");
                dao.insert(user);
            });
        }
    };

/*            if(db!=null){
        if(db.isOpen()) {
            db.close();
        }
        db=null;
    }
*/
}
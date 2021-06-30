package com.hypech.case83_room;

import android.content.Context;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {L1_Entity.class}, version = 1, exportSchema = false)
public abstract class L3_RoomDB extends RoomDatabase {

    public abstract L2_Dao wordDao();

    private static volatile L3_RoomDB INSTANCE;

    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static L3_RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactsContract.Data.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            L3_RoomDB.class, "word_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Override the onCreate method to populate the database.
     * For this sample, we clear the database every time it is created.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // If you want to keep data through app restarts,
            // comment out the following block
            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                // If you want to start with more words, just add them.
                L2_Dao dao = INSTANCE.wordDao();
                dao.deleteAll();

                L1_Entity word = new L1_Entity("Hello");
                dao.insert(word);

                word = new L1_Entity("World");
                dao.insert(word);
            });
        }
    };
}
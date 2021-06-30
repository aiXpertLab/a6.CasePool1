package com.hypech.case83_room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface L2_Dao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(L1_Entity word);

    @Query("DELETE FROM L1_Entity")
    void deleteAll();

    @Query("SELECT * FROM L1_Entity ORDER BY word ASC")
    LiveData<List<L1_Entity>> getAlphabetizedWords();
}
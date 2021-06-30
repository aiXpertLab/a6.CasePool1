package com.hypech.case83_listview_room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AMessageDao {
    @Insert
    void insert(AMessage amessage);

    @Update
    void update(AMessage amessage);

    @Delete
    void delete(AMessage amessage);

    @Query("DELETE FROM AMessage")
    void deleteAll();

    @Query("SELECT * FROM AMESSAGE ORDER BY ID DESC")
    List<AMessage> getAll();
}
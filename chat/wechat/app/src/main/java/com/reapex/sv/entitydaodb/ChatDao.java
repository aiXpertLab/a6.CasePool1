package com.reapex.sv.entitydaodb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDao {
    @Insert
    void insert(Chat chat);

    @Update
    void update(Chat chat);

    @Delete
    void delete(Chat chat);

    @Query("DELETE FROM Chat")
    void deleteAll();

    @Query("SELECT * FROM Chat ORDER BY ID DESC")
    List<Chat> getAll();

    @Query("SELECT * FROM Chat WHERE fromUserId =:fromUserId")
    List<Chat> getChatList(String fromUserId);
}

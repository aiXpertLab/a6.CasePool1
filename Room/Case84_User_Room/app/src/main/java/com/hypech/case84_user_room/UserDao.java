package com.hypech.case84_user_room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM USER")
    void deleteAll();

    @Query("SELECT * FROM USER ORDER BY ID DESC")
    List<User> getAllUsers();
    // LiveData<List<Word>> getAllWordsLive();

    @Query("SELECT * FROM User ORDER BY UserId ASC")
    LiveData<List<User>> getAllUsersLiveData();
}

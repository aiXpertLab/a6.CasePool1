package com.reapex.sv.entitydaodb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM USER")
    void deleteAll();

    @Query("SELECT * FROM User ORDER BY UserId ASC")
    LiveData<List<User>> getAllUsersLiveData();

    @Query("SELECT * FROM USER ORDER BY id DESC")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE UserPhone=:userPhone")
    List<User> getUserListFromPhone(String userPhone);

    @Query("SELECT * FROM user WHERE UserPhone=:userPhone")
    User getUserByPhone(String userPhone);
}
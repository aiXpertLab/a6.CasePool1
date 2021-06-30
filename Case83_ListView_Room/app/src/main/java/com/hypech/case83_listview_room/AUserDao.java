package com.hypech.case83_listview_room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AUser user);

    @Delete
    void delete(AUser user);

    @Query("SELECT * FROM AUSER ORDER BY id DESC")
    List<AUser> getAllUsers();

    @Query("SELECT * FROM Auser WHERE UserPhone=:userPhone")
    List<AUser> getUserByPhone(String userPhone);

    @Query("SELECT * FROM Auser WHERE UserId=:userId")
    List<AUser> getUserById(String userId);

    @Query("SELECT * FROM Auser WHERE IsFriend=:isFriend")
    List<AUser> getFriendList(Boolean isFriend);
}

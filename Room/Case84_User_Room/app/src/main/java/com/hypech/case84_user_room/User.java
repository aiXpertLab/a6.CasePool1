package com.hypech.case84_user_room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String UserId;
    private String UserName;
    private String UserPhone;
    private String UserPassword;
    private String UserAvatar;

    public User(String UserId, String UserName, String UserPhone, String UserPassword, String UserAvatar) {
        this.UserId = UserPhone;
        this.UserName = UserName;
        this.UserPhone = UserPhone;
        this.UserPassword = UserPassword;
        this.UserAvatar = UserAvatar;
    }

    public int    getId(){ return id;}
    public String getUserId() {        return UserId;    }
    public String getUserName() {        return this.UserName;    }
    public String getUserPhone() {        return this.UserPhone;    }
    public String getUserPassword() {        return this.UserPassword;    }
    public String getUserAvatar() {        return this.UserAvatar;    }

    public void setId(int id){               this.id = id;    }
    public void setUserId(String UserId) {        this.UserId = UserId;    }
    public void setUserName(String UserName) {      this.UserName = UserName;    }
    public void setUserPhone(String UserPhone) {     this.UserPhone = UserPhone;    }
    public void setUserPassword(String UserPassword) {  this.UserPassword = UserPassword;    }
    public void setUserAvatar(String UserAvatar) {    this.UserAvatar = UserAvatar;    }
}
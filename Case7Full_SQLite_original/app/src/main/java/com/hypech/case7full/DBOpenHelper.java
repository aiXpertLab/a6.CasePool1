package com.hypech.case7full;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBOpenHelper extends SQLiteOpenHelper {
    /**
     * Declare SQLite type variable: db
     */
    private final SQLiteDatabase db;

    /**
     * db_test.db is the database name. There could be many tables in db_test.
     * set db to read/write status. App won't do anything till getReadableDatabase()
     */
    DBOpenHelper(Context context){
        super(context,"db_test.db",null,1);
        db = getReadableDatabase();
    }

    /**
     * onCreate() and onUpgrade() are two abstracts must override.
     */
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "password TEXT," +
                "email TEXT," +
                "phonenum TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }

    void add(String name, String password,String email,String phonenum){
        db.execSQL("INSERT INTO user (name,password,email,phonenum) VALUES(?,?,?,?)",new Object[]{name,password,email,phonenum});
    }
    public void delete(String name,String password){
        db.execSQL("DELETE FROM user WHERE name = AND password ="+name+password);
    }
    public void updata(String password){
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }

    public Cursor getAllUsers() {
        return db.rawQuery("SELECT * FROM user", null);
    }

    /**
     * Drag cursor to loop all users, and put them into Arraylist
     * Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");
     */
    ArrayList<UserBean> getAllData(){
        ArrayList<UserBean> list = new ArrayList<UserBean>();
        Cursor cursor = getAllUsers();
        while(cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String phonenum = cursor.getString(cursor.getColumnIndex("phonenum"));
            String password = cursor.getString(cursor.getColumnIndex("password"));

            list.add(new UserBean(name,password,email,phonenum));
        }
        return list;
    }
}


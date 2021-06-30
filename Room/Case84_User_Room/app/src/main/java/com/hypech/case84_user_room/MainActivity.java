package com.hypech.case84_user_room;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    UserDB userDB;
    UserDao userDao;
    Button buttonInsert, buttonClear;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userDB = UserDB.getDatabase(this);
        userDao = userDB.getUserDao();

        buttonInsert = findViewById(R.id.buttonInsert);
        buttonClear = findViewById(R.id.buttonClear);
        textView = findViewById(R.id.textViewHW);

        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                User user1 = new User("28018018018", "2seeingVoice", "28018018018", "js180180", "NoAvatar");
                userDao.insert(user1);
                updateView();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userDao.deleteAll();
                updateView();
            }
        });
    }

    void updateView(){
        List<User> list = userDao.getAllUsers();
        String text = "";
        for(int i = 0; i < list.size(); i++){
            User user = list.get(i);
            text += user.getUserId() +":"+user.getUserName()+"="+user.getUserPhone()+"\n";
        }
        textView.setText(text);
    }
}
package com.hypech.case16_listview_chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView msgListView;
    private EditText inputText;
    private Button send;

    private ChatsAdapter adapter;
    private List<ChatData> msgList = new ArrayList<ChatData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText   = findViewById(R.id.input_text);
        send        = findViewById(R.id.send);
        msgListView = findViewById(R.id.msg_list_view);

        initMsgs();         //feed the data
        adapter = new ChatsAdapter(MainActivity.this, R.layout.chat_items, msgList);
        msgListView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String content = inputText.getText().toString();
                if (content != null && !"".equals(content)) {
                    ChatData msg = new ChatData(content, ChatData.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged();             // refresh ListView when new messages coming
                    msgListView.setSelection(msgList.size());   // go to the end of the ListView
                    inputText.setText("");
                }
            }
        });
    }

    private void initMsgs() {
        ChatData msg1 = new ChatData("Are you OK? Dan.", ChatData.TYPE_RECEIVED);
        msgList.add(msg1);
        ChatData msg2 = new ChatData("Hi, It's Dan. Who are you?", ChatData.TYPE_SENT);
        msgList.add(msg2);
        ChatData msg3 = new ChatData("I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)", ChatData.TYPE_RECEIVED);
        msgList.add(msg3);
        ChatData msg4 = new ChatData("Nice to meet you, boss :( I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)I'm Lei Jun, your boss :)", ChatData.TYPE_SENT);
        msgList.add(msg4);
    }
}
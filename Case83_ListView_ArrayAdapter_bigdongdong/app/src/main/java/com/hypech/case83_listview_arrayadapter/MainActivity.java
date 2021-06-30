package com.hypech.case83_listview_arrayadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText inputText;
    Button btnSend;

    AAdapter adapter;
    AMessage msg1, msg2, msg3, msg4, msg;
    List<AMessage> msgList = new ArrayList<AMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs();         //feed the data

        inputText= findViewById(R.id.edit_text);
        btnSend  = findViewById(R.id.button_send);
        listView = findViewById(R.id.list_view);

        adapter = new AAdapter(MainActivity.this, R.layout.a_item_sent_text, msgList);
        listView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = inputText.getText().toString();
                if (content != null && !"".equals(content)) {
                    msg = new AMessage("dd","S", content, "Home");
//                    msgList.add(msg);
                    int k = msgList.size();
                    msgList.set(k-1, msg);
                    adapter.notifyDataSetChanged();             // refresh ListView when new messages coming

                    listView.setSelection(msgList.size());   // go to the end of the ListView



                    inputText.setText("");
                }
            }
        });
    }

    private void initMsgs() {
        msg1 = new AMessage("1","R", "Are you OK? Dan.", "Home");
        msg2 = new AMessage("2","S", "I am good. WHo r u?", "Home");
        msg3 = new AMessage("3","R", "your boss, jun lei.", "Home");
        msg4 = new AMessage("4","S", "thx.sir.", "Home");

        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);
        msgList.add(msg4);
    }

}
package com.hypech.case84_webview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv);
        Button bt = findViewById(R.id.button);
        tv.setOnClickListener(myListener);
        bt.setOnClickListener(myListener);
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv) {
                Intent intent = new Intent(v.getContext(), Privacy.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(v.getContext(), ABar.class);
                startActivity(intent);
            }
        }
    };
}
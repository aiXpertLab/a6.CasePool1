package com.hypech.case82_interface_callback_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Act1 extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    TextView tv;
    Act2 = mAct2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.buttonsv);
        tv = findViewById(R.id.textview);

        btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonsv) {
            tv.setText("ddd");
            mAct2 = new RecognizerSV(this);
        }
    }

}
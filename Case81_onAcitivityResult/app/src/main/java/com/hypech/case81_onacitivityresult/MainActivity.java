package com.hypech.case81_onacitivityresult;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final int REQUEST_CODE_B = 1;
    public final int REQUEST_CODE_C = 2;

    public TextView tt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_b = (Button) findViewById(R.id.btn_tob);
        tt = (TextView)findViewById(R.id.tt);

        btn_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Activity_b.class);
                startActivityForResult(intent,REQUEST_CODE_B);
                //REQUEST_CODE用于辨别调用的是那个Activity
            }
        });

        Button btn_c = (Button) findViewById(R.id.btn_toc);
        btn_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, Activity_b.class);
                startActivityForResult(intent,REQUEST_CODE_C);
                //REQUEST_CODE用于辨别调用的是那个Activity
            }
        });
    }

    /**
     * requestCode和startActivityForResult中的requestCode相对应
     * resultCode和Intent是由子Activity通过其setResult()方法返回
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int i =1;
        switch (requestCode) {
            case REQUEST_CODE_B: //返回的结果是来自于Activity B
                if (resultCode == Activity.RESULT_OK) {
                    tt.setText(data.getStringExtra("respond"));
                } else {
                    tt.setText("What?Nobody? B");
                }
                break;
            case REQUEST_CODE_C: //返回的结果是来自于Activity C
                if (resultCode == Activity.RESULT_OK) {
                    tt.setText(data.getStringExtra("respond"));
                } else {
                    tt.setText("What?Nobody C?");
                }
                break;
            default:
                break;
        }
    }
}
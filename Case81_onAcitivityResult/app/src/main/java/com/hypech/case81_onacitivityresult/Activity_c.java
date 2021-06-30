package com.hypech.case81_onacitivityresult;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity_c extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);

        Button btn = (Button) findViewById(R.id.btn_CtoA);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将先要传回的数据放到Intent里
                // 可以用putExtra()的方法，也可以用setXXX()的方法
                Intent intent = new Intent();
                intent.putExtra("respond", "Hello,Alice!I'm Cob.");
                // 设置返回码和返回携带的数据
                setResult(Activity.RESULT_OK, intent);
                // RESULT_OK就是一个默认值，=-1，它说OK就OK吧
                finish();
            }
        });
    }
}
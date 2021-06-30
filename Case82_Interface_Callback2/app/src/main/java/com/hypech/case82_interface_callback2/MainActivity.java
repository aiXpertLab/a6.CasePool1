package com.hypech.case82_interface_callback2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView imgv;
    private Button downImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgv = (ImageView) findViewById(R.id.imageView);
        downImgBtn = (Button) findViewById(R.id.button);
        //点击按钮去下载
        downImgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //从接口回调得到数据,因为下载是在子线程中，所以这里要变到主线程中设置图片
        new SubCallee(new LeoInterface() {
            @Override
            public void result(final Bitmap bm) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imgv.setImageBitmap(bm);
                    }
                });
            }
        });
    }
}
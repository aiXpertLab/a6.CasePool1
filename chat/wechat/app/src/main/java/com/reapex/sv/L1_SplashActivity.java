package com.reapex.sv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.reapex.sv.user.L2_LoginActivity;
import com.reapex.sv.user.L2_RegisterActivity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import me.leolin.shortcutbadger.ShortcutBadger;


public class L1_SplashActivity extends FragmentActivity implements View.OnClickListener {
    private static final int sleepTime = 500;

    Button mLoginBtn;
    Button mRegisterBtn;

    private static final int SHOW_OPERATE_BTN = 0x3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final View view = View.inflate(this, R.layout.activity_splash, null);
        super.onCreate(savedInstanceState);
        setContentView(view);

        initView();

        L0_SharedPreferences.getInstance().init(this);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        view.setAnimation(animation);

        ShortcutBadger.removeCount(this);
    }

    private void initView() {
        mLoginBtn = findViewById(R.id.btn_login);
        mRegisterBtn = findViewById(R.id.btn_register);

        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 预置地址信息
                    // 初始化省市区
//                    AreaUtil.initArea(L1_SplashActivity.this);
//                    RegionUtil.initRegion(L1_SplashActivity.this);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (L0_SharedPreferences.getInstance().isLogin()) {
                    // 已登录，跳至主页面
                    startActivity(new Intent(L1_SplashActivity.this, L4_MainActivity.class));
                    finish();
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(SHOW_OPERATE_BTN));
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(L1_SplashActivity.this, L2_LoginActivity.class));
                break;
            case R.id.btn_register:
                startActivity(new Intent(L1_SplashActivity.this, L2_RegisterActivity.class));
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_OPERATE_BTN) {
                mLoginBtn.setVisibility(View.VISIBLE);
                mRegisterBtn.setVisibility(View.VISIBLE);
            }
        }
    };
}

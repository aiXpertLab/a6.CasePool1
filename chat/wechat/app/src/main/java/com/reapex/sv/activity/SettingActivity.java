package com.reapex.sv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.L1_SplashActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_SharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 *
 * @author zhou
 */
public class SettingActivity extends L0_BaseActivity {
    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView mTitleTv;

    /**
     * 账号与安全
     */
    @BindView(R.id.rl_account_security)
    RelativeLayout mAccountSecurityRl;

    /**
     * 关于微信
     */
    @BindView(R.id.rl_about)
    RelativeLayout mAboutRl;

    /**
     * 退出
     */
    @BindView(R.id.rl_log_out)
    RelativeLayout mLogOutRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initStatusBar();
        L0_SharedPreferences.getInstance().init(this);
        initView();
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);
    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.rl_account_security, R.id.rl_about, R.id.rl_log_out})
    public void onClick(View view) {
        switch (view.getId()) {
            // 账号与安全
            case R.id.rl_account_security:
                startActivity(new Intent(SettingActivity.this, AccountSecurityActivity.class));
                break;

            // 关于微信
            case R.id.rl_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;

            case R.id.rl_log_out:
                // 清除sharedpreferences中存储信息
                L0_SharedPreferences.getInstance().setLogin(false);
                L0_SharedPreferences.getInstance().setUser(null);

                // 清除通讯录
//                L_UserBean.deleteAll(L_UserBean.class);
                // 清除朋友圈
//                FriendsCircle.deleteAll(FriendsCircle.class);

                // 跳转至登录页
                Intent intent = new Intent(this, L1_SplashActivity.class);
                startActivity(intent);
                finish();

                break;
        }
    }
}

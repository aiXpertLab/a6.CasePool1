package com.reapex.sv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账号与安全
 *
 * @author zhou
 */
public class AccountSecurityActivity extends L0_BaseActivity {

    /**
     * 标题
     */
    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.tv_wechat_id)
    TextView mWeChatIdTv;

    @BindView(R.id.tv_phone)
    TextView mPhoneTv;

    /**
     * 手机号
     */
    @BindView(R.id.rl_phone)
    RelativeLayout mPhoneRl;

    /**
     * 微信密码
     */
    @BindView(R.id.rl_password)
    RelativeLayout mPasswordRl;

    /**
     * 登录设备管理
     */
    @BindView(R.id.rl_manage_devices)
    RelativeLayout mManageDevicesRl;

    /**
     * 更多安全设置
     */
    @BindView(R.id.rl_more_settings)
    RelativeLayout mMoreSettingRl;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        ButterKnife.bind(this);

        mUser = L0_SharedPreferences.getInstance().getUser();
        initView();
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.rl_phone, R.id.rl_password, R.id.rl_manage_devices, R.id.rl_more_settings})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_phone:
                // 绑定手机号
                startActivity(new Intent(AccountSecurityActivity.this, PhoneLinkActivity.class));
                break;

            case R.id.rl_password:
                // 设置密码
                startActivity(new Intent(AccountSecurityActivity.this, ModifyPasswordActivity.class));
                break;

            case R.id.rl_manage_devices:
                // 登录设备管理
                startActivity(new Intent(AccountSecurityActivity.this, ManageDevicesActivity.class));
                break;

            case R.id.rl_more_settings:
                // 更多安全设置
                startActivity(new Intent(AccountSecurityActivity.this, MoreSecuritySettingActivity.class));
                break;
        }
    }
}

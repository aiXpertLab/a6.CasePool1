package com.reapex.sv.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * "用户信息"-"更多信息"
 *
 * @author zhou
 */
public class UserInfoMoreActivity extends L0_BaseActivity {
    @BindView(R.id.tv_whats_up)
    TextView mWhatsUpTv;

    @BindView(R.id.tv_from)
    TextView mFromTv;

    UserDao mUserDao;
    private String mContactId;
    private User mContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_more);
        ButterKnife.bind(this);
        initStatusBar();

        initData();
        initView();
    }

    public void back(View view) {
        finish();
    }

    private void initData() {
    }

    private void initView() {

        }
    }


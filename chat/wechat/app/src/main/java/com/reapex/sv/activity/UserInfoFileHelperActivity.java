package com.reapex.sv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.L6_ChatActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.utils.StatusBarUtil;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件传输助手
 *
 * @author zhou
 */
public class UserInfoFileHelperActivity extends L0_BaseActivity {

    @BindView(R.id.rl_send_message)
    RelativeLayout mSendMessageRl;

    private UserDao mUserDao;
    private String mContactId;
    private User contact;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_file_helper);
        ButterKnife.bind(this);

        initStatusBar();
        StatusBarUtil.setStatusBarColor(UserInfoFileHelperActivity.this, R.color.status_bar_color_white);

//sv          mUserDao = new UserDao();
        initView();
    }

    private void initView() {
        mContactId = getIntent().getStringExtra("userId");
//sv        contact = mUserDao.getUserById(mContactId);
    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.rl_send_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_send_message:
                Intent intent = new Intent(UserInfoFileHelperActivity.this, L6_ChatActivity.class);
                intent.putExtra("targetType", L0_Constant.TARGET_TYPE_SINGLE);
                intent.putExtra("contactId", mContactId);
    //sv              intent.putExtra("contactNickName", contact.getUserName());
                intent.putExtra("contactAvatar", contact.getUserAvatar());
                startActivity(intent);
                break;
        }
    }
}

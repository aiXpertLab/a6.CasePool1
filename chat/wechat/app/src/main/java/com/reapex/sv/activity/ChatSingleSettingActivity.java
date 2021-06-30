package com.reapex.sv.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.daodb.MessageDao;
import com.reapex.sv.widget.ConfirmDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.annotation.Nullable;

/**
 * 单聊设置
 *
 * @author zhou
 */
public class ChatSingleSettingActivity extends L0_BaseActivity implements View.OnClickListener {

    private TextView mTitleTv;
    private TextView mNickNameTv;
    private SimpleDraweeView mAvatarSdv;
    private RelativeLayout mAddUserToGroupRl;

    private RelativeLayout mClearRl;

    String userId;
    String userNickName;
    String userAvatar;

    MessageDao messageDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat_setting);
        //initStatusBar();

        userId = getIntent().getStringExtra("userId");
        userNickName = getIntent().getStringExtra("userNickName");
        userAvatar = getIntent().getStringExtra("userAvatar");

        messageDao = new MessageDao();
        initView();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

        mNickNameTv = findViewById(R.id.tv_nick_name);
        mAvatarSdv = findViewById(R.id.sdv_avatar);
        mAddUserToGroupRl = findViewById(R.id.rl_add_user_to_group);

        mClearRl = findViewById(R.id.rl_clear);

        mNickNameTv.setText(userNickName);
        if (!TextUtils.isEmpty(userAvatar)) {
            mAvatarSdv.setImageURI(Uri.parse(userAvatar));
        }

        mAddUserToGroupRl.setOnClickListener(this);
        mClearRl.setOnClickListener(this);
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_user_to_group:
                Intent intent = new Intent(this, CreateGroupActivity.class);
                intent.putExtra("createType", L0_Constant.CREATE_GROUP_TYPE_FROM_SINGLE);
                intent.putExtra("userId", userId);
                intent.putExtra("userNickName", userNickName);
                startActivity(intent);
                break;

            case R.id.rl_clear:
                final ConfirmDialog clearConfirmDialog = new ConfirmDialog(this, "",
                        "确定删除和" + userNickName + "的聊天记录吗?", "清空", "");
                clearConfirmDialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
                    @Override
                    public void onOkClick() {
                        // 清除本地message
                        messageDao.deleteMessageByUserId(userId);
                        // 清除会话消息(极光)

                        clearConfirmDialog.dismiss();
                    }

                    @Override
                    public void onCancelClick() {
                        clearConfirmDialog.dismiss();
                    }
                });
                // 点击空白处消失
                clearConfirmDialog.setCancelable(true);
                clearConfirmDialog.show();
                break;
        }
    }
}

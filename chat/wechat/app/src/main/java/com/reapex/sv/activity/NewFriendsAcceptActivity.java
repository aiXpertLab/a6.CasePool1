package com.reapex.sv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.daodb.FriendApplyDao;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.FriendApply;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.StatusBarUtil;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 好友申请
 *
 * @author zhou
 */
public class NewFriendsAcceptActivity extends L0_BaseActivity {
    @BindView(R.id.ll_nick_name)
    LinearLayout mNickNameLl;

    @BindView(R.id.tv_nick_name)
    TextView mNickNameTv;

    @BindView(R.id.tv_name)
    TextView mNameTv;

    @BindView(R.id.sdv_avatar)
    SimpleDraweeView mAvatarSdv;

    @BindView(R.id.iv_sex)
    ImageView mSexIv;

    @BindView(R.id.tv_from)
    TextView mFromTv;

    // 朋友圈图片
    private SimpleDraweeView mMomentsPhoto1Sdv;
    private SimpleDraweeView mMomentsPhoto2Sdv;
    private SimpleDraweeView mMomentsPhoto3Sdv;
    private SimpleDraweeView mMomentsPhoto4Sdv;

    VolleyUtil mVolleyUtil;
    FriendApplyDao mFriendApplyDao;
    UserDao mUserDao;
    FriendApply mFriendApply;
    LoadingDialog mDialog;
    User mContact;
    User mUser;
    String mContactId;
    String mContactFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_accept);
        ButterKnife.bind(this);
        initStatusBar();
        StatusBarUtil.setStatusBarColor(NewFriendsAcceptActivity.this, R.color.status_bar_color_white);

        mUser = L0_SharedPreferences.getInstance().getUser();
        mDialog = new LoadingDialog(this);
        mVolleyUtil = VolleyUtil.getInstance(this);
        mFriendApplyDao = new FriendApplyDao();
        initView();
    }

    private void initView() {
        final String applyId = getIntent().getStringExtra("applyId");
        mFriendApply = mFriendApplyDao.getFriendApplyByApplyId(applyId);
        mContactId = mFriendApply.getFromUserId();
//sv        mContact = mUserDao.getUserById(mContactId);
        mContactFrom = mFriendApply.getFromUserFrom();
//sv        mContact.setUserContactFrom(mContactFrom);

        loadData(mContact);
        getContactFromServer(mUser.getUserId(), mContactId);
    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.iv_setting, R.id.rl_go_confirm})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_setting:
                intent = new Intent(NewFriendsAcceptActivity.this, UserSettingActivity.class);
                intent.putExtra("contactId", mContactId);
                intent.putExtra("isFriend", L0_Constant.IS_NOT_FRIEND);
                startActivity(intent);
                break;
            case R.id.rl_go_confirm:
                intent = new Intent(NewFriendsAcceptActivity.this, NewFriendsAcceptConfirmActivity.class);
                intent.putExtra("applyId", mFriendApply.getApplyId());
                startActivity(intent);
                break;
        }
    }


    private void loadData(User user) {
    }

    /**
     * 从服务器获取用户最新信息
     *
     * @param userId 用户ID
     */
    public void getContactFromServer(final String userId, final String contactId) {
        String url = L0_Constant.BASE_URL + "users/" + userId + "/contacts/" + contactId;

        mVolleyUtil.httpGetRequest(url, response -> {
            User user = JSON.parseObject(response, User.class);
            loadData(user);
        }, volleyError -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContactFromServer(mUser.getUserId(), mContactId);
    }
}

package com.reapex.sv.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.StatusBarUtil;
import com.reapex.sv.utils.VolleyUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 陌生人用户详情页
 *
 * @author zhou
 */
public class UserInfoStrangerActivity extends L0_BaseActivity {
    @BindView(R.id.sdv_avatar)
    SimpleDraweeView mAvatarSdv;

    @BindView(R.id.tv_name)
    TextView mNameTv;

    @BindView(R.id.tv_nick_name)
    TextView mNickNameTv;

    @BindView(R.id.iv_sex)
    ImageView mSexIv;

    @BindView(R.id.tv_whats_up)
    TextView mWhatsUpTv;

    @BindView(R.id.tv_from)
    TextView mFromTv;

    // 标签
    @BindView(R.id.rl_tags)
    RelativeLayout mTagsRl;

    @BindView(R.id.tv_tags)
    TextView mTagsTv;

    // 描述
    @BindView(R.id.rl_desc)
    RelativeLayout mDescRl;

    @BindView(R.id.tv_desc)
    TextView mDescTv;

    @BindView(R.id.rl_whats_up)
    RelativeLayout mWhatsUpRl;

    @BindView(R.id.rl_edit_contact)
    RelativeLayout mEditContactRl;

    @BindView(R.id.ll_nick_name)
    LinearLayout mNickNameLl;

    @BindView(R.id.rl_add)
    RelativeLayout mAddRl;

    private UserDao mUserDao;
    private VolleyUtil mVolleyUtil;
    private User mUser;
    private String mContactId;
    private User mContact;
    private String mFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_stranger);
        ButterKnife.bind(this);

        initStatusBar();
        StatusBarUtil.setStatusBarColor(UserInfoStrangerActivity.this, R.color.status_bar_color_white);

//sv         mUserDao = new UserDao();
        mVolleyUtil = VolleyUtil.getInstance(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        initView();
    }

    private void initView() {
        mContactId = getIntent().getStringExtra("contactId");
        mFrom = getIntent().getStringExtra("from");
        // 加载本地数据
//sv         mContact = mUserDao.getUserById(mContactId);
        loadData(mContact);
        // 加载服务器最新数据并保存至本地
        getContactFromServer(mUser.getUserId(), mContactId);
    }

    @OnClick({R.id.iv_setting, R.id.sdv_avatar, R.id.rl_edit_contact, R.id.rl_tags, R.id.rl_desc, R.id.rl_add})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_setting:
                intent = new Intent(UserInfoStrangerActivity.this, UserSettingActivity.class);
                intent.putExtra("contactId", mContactId);
                intent.putExtra("isFriend", L0_Constant.IS_NOT_FRIEND);
                startActivity(intent);
                break;
            case R.id.sdv_avatar:
                intent = new Intent(UserInfoStrangerActivity.this, BigImageActivity.class);
                intent.putExtra("imgUrl", mContact.getUserAvatar());
                startActivity(intent);
                break;
            case R.id.rl_edit_contact:
            case R.id.rl_tags:
            case R.id.rl_desc:
                intent = new Intent(UserInfoStrangerActivity.this, EditContactActivity.class);
                intent.putExtra("contactId", mContactId);
                intent.putExtra("isFriend", L0_Constant.IS_NOT_FRIEND);
                startActivity(intent);
                break;
            case R.id.rl_add:
                intent = new Intent(UserInfoStrangerActivity.this, NewFriendsApplyConfirmActivity.class);
                intent.putExtra("contactId", mContactId);
                intent.putExtra("from", mFrom);
                startActivity(intent);
                break;

        }
    }

    public void back(View view) {
        finish();
    }

    // 渲染数据
    private void loadData(User user) {
        if (!TextUtils.isEmpty(user.getUserAvatar())) {
            mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
        }

        if (L0_Constant.USER_SEX_MALE.equals(user.getUserName())) {
            mSexIv.setImageResource(R.mipmap.icon_sex_male);
        } else if (L0_Constant.USER_SEX_FEMALE.equals(user.getUserName())) {
            mSexIv.setImageResource(R.mipmap.icon_sex_female);
        } else {
            mSexIv.setVisibility(View.GONE);
        }


        if (L0_Constant.CONTACTS_FROM_PHONE.equals(mFrom)) {
            mFromTv.setText(getString(R.string.search_friend_by_phone));
        } else if (L0_Constant.CONTACTS_FROM_WX_ID.equals(mFrom)) {
            mFromTv.setText(getString(R.string.search_friend_by_wx_id));
        } else if (L0_Constant.CONTACTS_FROM_PEOPLE_NEARBY.equals(mFrom)) {
            mFromTv.setText(getString(R.string.search_friend_by_people_nearby));
        } else if (L0_Constant.CONTACTS_FROM_CONTACT.equals(mFrom)) {
            mFromTv.setText(getString(R.string.search_friend_by_contact));
        }

        // 标签

        // 描述
        if (!TextUtils.isEmpty(user.getUserName())) {
            mDescRl.setVisibility(View.VISIBLE);
            mDescTv.setText(user.getUserName());
        } else {
            mDescRl.setVisibility(View.GONE);
        }

        // 备注
        if (!TextUtils.isEmpty(user.getUserName())) {
            mNameTv.setText(user.getUserName());
            mNickNameLl.setVisibility(View.VISIBLE);
            mNickNameTv.setText("昵称：" + user.getUserName());
        } else {
            mNickNameLl.setVisibility(View.GONE);
            mNameTv.setText(user.getUserName());
        }
    }

    /**
     * 从服务器获取用户最新信息
     *
     * @param userId    用户ID
     * @param contactId 联系人用户ID
     */
    public void getContactFromServer(final String userId, final String contactId) {
        String url = L0_Constant.BASE_URL + "users/" + userId + "/contacts/" + contactId;
        mVolleyUtil.httpGetRequest(url, response -> {
            User user = JSON.parseObject(response, User.class);
//sv             mUserDao.saveUser(user);
            loadData(user);
        }, volleyError -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//sv         User contact = mUserDao.getUserById(mContactId);
//sv         loadData(contact);
        getContactFromServer(mUser.getUserId(), mContactId);
    }
}

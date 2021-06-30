package com.reapex.sv.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 联系人权限
 *
 * @author zhou
 */
public class ContactPrivacyActivity extends L0_BaseActivity {

    // 聊天、朋友圈、微信运动等
    @BindView(R.id.iv_chats_moments_werun_etc)
    ImageView mChatsMomentsWerunEtcIv;

    // 仅聊天
    @BindView(R.id.iv_chats_only)
    ImageView mChatsOnlyIv;

    // 权限(不看他，不让他看我)
    @BindView(R.id.ll_privacy)
    LinearLayout mPrivacyLl;

    // 不让他看我
    @BindView(R.id.iv_hide_my_posts)
    ImageView mHideMyPostsIv;

    // 可以看我
    @BindView(R.id.iv_show_my_posts)
    ImageView mShowMyPostsIv;

    // 不看他
    @BindView(R.id.iv_hide_his_posts)
    ImageView mHideHisPostsIv;

    // 看他
    @BindView(R.id.iv_show_his_posts)
    ImageView mShowHisPostsIv;

    UserDao mUserDao;
    String mContactId;
    User mContact;

    private VolleyUtil mVolleyUtil;
    LoadingDialog mDialog;
    User mUser;

    private String mRelaPrivacy;
    private String mRelaHideMyPosts;
    private String mRelaHideHisPosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_privacy);
        //initStatusBar();
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
//sv        mUserDao = new UserDao();
        mContactId = getIntent().getStringExtra("contactId");
//sv        mContact = mUserDao.getUserById(mContactId);

        mUser = L0_SharedPreferences.getInstance().getUser();
        mVolleyUtil = VolleyUtil.getInstance(this);
        mDialog = new LoadingDialog(ContactPrivacyActivity.this);

        //sv      mRelaPrivacy = mContact.getUserContactPrivacy();
        //sv mRelaHideHisPosts = mContact.getUserName();
        //sv mRelaHideMyPosts = mContact.getUserContactHideMyPosts();
    }

    private void initView() {
      /*  if (L0_Constant.PRIVACY_CHATS_MOMENTS_WERUN_ETC.equals(mContact.getUserContactPrivacy())) {
            // 所有权限
            mChatsMomentsWerunEtcIv.setVisibility(View.VISIBLE);
            mChatsOnlyIv.setVisibility(View.GONE);

            mPrivacyLl.setVisibility(View.VISIBLE);
            if (L0_Constant.HIDE_MY_POSTS.equals(mContact.getUserContactHideMyPosts())) {
                // 不让他看我
                mHideMyPostsIv.setVisibility(View.VISIBLE);
                mShowMyPostsIv.setVisibility(View.GONE);
            } else {
                // 让他看我
                mHideMyPostsIv.setVisibility(View.GONE);
                mShowMyPostsIv.setVisibility(View.VISIBLE);
            }

            if (L0_Constant.HIDE_HIS_POSTS.equals(mContact.getUserName())) {
                // 不看他
                mHideHisPostsIv.setVisibility(View.VISIBLE);
                mShowHisPostsIv.setVisibility(View.GONE);
            } else {
                // 看他
                mHideHisPostsIv.setVisibility(View.GONE);
                mShowHisPostsIv.setVisibility(View.VISIBLE);
            }
        } else {
            // 仅聊天
            mChatsMomentsWerunEtcIv.setVisibility(View.GONE);
            mChatsOnlyIv.setVisibility(View.VISIBLE);

            mPrivacyLl.setVisibility(View.GONE);
        }

*/
    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.rl_chats_moments_werun_etc, R.id.rl_chats_only,
            R.id.iv_hide_my_posts, R.id.iv_show_my_posts, R.id.iv_hide_his_posts, R.id.iv_show_his_posts})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_chats_moments_werun_etc:
                mRelaPrivacy = L0_Constant.PRIVACY_CHATS_MOMENTS_WERUN_ETC;

                mChatsMomentsWerunEtcIv.setVisibility(View.VISIBLE);
                mChatsOnlyIv.setVisibility(View.GONE);

                mPrivacyLl.setVisibility(View.VISIBLE);

//sv                updateContactPrivacy(mUser.getUserId(), mContactId,
//sv                        mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;
            case R.id.rl_chats_only:
                mRelaPrivacy = L0_Constant.PRIVACY_CHATS_ONLY;

                mChatsMomentsWerunEtcIv.setVisibility(View.GONE);
                mChatsOnlyIv.setVisibility(View.VISIBLE);

                mPrivacyLl.setVisibility(View.GONE);

                //sv updateContactPrivacy(mUser.getUserId(), mContactId,
//sv                        mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;

            case R.id.iv_hide_my_posts:
                mRelaHideMyPosts = L0_Constant.SHOW_MY_POSTS;

                mShowMyPostsIv.setVisibility(View.VISIBLE);
                mHideMyPostsIv.setVisibility(View.GONE);

//sv                updateContactPrivacy(mUser.getUserId(), mContactId,
//sv                        mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;
            case R.id.iv_show_my_posts:
                mRelaHideMyPosts = L0_Constant.HIDE_MY_POSTS;

                mShowMyPostsIv.setVisibility(View.GONE);
                mHideMyPostsIv.setVisibility(View.VISIBLE);

//sv                updateContactPrivacy(mUser.getUserId(), mContactId,
//sv                        mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;

            case R.id.iv_hide_his_posts:
                mRelaHideHisPosts = L0_Constant.SHOW_HIS_POSTS;

                mShowHisPostsIv.setVisibility(View.VISIBLE);
                mHideHisPostsIv.setVisibility(View.GONE);

//sv                updateContactPrivacy(mUser.getUserId(), mContactId,
//sv                        mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;
            case R.id.iv_show_his_posts:
                mRelaHideHisPosts = L0_Constant.HIDE_HIS_POSTS;

                mShowHisPostsIv.setVisibility(View.GONE);
                mHideHisPostsIv.setVisibility(View.VISIBLE);

                //sv            updateContactPrivacy(mUser.getUserId(), mContactId,                        mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;
        }
    }

    private void updateContactPrivacy(String userId, final String contactId,
                                      final String relaPrivacy, final String relaHideMyPosts, final String relaHideHisPosts) {
        mDialog.setMessage("正在处理...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        String url = L0_Constant.BASE_URL + "users/" + userId + "/userContactPrivacy";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("contactId", contactId);
        paramMap.put("relaPrivacy", relaPrivacy);
        paramMap.put("relaHideMyPosts", relaHideMyPosts);
        paramMap.put("relaHideHisPosts", relaHideHisPosts);

        mVolleyUtil.httpPutRequest(url, paramMap, s -> {
            mDialog.dismiss();
        }, volleyError -> {
            mDialog.dismiss();
        });
    }
}

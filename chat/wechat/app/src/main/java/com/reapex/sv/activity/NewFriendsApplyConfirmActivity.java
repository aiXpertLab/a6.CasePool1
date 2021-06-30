package com.reapex.sv.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.StatusBarUtil;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 申请添加朋友
 *
 * @author zhou
 */
public class NewFriendsApplyConfirmActivity extends L0_BaseActivity implements View.OnClickListener {

    // 申请备注
    @BindView(R.id.et_apply_remark)
    EditText mApplyRemarkEt;

    // 联系人备注
    @BindView(R.id.et_contact_alias)
    EditText mContactAliasEt;

    // 所有权限
    @BindView(R.id.rl_chats_moments_werun_etc)
    RelativeLayout mChatsMomentsWerunEtcRl;

    @BindView(R.id.iv_chats_moments_werun_etc)
    ImageView mChatsMomentsWerunEtcIv;

    // 仅聊天
    @BindView(R.id.rl_chats_only)
    RelativeLayout mChatsOnlyRl;

    @BindView(R.id.iv_chats_only)
    ImageView mChatsOnlyIv;

    // 权限(不看他，不让他看我)
    @BindView(R.id.rl_privacy_header)
    RelativeLayout mPrivacyHeaderRl;

    @BindView(R.id.rl_privacy)
    RelativeLayout mPrivacyRl;

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

    private User mUser;
    private UserDao mUserDao;
    private User mContact;
    private String mContactId;
    private String mFrom;

    private VolleyUtil mVolleyUtil;
    private LoadingDialog mDialog;

    private String mRelaPrivacy;
    private String mRelaHideMyPosts;
    private String mRelaHideHisPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends_apply_confirm);
        initStatusBar();
        StatusBarUtil.setStatusBarColor(NewFriendsApplyConfirmActivity.this, R.color.status_bar_color_white);

        ButterKnife.bind(this);

        mUser = L0_SharedPreferences.getInstance().getUser();
//sv         mUserDao = new UserDao();
        mVolleyUtil = VolleyUtil.getInstance(this);
        mDialog = new LoadingDialog(this);
        initView();
    }

    private void initView() {
        mRelaPrivacy = L0_Constant.PRIVACY_CHATS_MOMENTS_WERUN_ETC;
        mRelaHideMyPosts = L0_Constant.SHOW_MY_POSTS;
        mRelaHideHisPosts = L0_Constant.SHOW_HIS_POSTS;

        mFrom = getIntent().getStringExtra("from");
        mContactId = getIntent().getStringExtra("contactId");

        //sv mContact = mUserDao.getUserById(mContactId);
        mApplyRemarkEt.setText("我是" + mUser.getUserName());

        if (TextUtils.isEmpty(mContact.getUserName())) {
            mContactAliasEt.setText(mContact.getUserName());
        } else {
            mContactAliasEt.setText(mContact.getUserName());
        }
    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.rl_chats_moments_werun_etc, R.id.rl_chats_only,
            R.id.iv_hide_my_posts, R.id.iv_show_my_posts, R.id.iv_hide_his_posts, R.id.iv_show_his_posts, R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_chats_moments_werun_etc:
                mRelaPrivacy = L0_Constant.PRIVACY_CHATS_MOMENTS_WERUN_ETC;

                mChatsMomentsWerunEtcIv.setVisibility(View.VISIBLE);
                mChatsOnlyIv.setVisibility(View.GONE);

                mPrivacyHeaderRl.setVisibility(View.VISIBLE);
                mPrivacyRl.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_chats_only:
                mRelaPrivacy = L0_Constant.PRIVACY_CHATS_ONLY;

                mChatsMomentsWerunEtcIv.setVisibility(View.GONE);
                mChatsOnlyIv.setVisibility(View.VISIBLE);

                mPrivacyHeaderRl.setVisibility(View.GONE);
                mPrivacyRl.setVisibility(View.GONE);
                break;

            case R.id.iv_hide_my_posts:
                mRelaHideMyPosts = L0_Constant.SHOW_MY_POSTS;

                mShowMyPostsIv.setVisibility(View.VISIBLE);
                mHideMyPostsIv.setVisibility(View.GONE);
                break;
            case R.id.iv_show_my_posts:
                mRelaHideMyPosts = L0_Constant.HIDE_MY_POSTS;

                mShowMyPostsIv.setVisibility(View.GONE);
                mHideMyPostsIv.setVisibility(View.VISIBLE);
                break;

            case R.id.iv_hide_his_posts:
                mRelaHideHisPosts = L0_Constant.SHOW_HIS_POSTS;

                mShowHisPostsIv.setVisibility(View.VISIBLE);
                mHideHisPostsIv.setVisibility(View.GONE);
                break;
            case R.id.iv_show_his_posts:
                mRelaHideHisPosts = L0_Constant.HIDE_HIS_POSTS;

                mShowHisPostsIv.setVisibility(View.GONE);
                mHideHisPostsIv.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_send:
                mDialog.setMessage("正在发送...");
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                String applyRemark = mApplyRemarkEt.getText().toString();
                String relaContactAlias = mContactAliasEt.getText().toString();

                addFriendApply(applyRemark, mUser.getUserId(), mContactId,
                        mFrom, relaContactAlias, mRelaPrivacy, mRelaHideMyPosts, mRelaHideHisPosts);
                break;
            default:
                break;
        }
    }

    /**
     * 发送好友申请
     *
     * @param applyRemark      申请备注
     * @param fromUserId       请求人用户ID
     * @param toUserId         接收人用户ID
     * @param relaContactFrom  好友来源
     * @param relaContactAlias 联系人备注
     * @param relaPrivacy      朋友权限 "0":聊天、朋友圈、微信运动  "1":仅聊天
     * @param relaHideMyPosts  朋友圈和视频动态 "0":可以看我 "1":不让他看我
     * @param relaHideHisPosts 朋友圈和视频动态 "0":可以看他 "1":不看他
     */
    private void addFriendApply(String applyRemark, String fromUserId, String toUserId, String relaContactFrom, String relaContactAlias,
                                String relaPrivacy, String relaHideMyPosts, String relaHideHisPosts) {
        String url = L0_Constant.BASE_URL + "friendApplies";

        if (relaContactAlias.equals(mContact.getUserName())) {
            relaContactAlias = "";
        }

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("applyRemark", applyRemark);
        paramMap.put("fromUserId", fromUserId);
        paramMap.put("toUserId", toUserId);
        paramMap.put("relaContactFrom", relaContactFrom);
        paramMap.put("relaContactAlias", relaContactAlias);
        paramMap.put("relaPrivacy", relaPrivacy);
        paramMap.put("relaHideMyPosts", relaHideMyPosts);
        paramMap.put("relaHideHisPosts", relaHideHisPosts);

        mVolleyUtil.httpPostRequest(url, paramMap, response -> {
            mDialog.dismiss();
            Toast.makeText(NewFriendsApplyConfirmActivity.this, "已发送", Toast.LENGTH_SHORT).show();
            finish();
        }, volleyError -> {
            mDialog.dismiss();
            if (volleyError instanceof NetworkError) {
                Toast.makeText(NewFriendsApplyConfirmActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}

package com.reapex.sv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.EditDialog;
import com.reapex.sv.widget.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多安全设置
 *
 * @author zhou
 */
public class MoreSecuritySettingActivity extends L0_BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.rl_qq_id)
    RelativeLayout mQqIdRl;

    @BindView(R.id.rl_email)
    RelativeLayout mEmailRl;

    @BindView(R.id.tv_qq_is_linked)
    TextView mQqIdIsLinkedTv;

    @BindView(R.id.tv_email_is_linked)
    TextView mEmailIsLinkedTv;

    private VolleyUtil mVolleyUtil;
    private User mUser;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_security_setting);
        ButterKnife.bind(this);
        initStatusBar();

        mVolleyUtil = VolleyUtil.getInstance(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        mDialog = new LoadingDialog(this);

        getUserFromServer(mUser.getUserId());
        initView();
    }

    public void back(View view) {
        finish();
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

        refreshLinkedStatus();
    }

    /**
     * 刷新绑定状态
     */
    private void refreshLinkedStatus() {
        String emailIsLinked = "mUser.getUserIsEmailLinked()";
        if (L0_Constant.EMAIL_NOT_LINK.equals(emailIsLinked)) {
            mEmailIsLinkedTv.setText(getString(R.string.not_linked));
        } else if (L0_Constant.EMAIL_NOT_VERIFIED.equals(emailIsLinked)) {
            mEmailIsLinkedTv.setText(getString(R.string.not_verified));
        } else if (L0_Constant.EMAIL_VERIFIED.equals(emailIsLinked)) {
            mEmailIsLinkedTv.setText(getString(R.string.verified));
        } else {
            mEmailIsLinkedTv.setText(getString(R.string.not_linked));
        }

        String qqIsLinked = "mUser.getUserIsQqLinked()";
        String qqId = "mUser.";
        if (L0_Constant.QQ_ID_NOT_LINK.equals(qqIsLinked)) {
            mQqIdIsLinkedTv.setText("未绑定");
        } else if (L0_Constant.QQ_ID_LINKED.equals(qqIsLinked)) {
            mQqIdIsLinkedTv.setText(qqId);
        } else {
            mQqIdIsLinkedTv.setText("未绑定");
        }
    }

    @OnClick({R.id.rl_qq_id, R.id.rl_email})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_qq_id:
                startActivity(new Intent(MoreSecuritySettingActivity.this, QqIdLinkBeginActivity.class));
                break;
            case R.id.rl_email:
                // 未绑定出弹窗
                // 绑定未验证或者已绑定都是进邮件认证页面
                if (L0_Constant.EMAIL_NOT_LINK.equals(true)) {
                    openEditEmailDialog();
                } else {
                    startActivity(new Intent(MoreSecuritySettingActivity.this, EmailLinkActivity.class));
                }
                break;
        }
    }

    private void openEditEmailDialog() {
        final EditDialog mEditDialog = new EditDialog(MoreSecuritySettingActivity.this, getString(R.string.edit_email),
                "", getString(R.string.edit_email_tips),
                getString(R.string.ok), getString(R.string.cancel));
        mEditDialog.setOnDialogClickListener(new EditDialog.OnDialogClickListener() {
            @Override
            public void onOkClick() {
                mEditDialog.dismiss();
                String email = mEditDialog.getContent();

                mDialog.setMessage(getString(R.string.linking));
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.show();

                linkEmail(mUser.getUserId(), email, mUser.getUserId());
            }

            @Override
            public void onCancelClick() {
                mEditDialog.dismiss();
            }
        });

        // 点击空白处消失
        mEditDialog.setCancelable(false);
        mEditDialog.show();
    }

    private void linkEmail(String userId, final String email, final String wechatId) {
        String url = L0_Constant.BASE_URL + "email/link";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("to", email);
        paramMap.put("wechatId", wechatId);

        mVolleyUtil.httpPostRequest(url, paramMap, s -> {
            mDialog.dismiss();
            Toast.makeText(MoreSecuritySettingActivity.this,
                    "验证邮件已发送，请尽快登录邮箱验证", Toast.LENGTH_SHORT).show();
            mUser.setUserName(email);
            // 邮箱已绑定但未验证
            //sv mUser.setUserIsEmailLinked(L0_Constant.EMAIL_NOT_VERIFIED);
            L0_SharedPreferences.getInstance().setUser(mUser);
            refreshLinkedStatus();
        }, volleyError -> mDialog.dismiss());
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserFromServer(mUser.getUserId());
    }

    /**
     * 从服务器获取用户最新信息
     *
     * @param userId 用户ID
     */
    public void getUserFromServer(final String userId) {
        String url = L0_Constant.BASE_URL + "users/" + userId;

        mVolleyUtil.httpGetRequest(url, response -> {
            User user = JSON.parseObject(response, User.class);
            L0_SharedPreferences.getInstance().setUser(user);
            mUser = user;
            refreshLinkedStatus();
        }, volleyError -> {

        });
    }
}

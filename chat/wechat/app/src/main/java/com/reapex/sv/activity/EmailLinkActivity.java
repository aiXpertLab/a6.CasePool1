package com.reapex.sv.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.StatusBarUtil;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;


/**
 * 绑定邮箱地址
 *
 * @author zhou
 */
public class EmailLinkActivity extends L0_BaseActivity implements View.OnClickListener {

    private EditText mEmailEt;
    private TextView mTipsTv;
    private Button mUnLinkBtn;
    private TextView mSaveTv;

    private VolleyUtil mVolleyUtil;
    private User mUser;
    private LoadingDialog mDialog;
    private boolean mIsEdit = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_email);
        //initStatusBar();
        StatusBarUtil.setStatusBarColor(EmailLinkActivity.this, R.color.bottom_text_color_normal);

        mVolleyUtil = VolleyUtil.getInstance(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        mDialog = new LoadingDialog(this);

        initView();
    }

    private void initView() {
        mEmailEt = findViewById(R.id.et_email);
        mTipsTv = findViewById(R.id.tv_tips);
        mUnLinkBtn = findViewById(R.id.btn_unlink);
        mSaveTv = findViewById(R.id.tv_save);

//sv        mEmailEt.setText(mUser.getUserEmail());
        mUnLinkBtn.setOnClickListener(this);
        mSaveTv.setOnClickListener(this);

//sv        String isEmailLinked = mUser.getUserIsEmailLinked();
        if (L0_Constant.EMAIL_NOT_VERIFIED.equals(true)) {
            // 未验证
            mUnLinkBtn.setText(getString(R.string.resend_verification_email));
            mTipsTv.setText(getString(R.string.resend_verification_email_tips));
            mTipsTv.setTextColor(getColor(R.color.btn_red));
            mSaveTv.setVisibility(View.VISIBLE);
            mSaveTv.setText(getString(R.string.edit));
            mEmailEt.setEnabled(false);
        } else {
            // 已验证
            mUnLinkBtn.setVisibility(View.VISIBLE);
            mSaveTv.setVisibility(View.GONE);
            mEmailEt.setEnabled(false);
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
//sv         String isEmailLinked = mUser.getUserIsEmailLinked();
        switch (view.getId()) {
            case R.id.btn_unlink:
                if (L0_Constant.EMAIL_NOT_VERIFIED.equals(true)) {
                    // 未验证
                    // 发送验证邮件
                    String email = mEmailEt.getText().toString();
                    linkEmail(mUser.getUserId(), email, mUser.getUserId());
                } else {
                    // 已验证
                    // 解绑
                    mDialog.setMessage(getString(R.string.unlinking));
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    unLinkEmail(mUser.getUserId());
                }
                break;
            case R.id.tv_save:
                if (mIsEdit) {
                    // 未验证
                    // 编辑邮箱
                    mEmailEt.setEnabled(true);
                    mUnLinkBtn.setVisibility(View.GONE);
                    mTipsTv.setText(getString(R.string.unlink_tips));
                    mTipsTv.setTextColor(getColor(R.color.tips_grey));
                    mSaveTv.setText(R.string.save);

                    mIsEdit = false;
                } else {
                    // 已验证
                    // 保存
                    mEmailEt.setEnabled(false);
                    mUnLinkBtn.setVisibility(View.VISIBLE);
                    mUnLinkBtn.setText(R.string.resend_verification_email);
                    mTipsTv.setText(getString(R.string.resend_verification_email_tips));
                    mTipsTv.setTextColor(getColor(R.color.btn_red));
                    mSaveTv.setText(R.string.edit);
                    String email = mEmailEt.getText().toString();
                    linkEmail(mUser.getUserId(), email, mUser.getUserId());

                    mIsEdit = true;
                }
                break;
        }
    }

    private void unLinkEmail(String userId) {
        String url = L0_Constant.BASE_URL + "users/" + userId + "/emailLink";

        mVolleyUtil.httpDeleteRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mDialog.dismiss();
                //sv mUser.setUserName("");
                // 未绑定状态
                //sv mUser.setUserIsEmailLinked(L0_Constant.EMAIL_NOT_LINK);
                L0_SharedPreferences.getInstance().setUser(mUser);
                mUnLinkBtn.setVisibility(View.GONE);
                mSaveTv.setVisibility(View.VISIBLE);

                String content = "解绑完成后，下次登录请使用手机号" + mUser.getUserPhone() + "+微信独立密码 登录微信";
                showNoTitleAlertDialog(EmailLinkActivity.this, content, "知道了");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
            }
        });
    }

    private void linkEmail(String userId, final String email, final String wechatId) {
        mDialog = new LoadingDialog(EmailLinkActivity.this);
        mDialog.setMessage(getString(R.string.linking));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        String url = L0_Constant.BASE_URL + "email/link";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userId", userId);
        paramMap.put("to", email);
        paramMap.put("wechatId", wechatId);

        mVolleyUtil.httpPostRequest(url, paramMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mDialog.dismiss();
                //sv mUser.setUserName(email);
                // 邮箱已绑定但未验证
                //sv mUser.setUserIsEmailLinked(L0_Constant.EMAIL_NOT_VERIFIED);
                L0_SharedPreferences.getInstance().setUser(mUser);

//                Toast.makeText(EmailLinkActivity.this, "验证邮件已发送，请尽快登录邮箱验证", Toast.LENGTH_SHORT).show();

                showAlertDialog(EmailLinkActivity.this, "提示",
                        "验证邮件已发送，请尽快登录邮箱验证",
                        "确定", true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
            }
        });
    }
}

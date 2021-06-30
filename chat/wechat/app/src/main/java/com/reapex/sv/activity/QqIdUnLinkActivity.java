package com.reapex.sv.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.reapex.sv.widget.NoTitleConfirmDialog;

import androidx.annotation.Nullable;

/**
 * 解绑QQ号
 *
 * @author zhou
 */
public class QqIdUnLinkActivity extends L0_BaseActivity {

    private Button mUnLinkQqIdBtn;

    private VolleyUtil mVolleyUtil;
    private User mUser;
    private LoadingDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlink_qq_id);

        initStatusBar();
        StatusBarUtil.setStatusBarColor(QqIdUnLinkActivity.this, R.color.status_bar_color_white);

        mVolleyUtil = VolleyUtil.getInstance(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        mDialog = new LoadingDialog(this);

        mUnLinkQqIdBtn = findViewById(R.id.btn_unlink_qq);
        mUnLinkQqIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NoTitleConfirmDialog mNoTitleConfirmDialog = new NoTitleConfirmDialog(QqIdUnLinkActivity.this,
                        "确定解绑QQ",
                        getString(R.string.ok), getString(R.string.cancel), getColor(R.color.navy_blue));
                mNoTitleConfirmDialog.setOnDialogClickListener(new NoTitleConfirmDialog.OnDialogClickListener() {
                    @Override
                    public void onOkClick() {
                        mNoTitleConfirmDialog.dismiss();
                        mDialog.setMessage("解绑中");
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                        unLinkQqId(mUser.getUserId());
                    }

                    @Override
                    public void onCancelClick() {
                        mNoTitleConfirmDialog.dismiss();
                    }
                });
                // 点击空白处消失
                mNoTitleConfirmDialog.setCancelable(true);
                mNoTitleConfirmDialog.show();
            }
        });
    }

    public void back(View view) {
        finish();
    }

    /**
     * 解绑QQ号
     *
     * @param userId 用户ID
     */
    private void unLinkQqId(String userId) {
        String url = L0_Constant.BASE_URL + "users/" + userId + "/qqIdLink";

        mVolleyUtil.httpDeleteRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                mDialog.dismiss();
                // 解绑成功
                L0_SharedPreferences.getInstance().setUser(mUser);
                QqIdUnLinkActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mDialog.dismiss();
            }
        });
    }
}

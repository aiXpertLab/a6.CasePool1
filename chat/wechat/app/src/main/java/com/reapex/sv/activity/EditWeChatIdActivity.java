package com.reapex.sv.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.TimeoutError;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 微信号修改
 *
 * @author zhou
 */
public class EditWeChatIdActivity extends L0_BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.et_wechat_id)
    EditText mWeChatIdEt;

    @BindView(R.id.v_wechat_id)
    View mWechatIdView;

    @BindView(R.id.tv_save)
    TextView mSaveTv;

    private VolleyUtil mVolleyUtil;
    LoadingDialog mDialog;
    User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wechat_id);
        ButterKnife.bind(this);

        //initStatusBar();

        L0_SharedPreferences.getInstance().init(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        mVolleyUtil = VolleyUtil.getInstance(this);
        mDialog = new LoadingDialog(EditWeChatIdActivity.this);
        initView();

        mSaveTv.setOnClickListener(view -> {
            mDialog.setMessage(getString(R.string.saving));
            mDialog.show();
            String userId = mUser.getUserId();
            String userWxId = mWeChatIdEt.getText().toString();
            updateUserWxId(userId, userWxId);
        });
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

        mWeChatIdEt.setText(mUser.getUserId());
        // 光标移至最后
        CharSequence charSequence = mWeChatIdEt.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        mWeChatIdEt.addTextChangedListener(new TextChange());

        mWeChatIdEt.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                mWechatIdView.setBackgroundColor(getColor(R.color.divider_green));
            } else {
                mWechatIdView.setBackgroundColor(getColor(R.color.divider_grey));
            }
        });
    }

    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String newWxId = mWeChatIdEt.getText().toString();
            String oldWxId = mUser.getUserId() == null ? "" : mUser.getUserId();
            // 是否填写
            boolean isWxIdHasText = mWeChatIdEt.length() > 0;
            // 是否修改
            boolean isWxIdChanged = !oldWxId.equals(newWxId);

            if (isWxIdHasText && isWxIdChanged) {
                // 可保存
                mSaveTv.setTextColor(0xFFFFFFFF);
                mSaveTv.setEnabled(true);
            } else {
                // 不可保存
                mSaveTv.setTextColor(getColor(R.color.btn_text_default_color));
                mSaveTv.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public void back(View view) {
        finish();
    }

    private void updateUserWxId(final String userId, final String userWxId) {
        String url = L0_Constant.BASE_URL + "users/" + userId + "/userWxId";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userWxId", userWxId);

        mVolleyUtil.httpPutRequest(url, paramMap, response -> {
            mDialog.dismiss();
            setResult(RESULT_OK);
//sv            mUser.setUserWxId(userWxId);
//sv            mUser.setUserWxIdModifyFlag(L0_Constant.USER_WX_ID_MODIFY_FLAG_TRUE);
            L0_SharedPreferences.getInstance().setUser(mUser);
            finish();
        }, volleyError -> {
            mDialog.dismiss();
            if (volleyError instanceof NetworkError) {
                Toast.makeText(EditWeChatIdActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                return;
            } else if (volleyError instanceof TimeoutError) {
                Toast.makeText(EditWeChatIdActivity.this, R.string.network_time_out, Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}

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
 * 修改个性签名
 *
 * @author zhou
 */
public class EditSignActivity extends L0_BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.et_sign)
    EditText mSignEt;

    @BindView(R.id.tv_save)
    TextView mSaveTv;

    @BindView(R.id.tv_sign_length)
    TextView mSignLengthTv;

    final int maxSignLenth = 30;

    private VolleyUtil mVolleyUtil;
    LoadingDialog mDialog;
    User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        ButterKnife.bind(this);
        //initStatusBar();

        L0_SharedPreferences.getInstance().init(this);
        mVolleyUtil = VolleyUtil.getInstance(this);
        mDialog = new LoadingDialog(EditSignActivity.this);
        initView();

        mSaveTv.setOnClickListener(view -> {
            mDialog.setMessage(getString(R.string.saving));
            mDialog.show();
            String userId = mUser.getUserId();
            String userNickName = mSignEt.getText().toString();
            updateUserSign(userId, userNickName);
        });

    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

        mUser = L0_SharedPreferences.getInstance().getUser();

        mSignEt.setText(mUser.getId());
        // 剩余可编辑字数
        int leftSignLenth = maxSignLenth - mSignEt.length();
        if (leftSignLenth >= 0) {
            mSignLengthTv.setText(String.valueOf(leftSignLenth));
        }

        // 光标移至最后
        CharSequence charSequence = mSignEt.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
        mSignEt.addTextChangedListener(new TextChange());
    }

    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String newSign = mSignEt.getText().toString();
     //       String oldSign = mUser.getSerial() == null ? "" : mUser.getSerial();
            // 是否填写
            boolean isSignHasText = mSignEt.length() > 0;
            // 是否修改
            boolean isSignChanged = true; //!oldSign.equals(newSign);

            if (isSignHasText && isSignChanged) {
                // 可保存
                mSaveTv.setTextColor(0xFFFFFFFF);
                mSaveTv.setEnabled(true);
            } else {
                // 不可保存
                mSaveTv.setTextColor(getColor(R.color.btn_text_default_color));
                mSaveTv.setEnabled(false);
            }

            int leftSignLenth = maxSignLenth - mSignEt.length();
            if (leftSignLenth >= 0) {
                mSignLengthTv.setText(String.valueOf(leftSignLenth));
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // 是否填写
            int leftSignLenth = maxSignLenth - mSignEt.length();
            if (leftSignLenth >= 0) {
                mSignLengthTv.setText(String.valueOf(leftSignLenth));
            }
        }
    }

    public void back(View view) {
        finish();
    }

    private void updateUserSign(final String userId, final String userSign) {
        String url = L0_Constant.BASE_URL + "users/" + userId + "/userSign";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("userSign", userSign);

        mVolleyUtil.httpPutRequest(url, paramMap, response -> {
            mDialog.dismiss();
        //    mUser.setUserSign(userSign);
            L0_SharedPreferences.getInstance().setUser(mUser);
            finish();
        }, volleyError -> {
            mDialog.dismiss();
            if (volleyError instanceof NetworkError) {
                Toast.makeText(EditSignActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                return;
            } else if (volleyError instanceof TimeoutError) {
                Toast.makeText(EditSignActivity.this, R.string.network_time_out, Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}

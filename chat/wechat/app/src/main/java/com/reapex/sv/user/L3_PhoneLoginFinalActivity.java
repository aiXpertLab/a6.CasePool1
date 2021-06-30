package com.reapex.sv.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.reapex.sv.R;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.L4_MainActivity;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.DeviceInfo;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.utils.DeviceInfoUtil;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.widget.LoadingDialog;

import androidx.annotation.Nullable;

public class L3_PhoneLoginFinalActivity extends L0_BaseActivity implements View.OnClickListener {

    private static final String TAG = "PhoneLoginFinalActivity";
    public static int sequence = 1;

    EditText mPhoneEt, mPasswordEt;
    RelativeLayout mLoginByPasswordRl;
    ImageView mClearPasswordIv;
    Button mLoginBtn;
    UserDao dao;
    String mPhone, mNickname, mPassword, mAvatar;
    User mUser;
    LoadingDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_phone_login_final);

        mLoginBtn       = findViewById(R.id.btn_login);
        mClearPasswordIv= findViewById(R.id.iv_clear_password);
        mPhoneEt        = findViewById(R.id.et_phone);
        mPasswordEt     = findViewById(R.id.et_password);
        mLoginByPasswordRl= findViewById(R.id.rl_login_by_password);

        mLoginBtn.setOnClickListener(this);

        mPhone = getIntent().getStringExtra("phone");
        mNickname = getIntent().getStringExtra("nickname");
        mAvatar = getIntent().getStringExtra("avatar");
        mPassword = getIntent().getStringExtra("password");

        mDialog = new LoadingDialog(L3_PhoneLoginFinalActivity.this);
        initView();
    }

    public void back(View view) {
        finish();
    }

    private void initView() {
        mPhoneEt.setHint(mPhone);
        mPasswordEt.addTextChangedListener(new TextChange());
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            String password = mPasswordEt.getText().toString();
            if (TextUtils.isEmpty(password)) {
                showAlertDialog(L3_PhoneLoginFinalActivity.this, getString(R.string.login_error),
                        getString(R.string.enter_your_password), getString(R.string.ok), true);
            } else {
                login(mPhone, password);
            }
        } else if (v.getId() == R.id.iv_clear_password) {
            mPasswordEt.setText("");
        }
    }

    private void login(String phone, String password){
        mDialog.setMessage(getString(R.string.logging_in));
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        DeviceInfo deviceInfo = DeviceInfoUtil.getInstance().getDeviceInfo(L3_PhoneLoginFinalActivity.this);
        int ii = 0;
        if (mPassword.equals(password)){
            mUser =  new User(phone, mNickname, phone, mPassword, mAvatar);
            L0_SharedPreferences.getInstance().setUser(mUser);
            L0_SharedPreferences.getInstance().setLogin(true);
            startActivity(new Intent(L3_PhoneLoginFinalActivity.this, L4_MainActivity.class));
        }else{
            finish();
        }
    }

    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {            }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            boolean passwordEtHasText = mPasswordEt.getText().length() > 0;
            if (passwordEtHasText) {
                mClearPasswordIv.setVisibility(View.VISIBLE);
            } else {
                mClearPasswordIv.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }


}
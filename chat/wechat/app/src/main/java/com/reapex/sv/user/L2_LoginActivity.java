package com.reapex.sv.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.reapex.sv.R;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.entitydaodb.UChatDB;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.utils.ValidateUtil;

import androidx.annotation.Nullable;

import java.util.List;

public class L2_LoginActivity extends L0_BaseActivity implements View.OnClickListener {
    EditText mEditTextPhone;
    Button mBtnNext;
    UChatDB db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_login);

        mEditTextPhone =findViewById(R.id.et_phone);
        mBtnNext       =findViewById(R.id.btn_next);
        mBtnNext.setOnClickListener(this);

        initView();
    }

    public void back(View view) {
        finish();
    }

    private void initView() {
        mEditTextPhone.addTextChangedListener(new TextChange());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {

            String phone = mEditTextPhone.getText().toString();
            String password ="";
            String nickname="";
            String avatar="";

            // 是否有效手机号
            boolean isValidChinesePhone = ValidateUtil.isValidChinesePhone(phone);
            if (isValidChinesePhone) {
                db = UChatDB.getDatabase(this);
                List<User> list = db.getUserDao().getUserListFromPhone(phone);
                int i2 = list.size();
                if (list.size()>=1) {
                    String text = "";
                    for (int i = 0; i < list.size(); i++) {
                        User user = list.get(i);
                        nickname = user.getUserName();
                        password = user.getUserPassword();
                        avatar   = user.getUserAvatar();
                        Log.e("text:  ", nickname + " " + password + " " + avatar);
                    }
                    // 有效
                    Intent intent = new Intent(L2_LoginActivity.this, L3_PhoneLoginFinalActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("nickName", nickname);
                    intent.putExtra("password", password);
                    intent.putExtra("avatar", avatar);
                    startActivity(intent);
                }else{
                    // 无效
                    showAlertDialog(L2_LoginActivity.this, "手机号码错误",
                            "本号码尚未注册。请注册后继续。",
                            "确定", true);
                }
            } else {
                // 无效
                showAlertDialog(L2_LoginActivity.this, "手机号码错误",
                        "你输入的是一个无效的手机号码",
                        "确定", true);
            }
        }
    }

    class TextChange implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // 手机号登录
            boolean phoneEtHasText = mEditTextPhone.getText().length() > 0;
            if (phoneEtHasText) {
                // "下一步"按钮可用
                mBtnNext.setBackgroundResource(R.drawable.btn_login_next_enable);
                mBtnNext.setTextColor(getColor(R.color.register_btn_text_enable));
                mBtnNext.setEnabled(true);
            } else {
                // "下一步"按钮不可用
                mBtnNext.setBackgroundResource(R.drawable.btn_login_next_disable);
                mBtnNext.setTextColor(getColor(R.color.register_btn_text_disable));
                mBtnNext.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {        }
    }
}
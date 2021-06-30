package com.hypech.case7full;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private DBOpenHelper mDBOpenHelper;
    private EditText etUser, etPWD;
    private CheckBox cb_rmbPsw;
    private String userName;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        Objects.requireNonNull(getSupportActionBar()).hide();//隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//禁止横屏
        setContentView(R.layout.login);

        initView();//初始化界面
        mDBOpenHelper = new DBOpenHelper(this);

        SharedPreferences sp = getSharedPreferences("user_mes", MODE_PRIVATE);
        editor = sp.edit();
        if(sp.getBoolean("flag",false)){
            String user_read = sp.getString("user","");
            String psw_read = sp.getString("psw","");
            etUser.setText(user_read);
            etPWD.setText(psw_read);
        }
    }

    private void initView() {
        //初始化控件
        etUser = findViewById(R.id.et_User);
        etPWD = findViewById(R.id.et_Psw);
        cb_rmbPsw = findViewById(R.id.cb_rmbPsw);
        Button btn_Login = findViewById(R.id.btn_Login);
        TextView tv_register = findViewById(R.id.tv_Register);
        //设置点击事件监听器
        btn_Login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_Register: //注册
                Intent intent = new Intent(Login.this, Register.class);//跳转到注册界面
                startActivity(intent);
                finish();
                break;
            /**
             * 登录验证：
             *
             * 从EditText的对象上获取文本编辑框输入的数据，并把左右两边的空格去掉
             *  String name = mEtLoginactivityUsername.getText().toString().trim();
             *  String password = mEtLoginactivityPassword.getText().toString().trim();
             *  进行匹配验证,先判断一下用户名密码是否为空，
             *  if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password))
             *  再进而for循环判断是否与数据库中的数据相匹配
             *  if (name.equals(user.getName()) && password.equals(user.getPassword()))
             *  一旦匹配，立即将match = true；break；
             *  否则 一直匹配到结束 match = false；
             *
             *  登录成功之后，进行页面跳转：
             *
             *  Intent intent = new Intent(this, MainActivity.class);
             *  startActivity(intent);
             *  finish();//销毁此Activity
             */
            case R.id.btn_Login:
                String name = etUser.getText().toString().trim();
                String password = etPWD.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<UserBean> data = mDBOpenHelper.getAllData();
                    int ii = data.size();
                    if (data.size() == 0){
                        Toast.makeText(this, "请输入你的用eeee户名或密码", Toast.LENGTH_SHORT).show();
                    }
                    boolean match = false;
                    boolean match2 = false;
                    for (int i = 0; i < data.size(); i++) {
                        UserBean user = data.get(i);
                        if ((name.equals(user.getName()) && password.equals(user.getPassword()))||
                                (name.equals(user.getEmail())&&password.equals(user.getPassword()))||
                                (name.equals(user.getPhonenum())&&password.equals(user.getPassword()))) {
                            userName = user.getName();
                            match = true;
                            if(cb_rmbPsw.isChecked()){
                                editor.putBoolean("flag",true);
                                editor.putString("user",user.getName());
                                editor.putString("psw",user.getPassword());
                                editor.apply();
                                match2 = true;
                            }else {
                                editor.putString("user",user.getName());
                                editor.putString("psw","");
//                                editor.clear();
                                editor.apply();
                                match2 = false;
                            }
                            break;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, userName + "登录成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
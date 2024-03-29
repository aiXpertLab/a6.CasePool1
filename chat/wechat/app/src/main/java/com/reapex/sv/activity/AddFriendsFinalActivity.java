package com.reapex.sv.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.VolleyUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;


public class AddFriendsFinalActivity extends FragmentActivity {

    private EditText mReasonEt;
    private TextView mSendTv;
    private VolleyUtil volleyUtil;
    ProgressDialog dialog;
    User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends_final);

        L0_SharedPreferences.getInstance().init(this);
        user = L0_SharedPreferences.getInstance().getUser();
        volleyUtil = VolleyUtil.getInstance(this);
        dialog = new ProgressDialog(AddFriendsFinalActivity.this);
        initView();
    }

    private void initView() {
        mReasonEt = findViewById(R.id.et_reason);
        mSendTv = findViewById(R.id.tv_send);

//sv        mReasonEt.setText("我是" + nickName);
        // 光标移至最后
        CharSequence charSequence = mReasonEt.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }

//sv        final String fromUserId = user.getUserId();
        final String toUserId = getIntent().getStringExtra("userId");
        final String applyRemark = mReasonEt.getText().toString().trim();

        mSendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setMessage("正在发送...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();
//sv                addFriendApply(fromUserId, toUserId, applyRemark);
            }
        });
    }

    public void back(View view) {
        finish();
    }

    private void addFriendApply(String fromUserId, String toUserId, String applyRemark) {
        String url = L0_Constant.BASE_URL + "friendApplies";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("fromUserId", fromUserId);
        paramMap.put("toUserId", toUserId);
        paramMap.put("applyRemark", applyRemark);

        volleyUtil.httpPostRequest(url, paramMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(AddFriendsFinalActivity.this, "已发送", Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NetworkError) {
                    Toast.makeText(AddFriendsFinalActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
            }
        });
    }
}

package com.reapex.sv.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 更多信息
 *
 * @author zhou
 */
public class MyMoreProfileActivity extends L0_BaseActivity {

    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.rl_sex)
    RelativeLayout mSexRl;

    @BindView(R.id.rl_region)
    RelativeLayout mRegionRl;

    @BindView(R.id.rl_sign)
    RelativeLayout mSignRl;

    @BindView(R.id.tv_sex)
    TextView mSexTv;

    @BindView(R.id.tv_region)
    TextView mRegionTv;

    @BindView(R.id.tv_sign)
    TextView mSignTv;

    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_more_profile);
        ButterKnife.bind(this);

        initStatusBar();

        L0_SharedPreferences.getInstance().init(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        initView();
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

        String userSex = mUser.getUserName();

        if (L0_Constant.USER_SEX_MALE.equals(userSex)) {
            mSexTv.setText(getString(R.string.sex_male));
        } else if (L0_Constant.USER_SEX_FEMALE.equals(userSex)) {
            mSexTv.setText(getString(R.string.sex_female));
        }
        //sv mRegionTv.setText(mUser.getUserRegion());
        mSignTv.setText(mUser.getId());
    }

    public void back(View view) {
        finish();
    }

    @OnClick({R.id.rl_sex, R.id.rl_region, R.id.rl_sign})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sex:
                startActivity(new Intent(this, SetGenderActivity.class));
                break;
            case R.id.rl_region:
                startActivity(new Intent(this, PickRegionActivity.class));
                break;
            case R.id.rl_sign:
                // 签名
                startActivity(new Intent(this, EditSignActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = L0_SharedPreferences.getInstance().getUser();
        if (L0_Constant.USER_SEX_MALE.equals(user.getUserName())) {
            mSexTv.setText(getString(R.string.sex_male));
        } else if (L0_Constant.USER_SEX_FEMALE.equals(user.getUserName())) {
            mSexTv.setText(getString(R.string.sex_female));
        } else {
            mSexTv.setText("");
        }
        //sv mRegionTv.setText(user.getUserRegion());

        // 个性签名
        mSignTv.setText(user.getId());
    }
}

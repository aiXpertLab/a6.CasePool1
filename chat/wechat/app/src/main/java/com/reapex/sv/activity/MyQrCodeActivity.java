package com.reapex.sv.activity;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 二维码名片
 *
 * @author zhou
 */
public class MyQrCodeActivity extends L0_BaseActivity {
    @BindView(R.id.tv_title)
    TextView mTitleTv;

    @BindView(R.id.sdv_avatar)
    SimpleDraweeView mAvatarSdv;

    @BindView(R.id.tv_nick_name)
    TextView mNickNameTv;

    @BindView(R.id.iv_sex)
    ImageView mSexIv;

    @BindView(R.id.tv_region)
    TextView mRegionTv;

    @BindView(R.id.sdv_qr_code)
    SimpleDraweeView mQrCodeSdv;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr_code);
        ButterKnife.bind(this);
        initStatusBar();

        mUser = L0_SharedPreferences.getInstance().getUser();
        initView();
    }

    private void initView() {
        TextPaint paint = mTitleTv.getPaint();
        paint.setFakeBoldText(true);

        if (!TextUtils.isEmpty(mUser.getUserAvatar())) {
            mAvatarSdv.setImageURI(Uri.parse(mUser.getUserAvatar()));
        }

    }

    public void back(View view) {
        finish();
    }
}

package com.reapex.sv.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reapex.sv.R;
import com.reapex.sv.activity.BigImageActivity;
import com.reapex.sv.activity.MyProfileActivity;
import com.reapex.sv.activity.SettingActivity;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.utils.OssUtil;
import com.reapex.sv.L0_SharedPreferences;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MeFragment extends Fragment implements View.OnClickListener {

    SimpleDraweeView mAvatarSdv;
    TextView mNickNameTv;

    User mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.a_fragment_me, container, false);
        mAvatarSdv = view.findViewById(R.id.sdv_avatar);
        mNickNameTv= view.findViewById(R.id.tv_name);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUser = L0_SharedPreferences.getInstance().getUser();
        initView();

    }

    private void initView() {
        mNickNameTv.setText(mUser.getUserName());
        String userAvatar = mUser.getUserAvatar();
        if (!TextUtils.isEmpty(userAvatar)) {
            String resizeAvatarUrl = OssUtil.resize(mUser.getUserAvatar());
            mAvatarSdv.setImageURI(Uri.parse(resizeAvatarUrl));
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            // 个人页面
            case R.id.rl_me:
                startActivity(new Intent(getActivity(), MyProfileActivity.class));
                break;
            // 设置页面
            case R.id.rl_settings:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            // 头像点击放大
            case R.id.sdv_avatar:
                Intent intent = new Intent(getActivity(), BigImageActivity.class);
                intent.putExtra("imgUrl", mUser.getUserAvatar());
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUser = L0_SharedPreferences.getInstance().getUser();
        mNickNameTv.setText(mUser.getUserName());
        if (!TextUtils.isEmpty(mUser.getUserAvatar())) {
            String resizeAvatarUrl = OssUtil.resize(mUser.getUserAvatar());
            mAvatarSdv.setImageURI(Uri.parse(resizeAvatarUrl));
        }
    }
}

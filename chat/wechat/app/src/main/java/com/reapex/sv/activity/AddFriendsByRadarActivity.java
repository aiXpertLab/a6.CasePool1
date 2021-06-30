package com.reapex.sv.activity;

import android.os.Bundle;
import android.view.View;

import com.reapex.sv.R;
import com.facebook.drawee.view.SimpleDraweeView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class AddFriendsByRadarActivity extends FragmentActivity {
    private SimpleDraweeView mAvatarSdv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends_by_radar);

    }

    public void back(View view) {
        finish();
    }
}

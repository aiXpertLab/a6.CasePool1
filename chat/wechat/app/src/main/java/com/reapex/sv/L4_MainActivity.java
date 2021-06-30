package com.reapex.sv;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.activity.AddContactsActivity;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.fragment.ChatsFragment;
import com.reapex.sv.fragment.DiscoverFragment;
import com.reapex.sv.fragment.ContactsFragment;
import com.reapex.sv.fragment.MeFragment;
import com.reapex.sv.utils.StatusBarUtil;
import com.reapex.sv.widget.ConfirmDialog;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * 主activity
 */
public class L4_MainActivity extends L0_BaseActivity {
    public static boolean isForeground = false;

    private Fragment[]       mFragments;
    private ChatsFragment    mChatsFragment;
    private ContactsFragment mContactsFragment;
    private DiscoverFragment mDiscoverFragment;
    private MeFragment       mMeFragment;

    private ImageView[] mMainButtonIvs;
    private TextView[] mMainButtonTvs;

    private int mIndex;
    // 当前fragment的index
    private int mCurrentTabIndex;
    User mUser;

    // 首页弹出框
    private PopupWindow mPopupWindow;
    private View mPopupView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_main);
        initView();

        L0_SharedPreferences.getInstance().init(this);
        mUser = L0_SharedPreferences.getInstance().getUser();
        // 进入强制刷新，防止离线消息
        mChatsFragment.refreshConversationList();
    }

    private void initView() {
        mChatsFragment    = new ChatsFragment();
        mContactsFragment = new ContactsFragment();
        mDiscoverFragment = new DiscoverFragment();
        mMeFragment       = new MeFragment();

        mFragments = new Fragment[]{mChatsFragment, mContactsFragment,
                mDiscoverFragment, mMeFragment};

        mMainButtonIvs = new ImageView[4];
        mMainButtonIvs[0] = findViewById(R.id.iv_chats);
        mMainButtonIvs[1] = findViewById(R.id.iv_contacts);
        mMainButtonIvs[2] = findViewById(R.id.iv_discover);
        mMainButtonIvs[3] = findViewById(R.id.iv_me);

        mMainButtonIvs[0].setSelected(true);
        mMainButtonTvs = new TextView[4];
        mMainButtonTvs[0] = findViewById(R.id.tv_chats);
        mMainButtonTvs[1] = findViewById(R.id.tv_contacts);
        mMainButtonTvs[2] = findViewById(R.id.tv_discover);
        mMainButtonTvs[3] = findViewById(R.id.tv_me);
        mMainButtonTvs[0].setTextColor(0xFF45C01A);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.rl_fragment_container, mChatsFragment)
                .add(R.id.rl_fragment_container, mContactsFragment)
                .add(R.id.rl_fragment_container, mDiscoverFragment)
                .add(R.id.rl_fragment_container, mMeFragment)
                .hide(mContactsFragment).hide(mDiscoverFragment).hide(mMeFragment)
                .show(mChatsFragment).commit();

    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_chats:
                // 会话列表
                // 主动加载一次会话
                int i = 1;
                mChatsFragment.refreshConversationList();
                mIndex = 0;
                StatusBarUtil.setStatusBarColor(L4_MainActivity.this, R.color.wechat_common_bg);
                break;
            case R.id.rl_contacts:
                mIndex = 1;
                StatusBarUtil.setStatusBarColor(L4_MainActivity.this, R.color.wechat_common_bg);
                break;
            case R.id.rl_discover:
                mIndex = 2;
                StatusBarUtil.setStatusBarColor(L4_MainActivity.this, R.color.wechat_common_bg);
                break;
            case R.id.rl_me:
                mIndex = 3;
                StatusBarUtil.setStatusBarColor(L4_MainActivity.this, R.color.bottom_text_color_normal);
                break;
        }

        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[mIndex].isAdded()) {
                trx.add(R.id.rl_fragment_container, mFragments[mIndex]);
            }
            trx.show(mFragments[mIndex]).commit();
        }
        mMainButtonIvs[mCurrentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mMainButtonIvs[mIndex].setSelected(true);
        mMainButtonTvs[mCurrentTabIndex].setTextColor(getColor(R.color.black_deep));
        mMainButtonTvs[mIndex].setTextColor(0xFF45C01A);
        mCurrentTabIndex = mIndex;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isForeground = true;

        mContactsFragment.refreshNewFriendsUnreadNum();
        mContactsFragment.refreshFriendsList();

        // 会话
        if (mCurrentTabIndex == 0) {
            mChatsFragment.refreshConversationList();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeground = false;
    }

    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化首页弹出框
     */
    private void initPopupWindow() {
        mPopupView = View.inflate(this, R.layout.popup_window_add, null);
        mPopupWindow = new PopupWindow();
        // 设置SelectPicPopupWindow的View
        mPopupWindow.setContentView(mPopupView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        // 刷新状态
        mPopupWindow.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        mPopupWindow.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);

        // 帮助和反馈
        RelativeLayout mHelpRl = mPopupView.findViewById(R.id.rl_help);

    }

    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
    }

    /**
     * 动态权限
     */
    public void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //Android 6.0开始的动态权限，这里进行版本判断
            ArrayList<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {
                // 非初次进入App且已授权
                switch (requestCode) {
                }
            } else {
                // 请求权限方法
                String[] requestPermissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                // 这个触发下面onRequestPermissionsResult这个回调
                ActivityCompat.requestPermissions(this, requestPermissions, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        // 判断是否拒绝  拒绝后要怎么处理 以及取消再次提示的处理
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                break;
            }
        }
        if (hasAllGranted) {
            // 同意权限做的处理,开启服务提交通讯录
            switch (requestCode) {
            }
        } else {
            // 拒绝授权做的处理，弹出弹框提示用户授权
            handleRejectPermission(this, permissions[0], requestCode);
        }
    }

    public void handleRejectPermission(final Activity context, String permission,
                                       int requestCode) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            String content = "";
            // 非初次进入App且已授权
            switch (requestCode) {
            }
            if (!TextUtils.isEmpty(content)) {
                final ConfirmDialog mConfirmDialog = new ConfirmDialog(L4_MainActivity.this, "权限申请",
                        content,
                        "去设置", "取消", context.getColor(R.color.navy_blue));
                mConfirmDialog.setOnDialogClickListener(new ConfirmDialog.OnDialogClickListener() {
                    @Override
                    public void onOkClick() {
                        mConfirmDialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getApplicationContext().getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelClick() {
                        mConfirmDialog.dismiss();
                    }
                });
                // 点击空白处消失
                mConfirmDialog.setCancelable(false);
                mConfirmDialog.show();
            }
        }
    }
}
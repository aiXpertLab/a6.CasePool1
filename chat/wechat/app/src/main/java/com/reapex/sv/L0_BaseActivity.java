package com.reapex.sv;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.reapex.sv.daodb.MessageDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.widget.AlertDialog;
import com.reapex.sv.widget.NoTitleAlertDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class L0_BaseActivity extends FragmentActivity {
    private MessageDao mMessageDao;
    private User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessageDao = new MessageDao();
        mUser = L0_SharedPreferences.getInstance().getUser();
    }

    protected void initStatusBar() {
     //   Window win = getWindow();
        // KITKAT也能满足，只是SYSTEM_UI_FLAG_LIGHT_STATUS_BAR（状态栏字体颜色反转）只有在6.0才有效
       // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
         //   win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 显示警告弹窗
     *
     * @param context    context
     * @param title      标题
     * @param content    内容
     * @param confirm    确认键
     * @param cancelable 点击空白处是否消失
     */
    protected void showAlertDialog(Context context, String title, String content, String confirm, boolean cancelable) {
        final AlertDialog mAlertDialog = new AlertDialog(context, title, content, confirm);
        mAlertDialog.setOnDialogClickListener(new AlertDialog.OnDialogClickListener() {
            @Override
            public void onOkClick() {
                mAlertDialog.dismiss();
            }

        });
        // 点击空白处消失
        mAlertDialog.setCancelable(cancelable);
        mAlertDialog.show();
    }

    /**
     * 显示警告弹窗(无标题)
     *
     * @param context context
     * @param content 内容
     * @param confirm 确认键
     */
    protected void showNoTitleAlertDialog(Context context, String content, String confirm) {
        final NoTitleAlertDialog mNoTitleAlertDialog = new NoTitleAlertDialog(context, content, confirm);
        mNoTitleAlertDialog.setOnDialogClickListener(new NoTitleAlertDialog.OnDialogClickListener() {
            @Override
            public void onOkClick() {
                mNoTitleAlertDialog.dismiss();
            }

        });
        // 点击空白处消失
        mNoTitleAlertDialog.setCancelable(true);
        mNoTitleAlertDialog.show();
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}

package com.reapex.sv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.activity.ChatSingleSettingActivity;
import com.reapex.sv.entitydaodb.Chat;
import com.reapex.sv.entitydaodb.ChatAdapter;
import com.reapex.sv.entitydaodb.L_Message;
import com.reapex.sv.entitydaodb.L_MessageAdapter;
import com.reapex.sv.entitydaodb.UChatDB;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.entitydaodb.enums.MessageStatus;
import com.reapex.sv.utils.L_CommonUtil;
import com.reapex.sv.utils.TimeUtil;
import com.reapex.sv.widget.ConfirmDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.reapex.sv.L0_Constant.REQUEST_CODE_VOICE;

/**
 * 聊天
 * @author zhou
 */
public class L6_ChatActivity extends L0_BaseActivity implements View.OnClickListener {

    UChatDB db;

    private InputMethodManager mInputMethod;
    private TextView mFromNickNameTextView;
    private LinearLayout mMoreLinearLayout;

    private Button mSetModeVoiceBtn;    //切换成语音
    private Button mSetModeKeyboardBtn; // * 切换成文字
    private LinearLayout mPressToSpeakLl;   //* 按住说话
    private LinearLayout mBtnContainerLl;   //各种消息类型容器

    // 语音
    private RelativeLayout mVoiceRecordingContainerRl;
    private TextView mVoiceRecordingHintTv;
    private ImageView mVoiceRecordingAnimIv;
    private AnimationDrawable mVoiceReocrdingAd;

    private Button mMoreBtn, mSendBtn;

    private EditText mTextMsgEt;
    private RelativeLayout mTextMsgRl;

    private ImageView mSingleChatSettingIv;

    private ListView mChatListView;
    private ChatAdapter mChatAdapter;
    List<Chat> mChatList;
    User mUser;

    // intent传值    // 单聊
    private String targetType,    contactId,     contactNickName,     contactAvatar;
    private int mChatIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_chat);               //a_activity_chat is listview
        L0_SharedPreferences.getInstance().init(this);
        mUser = L0_SharedPreferences.getInstance().getUser();       //return user
        db = UChatDB.getDatabase(getApplicationContext());
        initView();
        setUpView();
    }

    private void initView() {
        mFromNickNameTextView = findViewById(R.id.tv_from_nick_name);     //nickname 聊天窗口，左上角
        mChatListView = findViewById(R.id.lv_message);
        mMoreLinearLayout = findViewById(R.id.ll_more);
        mBtnContainerLl = findViewById(R.id.ll_btn_container);
        mSetModeVoiceBtn = findViewById(R.id.btn_set_mode_voice);
        mSetModeKeyboardBtn = findViewById(R.id.btn_set_mode_keyboard);
        mPressToSpeakLl = findViewById(R.id.ll_press_to_speak);
        mMoreBtn = findViewById(R.id.btn_more);
        mSendBtn = findViewById(R.id.btn_send);
        mTextMsgEt = findViewById(R.id.et_text_msg);
        mTextMsgRl = findViewById(R.id.rl_text_msg);
        mSingleChatSettingIv = findViewById(R.id.iv_setting);
        mVoiceRecordingContainerRl = findViewById(R.id.rl_voice_recording_container);
        mVoiceRecordingHintTv = findViewById(R.id.tv_voice_recording_hint);
        mVoiceRecordingAnimIv = findViewById(R.id.iv_voice_recording_anim);

        mTextMsgEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    // 获取焦点                    // 隐藏消息类型容器
                    mBtnContainerLl.setVisibility(View.GONE);
                    // 聊天页拉至最下
                    mChatListView.setSelection(mChatListView.getCount() - 1);
                }
            }
        });

        mTextMsgEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if (TextUtils.isEmpty(charSequence)) {
                    mMoreBtn.setVisibility(View.VISIBLE);
                    mSendBtn.setVisibility(View.GONE);
                } else {
                    mMoreBtn.setVisibility(View.GONE);
                    mSendBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mChatListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                hideKeyboard();

                // 隐藏更多
                    mMoreLinearLayout.setVisibility(View.GONE);

                return false;
            }
        });

        mSingleChatSettingIv.setOnClickListener(this);
        mSetModeVoiceBtn.setOnClickListener(this);
        mSetModeKeyboardBtn.setOnClickListener(this);
        mPressToSpeakLl.setOnTouchListener(new PressToSpeakListener());
    }

    private void setUpView() {
        mInputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        targetType = getIntent().getStringExtra("targetType");
        contactId = getIntent().getStringExtra("contactId");
        contactNickName = getIntent().getStringExtra("contactNickName");
        contactAvatar = getIntent().getStringExtra("contactAvatar");
        mFromNickNameTextView.setText(contactNickName);

        mChatList = db.getChatDao().getChatList("18018018018");

        mChatAdapter = new ChatAdapter(this, mChatList);
        mChatListView.setAdapter(mChatAdapter);

        mChatListView.setSelection(mChatListView.getCount() - 1);
    }

    @Override
    public void onClick(View view) {
        String[] permissions;
        switch (view.getId()) {
            case R.id.btn_send:
//                String content = mTextMsgEt.getText().toString();
                String content = "//sv文字入口";        //sent action is done by MessageAdapter
                sendTextMsg(content);
                break;
            case R.id.iv_setting:
                // 单聊设置
                Intent intent = new Intent(L6_ChatActivity.this, ChatSingleSettingActivity.class);
                intent.putExtra("userId", contactId);
                intent.putExtra("userNickName", contactNickName);
                intent.putExtra("userAvatar", contactAvatar);
                startActivity(intent);
                break;
            case R.id.btn_set_mode_voice:
                // 切换成语音
                permissions = new String[]{"android.permission.RECORD_AUDIO"};
                requestPermissions(L6_ChatActivity.this, permissions, REQUEST_CODE_VOICE);
                break;
            case R.id.btn_set_mode_keyboard:
                // 切换成文字
                // 输入框获取焦点
                // 显示软键盘
                mTextMsgEt.setFocusable(true);
                mTextMsgEt.setFocusableInTouchMode(true);
                mTextMsgEt.requestFocus();
//                showKeyboard();

                mPressToSpeakLl.setVisibility(View.GONE);
                mTextMsgRl.setVisibility(View.VISIBLE);

                mSetModeKeyboardBtn.setVisibility(View.GONE);
                mSetModeVoiceBtn.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_press_to_speak:
                break;
        }
    }

    public void back(View view) {
        finish();
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view
     */
    public void more(View view) {
        if (mMoreLinearLayout.getVisibility() == View.GONE) {
//            hideKeyboard();
            mMoreLinearLayout.setVisibility(View.VISIBLE);
            mBtnContainerLl.setVisibility(View.VISIBLE);
//            mEmojiContainerLl.setVisibility(View.GONE);

            // 切换成文字
            mPressToSpeakLl.setVisibility(View.GONE);
            mTextMsgRl.setVisibility(View.VISIBLE);

            mSetModeKeyboardBtn.setVisibility(View.GONE);
            mSetModeVoiceBtn.setVisibility(View.VISIBLE);
        } else {
                mMoreLinearLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 点击文字输入框
     *
     * @param v
     */
    public void editClick(View v) {
        mChatListView.setSelection(mChatListView.getCount() - 1);
        if (mMoreLinearLayout.getVisibility() == View.VISIBLE) {
            mMoreLinearLayout.setVisibility(View.GONE);
        }

    }

    /**
     * 发送文字消息
     * @param content 消息内容
     */
    private void sendTextMsg(String content) {
        Chat chat = new Chat();
        chat.setChatId(L_CommonUtil.generateId());
        chat.setTargetType(targetType);
        chat.setContent(content);
        chat.setCreateTime(TimeUtil.getTimeStringAutoShort2(new Date().getTime(), true));
        chat.setFromUserId(mUser.getUserId());
        chat.setToUserId(contactId);
        chat.setToUserName(contactNickName);
        chat.setToUserAvatar(contactAvatar);
        chat.setTimestamp(new Date().getTime());
        chat.setStatus(MessageStatus.SENDING.value());

        mChatList.add(chat);
        mChatIndex = mChatList.size() - 1;
        chat.setChatType(L0_Constant.MSG_TYPE_TEXT);

        db.getChatDao().insert(chat);

        mChatAdapter.notifyDataSetChanged();
        mChatListView.setSelection(mChatListView.getCount() - 1);
        mTextMsgEt.setText("");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(db!=null){
            if(db.isOpen()) {
                db.close();
            }
            db=null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        }
    }

    /**
     * 动态权限
     */
    public void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        // Android 6.0开始的动态权限，这里进行版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    case REQUEST_CODE_VOICE:
                        showAudio();
                        break;
                }
            } else {
                // 请求权限方法
                String[] requestPermissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                // 这个触发下面onRequestPermissionsResult这个回调
                ActivityCompat.requestPermissions(activity, requestPermissions, requestCode);
            }
        }
    }

    /**
     * requestPermissions的回调
     * 一个或多个权限请求结果回调
     */
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
            switch (requestCode) {
                case REQUEST_CODE_VOICE:
                    showAudio();
                    break;
            }
        } else {
            // 拒绝授权做的处理，弹出弹框提示用户授权
            handleRejectPermission(L6_ChatActivity.this, permissions[0], requestCode);
        }
    }

    public void handleRejectPermission(final Activity context, String permission, int requestCode) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
            String content = "";
            // 非初次进入App且已授权
            switch (requestCode) {
                case REQUEST_CODE_VOICE:
                    content = getString(R.string.request_permission_record_audio);
                    break;
            }

            final ConfirmDialog mConfirmDialog = new ConfirmDialog(L6_ChatActivity.this, getString(R.string.request_permission),
                    content,
                    getString(R.string.go_setting), getString(R.string.cancel), getColor(R.color.navy_blue));
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

    /**
     * 进入录音模式
     */
    private void showAudio() {
        // 切换成语音
//        hideKeyboard();
        // 隐藏消息类型容器
        mBtnContainerLl.setVisibility(View.GONE);

        // 显示"按住说话"
        mPressToSpeakLl.setVisibility(View.VISIBLE);
        // 隐藏文本输入框
        mTextMsgRl.setVisibility(View.GONE);

        mSetModeVoiceBtn.setVisibility(View.GONE);
        mSetModeKeyboardBtn.setVisibility(View.VISIBLE);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            }
        }
    };

    /**
     * 按住说话listener
     */
    class PressToSpeakListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    try {
                        v.setPressed(true);
                        // 播放动画
                        mVoiceReocrdingAd = (AnimationDrawable) mVoiceRecordingAnimIv.getDrawable();
                        mVoiceReocrdingAd.start();

                        mVoiceRecordingContainerRl.setVisibility(View.VISIBLE);
                        mVoiceRecordingHintTv.setText(getString(R.string.move_up_to_cancel));
                        mVoiceRecordingHintTv.setBackgroundColor(Color.TRANSPARENT);
                    } catch (Exception e) {
                        v.setPressed(false);
                        mVoiceRecordingContainerRl.setVisibility(View.INVISIBLE);
                        return false;
                    }
                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        mVoiceRecordingHintTv.setText(getString(R.string.release_to_cancel));
                        mVoiceRecordingHintTv.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        mVoiceRecordingHintTv.setText(getString(R.string.move_up_to_cancel));
                        mVoiceRecordingHintTv.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    mVoiceRecordingContainerRl.setVisibility(View.INVISIBLE);
                    return true;
                default:
                    mVoiceRecordingContainerRl.setVisibility(View.INVISIBLE);
                    return false;
            }
        }
    }
}
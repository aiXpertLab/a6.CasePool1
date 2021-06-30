package com.reapex.sv.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reapex.sv.R;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.L4_MainActivity;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UChatDB;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.utils.L_CommonUtil;
import com.reapex.sv.utils.FileUtil;
import com.reapex.sv.widget.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;

import androidx.annotation.Nullable;

import static com.reapex.sv.utils.ValidateUtil.validatePassword;

/**
 * 注册
 */
public class L2_RegisterActivity extends L0_BaseActivity implements View.OnClickListener {
    private static final String TAG = "Register ----  ----";
    private static final int UPDATE_AVATAR_BY_TAKE_CAMERA = 1;
    private static final int UPDATE_AVATAR_BY_ALBUM = 2;

    private String mImageName, mUserAvatar;

    public static int sequence = 1;

    LoadingDialog mDialog;
    SimpleDraweeView mAvatarSdv;
    ImageView mAgreementIv;
    TextView mAgreementTv;
    EditText mNickNameEt, mPhoneEt, mPasswordEt;
    Button mRegisterBtn;

    private boolean mIsAgree = false;   //是否同意协议

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_register);

        mRegisterBtn    = findViewById(R.id.btn_register);
        mAvatarSdv      = findViewById(R.id.sdv_avatar);
        mAgreementIv    = findViewById(R.id.iv_agreement);
        mAgreementTv    = findViewById(R.id.tv_agreement);

        mNickNameEt     = findViewById(R.id.et_nick_name);
        mPhoneEt        = findViewById(R.id.et_phone);
        mPasswordEt     = findViewById(R.id.et_password);

        mAvatarSdv.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mAgreementIv.setOnClickListener(this);
        //    mPhoneEt.setOnClickListener(this);

        mDialog = new LoadingDialog(L2_RegisterActivity.this);

        initView();
    }

    private void initView() {
        String agreement = "<font color=" + "\"" + "#AAAAAA" + "\">" + "已阅读并同意" + "</font>" + "<u>"
                + "<font color=" + "\"" + "#576B95" + "\">" + "《软件许可及服务协议》"
                + "</font>" + "</u>";
        mAgreementTv.setText(Html.fromHtml(agreement));

        mNickNameEt.addTextChangedListener(new TextChange());
        mPhoneEt.addTextChangedListener(new TextChange());
        mPasswordEt.addTextChangedListener(new TextChange());
    }

    public void back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register) {
            mDialog.setMessage(getString(R.string.registering));
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            String nickName = mNickNameEt.getText().toString();
            String phone    = mPhoneEt.getText().toString();
            String password = mPasswordEt.getText().toString();
            if (!validatePassword(password)) {
                mDialog.dismiss();
                Toast.makeText(L2_RegisterActivity.this, R.string.password_rules, Toast.LENGTH_SHORT).show();
                return;
            }
            int k=1;
            register(nickName, phone, password, mUserAvatar);
        }else if (v.getId() == R.id.iv_agreement){
            if (mIsAgree) {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_false);
                mIsAgree = false;
            } else {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_true);
                mIsAgree = true;
            }
            checkSubmit();
        }else if (v.getId() == R.id.sdv_avatar){
            showPhotoDialog();
        }else if (v.getId() == R.id.iv_agreement){
            if (mIsAgree) {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_false);
                mIsAgree = false;
            } else {
                mAgreementIv.setBackgroundResource(R.mipmap.icon_choose_true);
                mIsAgree = true;
            }
            checkSubmit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case UPDATE_AVATAR_BY_TAKE_CAMERA:
                    final File file = new File(Environment.getExternalStorageDirectory(), mImageName);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<String> imageList = FileUtil.uploadFile(L0_Constant.BASE_URL + "oss/file", file.getPath());
                            if (null != imageList && imageList.size() > 0) {
                                mUserAvatar = imageList.get(0);
                            }
                        }
                    }).start();
                    mAvatarSdv.setImageURI(Uri.fromFile(file));
                    break;
                case UPDATE_AVATAR_BY_ALBUM:
                    int p = 1;
                    if (data != null) {
                        Uri uri = data.getData();
                        final String filePath = FileUtil.getFilePathByUri(this, uri);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                List<String> imageList = FileUtil.uploadFile(L0_Constant.BASE_URL + "oss/file", filePath);
                                if (null != imageList && imageList.size() > 0) {
                                    mUserAvatar = imageList.get(0);
                                }
                            }
                        }).start();
                        mAvatarSdv.setImageURI(uri);
                    }
                    break;
            }
        }
    }

    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            checkSubmit();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    /**
     * 显示修改头像对话框
     */
    private void showPhotoDialog() {
        final AlertDialog photoDialog = new AlertDialog.Builder(this).create();
        photoDialog.show();
        Window window = photoDialog.getWindow();
        window.setContentView(R.layout.dialog_alert_abandon);
        TextView mTakePicTv = window.findViewById(R.id.tv_content1);
        TextView mAlbumTv = window.findViewById(R.id.tv_content2);
        mTakePicTv.setText("拍照");
        mTakePicTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageName = L_CommonUtil.generateId() + ".png";
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                        new File(Environment.getExternalStorageDirectory(), mImageName)));
                startActivityForResult(cameraIntent, UPDATE_AVATAR_BY_TAKE_CAMERA);
                photoDialog.dismiss();
            }
        });

        mAlbumTv.setText("相册");
        mAlbumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, UPDATE_AVATAR_BY_ALBUM);
                photoDialog.dismiss();
            }
        });
    }

    /**
     * 注册
     *
     * @param nickName 昵称
     * @param phone    手机号
     * @param password 密码
     */
    private void register(String nickName, String phone, String password, String pAvatar) {
        if (!TextUtils.isEmpty(pAvatar)) {
            pAvatar = "avatar";
        }
//        public void add(String vId, String vName, String vPassword,String vPhone,String vAvatar){
        //          db.execSQL("INSERT INTO UUSER(sv_userid, sv_name, sv_password, sv_phone, sv_avatar, sv_1, sv_2, sv_3) VALUES(?,?,?,?,?,?,?,?)",
        //             new Object[]{vId,     vName,  vPassword,    vPhone,   vAvatar,  "",   "",   ""});
        int kk=1;
        UChatDB db = UChatDB.getDatabase(this);
        UserDao dao = db.getUserDao();

        //User nUser = New() User().add(phone, nickName, MD5Util.encode(password, "utf8"), phone, pAvatar);
        User user = new User(phone, nickName, phone, password, pAvatar);
        dao.insert(user);

        startActivity(new Intent(L2_RegisterActivity.this, L4_MainActivity.class));
        finish();
    }

    /**
     * 表单是否填充完成(昵称,手机号,密码,是否同意协议)
     */
    private void checkSubmit() {
        boolean nickNameHasText = mNickNameEt.getText().toString().length() > 0;
        boolean phoneHasText = mPhoneEt.getText().toString().length() > 0;
        boolean passwordHasText = mPasswordEt.getText().toString().length() > 0;
        if (nickNameHasText && phoneHasText && passwordHasText && mIsAgree) {
            // 注册按钮可用
            mRegisterBtn.setBackgroundColor(getColor(R.color.register_btn_bg_enable));
            mRegisterBtn.setTextColor(getColor(R.color.register_btn_text_enable));
            mRegisterBtn.setEnabled(true);
        } else {
            // 注册按钮不可用
            mRegisterBtn.setBackgroundColor(getColor(R.color.register_btn_bg_disable));
            mRegisterBtn.setTextColor(getColor(R.color.register_btn_text_disable));
            mRegisterBtn.setEnabled(false);
        }
    }
}
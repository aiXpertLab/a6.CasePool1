package com.reapex.sv.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.L4_MainActivity;
import com.reapex.sv.R;
import com.reapex.sv.adapter.PickContactAdapter;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.entitydaodb.User;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.utils.VolleyUtil;
import com.reapex.sv.widget.LoadingDialog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * 创建群组
 * 进入此页面的三种场景
 * 场景1: 首页发起群聊，此时所有friend都是unchecked状态
 * 场景2: 单聊设置发起群聊, 此时有一个用户(单聊对象)处于checked状态，其他friend都是unchecked状态
 * 场景3: 群聊设置拉人，此时此群里所有人都处于checked状态，其他friend都是unchecked状态
 *
 * @author zhou
 */
public class CreateGroupActivity extends L0_BaseActivity {

    private PickContactAdapter contactAdapter;
    private ListView listView;

    // 创建群聊类型
    // 1.首页发起群聊
    // 2.单聊拉人
    // 3.群聊拉人
    String createType;

    // 单聊拉人
    String firstUserId;
    String firstUserNickName;

    // 群聊拉人
    String groupId;
    List<String> firstUserIdList;
    List<String> firstUserNickNameList;

    // 可滑动的显示选中用户的View
    private LinearLayout mAvatarListLl;
    private ImageView mSearchIv;
    private TextView mSaveTv;


    private List<String> checkedUserIdList = new ArrayList<>();
    private List<User> checkedUserList = new ArrayList<>();

    private List<String> initUserIdList = new ArrayList<>();
    private int totalCount = 0;

    private VolleyUtil volleyUtil;
    LoadingDialog loadingDialog;

    private EditText mSearchEt;
    private UserDao mUserDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        //initStatusBar();

//sv        mUserDao = new UserDao();
        volleyUtil = VolleyUtil.getInstance(this);
        loadingDialog = new LoadingDialog(CreateGroupActivity.this);
        createType = getIntent().getStringExtra("createType");
        if (L0_Constant.CREATE_GROUP_TYPE_FROM_SINGLE.equals(createType)) {
            firstUserId = getIntent().getStringExtra("userId");
            firstUserNickName = getIntent().getStringExtra("userNickName");

            initUserIdList.add(firstUserId);
        } else if (L0_Constant.CREATE_GROUP_TYPE_FROM_GROUP.equals(createType)) {
            groupId = getIntent().getStringExtra("groupId");
            firstUserIdList = getIntent().getStringArrayListExtra("userIdList");
            firstUserNickNameList = getIntent().getStringArrayListExtra("userIdNickNameList");

            initUserIdList.addAll(firstUserIdList);
        }
//sv        final List<User> friendList = mUserDao.getAllFriendList();
        // 对list进行排序
//sv        Collections.sort(friendList, new PinyinComparator() {        });
        mAvatarListLl = findViewById(R.id.ll_avatar_list);
        mSearchIv = findViewById(R.id.iv_search);
        mSaveTv = findViewById(R.id.tv_save);
        mSearchEt = findViewById(R.id.et_search);

        listView = findViewById(R.id.lv_friends);
//sv        contactAdapter = new PickContactAdapter(this,                R.layout.item_pick_contact_list, friendList, checkedUserIdList, initUserIdList);
        listView.setAdapter(contactAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//sv                final User friend = friendList.get(position);
                CheckBox mPickFriendCb = view.findViewById(R.id.cb_pick_friend);
                boolean isEnabled = mPickFriendCb.isEnabled();
                boolean isChecked = mPickFriendCb.isChecked();
                if (isEnabled) {
                    if (isChecked) {
                        mPickFriendCb.setChecked(false);
//sv                        removeCheckedImage(friend.getUserId(), friend);
                    } else {
                        mPickFriendCb.setChecked(true);
                        //sv                    addCheckedImage(friend.getUserAvatar(), friend.getUserId(), friend);
                    }
                }
            }
        });

        mSaveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (L0_Constant.CREATE_GROUP_TYPE_FROM_SINGLE.equals(createType)) {
                    loadingDialog.setMessage("正在创建群聊...");
                    loadingDialog.show();
                    createGroup();
                } else if (L0_Constant.CREATE_GROUP_TYPE_FROM_GROUP.equals(createType)) {
                    loadingDialog.setMessage("正在添加联系人...");
                    loadingDialog.show();
                    addGroupMembers(groupId);
                }
            }
        });

    }

    public void back(View view) {
        finish();
    }

    private void addCheckedImage(String userAvatar, final String userId, final User friend) {
        // 是否已包含
        if (checkedUserIdList.contains(userId)) {
            return;
        }
        totalCount++;
        checkedUserIdList.add(userId);
        checkedUserList.add(friend);
        // 包含TextView的LinearLayout
        // 参数设置
        android.widget.LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        View view = LayoutInflater.from(this).inflate(
                R.layout.item_create_group_header, null);
        SimpleDraweeView mAvatarSdv = view.findViewById(R.id.sdv_avatar);
        mAvatarSdv.setImageURI(Uri.parse(userAvatar));
        menuLinerLayoutParames.setMargins(6, 0, 6, 0);

        // 设置id，方便后面删除
        view.setTag(userId);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeCheckedImage(userId, friend);
                contactAdapter.notifyDataSetChanged();
            }
        });
        mAvatarListLl.addView(view, menuLinerLayoutParames);
        if (totalCount > 0) {
            if (mSearchIv.getVisibility() == View.VISIBLE) {
                mSearchIv.setVisibility(View.GONE);
            }

            mSaveTv.setText("确定(" + totalCount + ")");
            mSaveTv.setEnabled(true);
            mSaveTv.setTextColor(0xFFFFFFFF);
        }
    }

    private void removeCheckedImage(String userId, User friend) {
        View view = mAvatarListLl.findViewWithTag(userId);
        mAvatarListLl.removeView(view);
        totalCount--;
        checkedUserIdList.remove(userId);
        checkedUserList.remove(friend);
        mSaveTv.setText("确定(" + totalCount + ")");
        if (totalCount <= 0) {
            if (mSearchIv.getVisibility() == View.GONE) {
                mSearchIv.setVisibility(View.VISIBLE);
            }
            mSaveTv.setText("确定");
            mSaveTv.setEnabled(false);
            mSaveTv.setTextColor(0xFFD0EFC6);
        }
    }

    /**
     * 创建群组
     */
    private void createGroup() {
        String url = L0_Constant.BASE_URL + "groups";
        Map<String, String> paramMap = new HashMap<>();
        User user = L0_SharedPreferences.getInstance().getUser();
        List<String> pickedUserIdList = new ArrayList<>();
        pickedUserIdList.addAll(checkedUserIdList);
        pickedUserIdList.add(firstUserId);

        StringBuffer pickedUserIdBuffer = new StringBuffer();
        if (null != pickedUserIdList && pickedUserIdList.size() > 0) {
            for (String pickedUserId : pickedUserIdList) {
                pickedUserIdBuffer.append(pickedUserId);
                pickedUserIdBuffer.append(",");
            }
            pickedUserIdBuffer.deleteCharAt(pickedUserIdBuffer.length() - 1);
        }

        final StringBuffer pickedUserNickNameBuffer = new StringBuffer();
        if (null != checkedUserList && checkedUserList.size() > 0) {
            for (User checkedUser : checkedUserList) {
//sv                pickedUserNickNameBuffer.append(checkedUser.getUserName());
                pickedUserNickNameBuffer.append("、");
            }
            pickedUserNickNameBuffer.append(firstUserNickName);
        }

        String userIds = pickedUserIdBuffer.toString();
        final String groupName = pickedUserNickNameBuffer.toString();
//sv        paramMap.put("owner", user.getUserId());
        paramMap.put("groupName", groupName);
        paramMap.put("desc", "");
        paramMap.put("userIds", userIds);

        volleyUtil.httpPostRequest(url, paramMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.dismiss();
                Intent intent = new Intent(CreateGroupActivity.this, L4_MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.dismiss();
                if (volleyError instanceof NetworkError) {
                    Toast.makeText(CreateGroupActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                    return;
                } else if (volleyError instanceof TimeoutError) {
                    Toast.makeText(CreateGroupActivity.this, R.string.network_time_out, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void addGroupMembers(final String groupId) {
        String url = L0_Constant.BASE_URL + "groups/" + groupId + "/members";
        Map<String, String> paramMap = new HashMap<>();
        StringBuffer pickedUserIdBuffer = new StringBuffer();
        if (null != checkedUserIdList && checkedUserIdList.size() > 0) {
            for (String checkedUserId : checkedUserIdList) {
                pickedUserIdBuffer.append(checkedUserId);
                pickedUserIdBuffer.append(",");
            }
            pickedUserIdBuffer.deleteCharAt(pickedUserIdBuffer.length() - 1);
        }
        paramMap.put("addUserIds", pickedUserIdBuffer.toString());

        volleyUtil.httpPostRequest(url, paramMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingDialog.dismiss();
                Intent intent = new Intent(CreateGroupActivity.this, L4_MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loadingDialog.dismiss();
                if (volleyError instanceof NetworkError) {
                    Toast.makeText(CreateGroupActivity.this, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
                    return;
                } else if (volleyError instanceof TimeoutError) {
                    Toast.makeText(CreateGroupActivity.this, R.string.network_time_out, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}

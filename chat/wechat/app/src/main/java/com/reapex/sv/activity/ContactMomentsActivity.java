package com.reapex.sv.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reapex.sv.L0_BaseActivity;
import com.reapex.sv.R;
import com.reapex.sv.adapter.UserFriendsCircleAdapter;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.daodb.UserFriendsCircleDao;
import com.reapex.sv.entitydaodb.UserFriendsCircle;
import com.reapex.sv.utils.VolleyUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class ContactMomentsActivity extends L0_BaseActivity {

    private ListView mFriendsCircleLv;
    UserFriendsCircleAdapter mAdapter;
    RefreshLayout mRefreshLayout;
    private UserDao mUserDao;
    long mTimeStamp;
    private VolleyUtil mVolleyUtil;
    private UserFriendsCircleDao mUserFriendsCircleDao;

    private List<UserFriendsCircle> mUserFriendsCircleList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_moments);
//sv        mUserDao = new UserDao();
        mTimeStamp = 0L;
        mVolleyUtil = VolleyUtil.getInstance(this);
        mUserFriendsCircleDao = new UserFriendsCircleDao();
        initView();
    }

    private void initView() {
        final String userId = getIntent().getStringExtra("userId");
//sv        final User friend = mUserDao.getUserById(userId);

        mFriendsCircleLv = findViewById(R.id.lv_friends_circle);

        mUserFriendsCircleList = mUserFriendsCircleDao.getUserFriendsCircleList(userId, L0_Constant.DEFAULT_PAGE_SIZE, mTimeStamp);
        mAdapter = new UserFriendsCircleAdapter(mUserFriendsCircleList, this);
        mFriendsCircleLv.setAdapter(mAdapter);
        View headerView = LayoutInflater.from(this).inflate(R.layout.item_friends_circle_header, null);
        mFriendsCircleLv.addHeaderView(headerView, null, false);
        mFriendsCircleLv.setHeaderDividersEnabled(false);

        // headerView
        ImageView mCoverIv = headerView.findViewById(R.id.iv_cover);
        mCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        TextView mNickNameTv = headerView.findViewById(R.id.tv_nick_name);
        //sv    mNickNameTv.setText(friend.getUserName());

        SimpleDraweeView mAvatarSdv = headerView.findViewById(R.id.sdv_avatar);
//sv        if (!TextUtils.isEmpty(friend.getUserAvatar())) {
        //sv        mAvatarSdv.setImageURI(Uri.parse(friend.getUserAvatar()));
//sv        }

        //sv    getUserFriendsCircleList(friend.getUserId(), L0_Constant.DEFAULT_PAGE_SIZE, false);

//        // 上拉加载，下拉刷新
//        mRefreshLayout = findViewById(R.id.srl_friends_circle);
//        mRefreshLayout.setPrimaryColorsId(android.R.color.black, android.R.color.white);
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                // 下拉刷新
//                getUserFriendsCircleList(friend.getUserId(), Constant.DEFAULT_PAGE_SIZE, false);
//            }
//        });
//        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                // 上拉加载
//                getUserFriendsCircleList(friend.getUserId(), Constant.DEFAULT_PAGE_SIZE, true);
//            }
//        });
    }

    public void back(View view) {
        finish();
    }

    private void getUserFriendsCircleList(final String userId, final int pageSize, final boolean isAdd) {
        mTimeStamp = isAdd ? mTimeStamp : 0L;
        String url = L0_Constant.BASE_URL + "users/" + userId + "/friendsCircle?pageSize=" + pageSize + "&timestamp=" + mTimeStamp;

        mVolleyUtil.httpGetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                if (isAdd) {
//                    // 上拉加载
//                    mRefreshLayout.finishLoadMore();
//                } else {
//                    // 下拉刷新
//                    mRefreshLayout.finishRefresh();
//                }

                List<UserFriendsCircle> list = JSONArray.parseArray(response, UserFriendsCircle.class);
                if (null != list && list.size() > 0) {
                    for (UserFriendsCircle userFriendsCircle : list) {
                        UserFriendsCircle checkUserFriendsCircle = mUserFriendsCircleDao.getUserFriendsCircleByCircleId(userFriendsCircle.getCircleId());

                        if (null == checkUserFriendsCircle) {
                            // 不存在,插入
                            mUserFriendsCircleDao.addUserFriendsCircle(userFriendsCircle);
                        } else {
                            // 存在,修改
                            userFriendsCircle.setId(checkUserFriendsCircle.getId());
                            mUserFriendsCircleDao.addUserFriendsCircle(userFriendsCircle);
                        }
                    }
                    List<UserFriendsCircle> friendsCircleList = mUserFriendsCircleDao.getUserFriendsCircleList(userId, pageSize, mTimeStamp);
                    if (isAdd) {
                        // 上拉加载
                        mAdapter.addData(friendsCircleList);
                    } else {
                        // 下拉刷新
                        mAdapter.setData(friendsCircleList);
                    }
                    mTimeStamp = friendsCircleList.get(friendsCircleList.size() - 1).getTimestamp();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (isAdd) {
                    // 上拉加载
                    mRefreshLayout.finishLoadMore();
                } else {
                    // 下拉刷新
                    mRefreshLayout.finishRefresh();
                }
                // 网络错误，从本地读取
                List<UserFriendsCircle> friendsCircleList = mUserFriendsCircleDao.getUserFriendsCircleList(userId, pageSize, mTimeStamp);
                if (null != friendsCircleList && friendsCircleList.size() > 0) {
                    if (isAdd) {
                        // 上拉加载
                        mAdapter.addData(friendsCircleList);
                    } else {
                        // 下拉刷新
                        mAdapter.setData(friendsCircleList);
                    }
                    mTimeStamp = friendsCircleList.get(friendsCircleList.size() - 1).getTimestamp();
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}

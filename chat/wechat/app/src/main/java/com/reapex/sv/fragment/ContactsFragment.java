package com.reapex.sv.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reapex.sv.R;
import com.reapex.sv.user.UserInfoActivity;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.L0_SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * 通讯录
 *
 * @author zhou
 */
public class ContactsFragment extends Fragment {
    private FriendsAdapter mFriendsAdapter;

    private ListView mFriendsLv;
    private LayoutInflater mInflater;

    // 好友总数    private TextView mFriendsCountTv;

    TextView mNewFriendsUnreadNumTv;

    // 好友列表    private List<L_User> mFriendList;

    // 星标好友列表    private List<L_User> mStarFriendList;

    private UserDao mUserDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.a_fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        L0_SharedPreferences.getInstance().init(getActivity());
        //sv mUserDao = new UserDao();
        mFriendsLv = getView().findViewById(R.id.lv_friends);
        mInflater = LayoutInflater.from(getActivity());
        View headerView = mInflater.inflate(R.layout.a_item_contacts_header, null);
        mFriendsLv.addHeaderView(headerView);
        View footerView = mInflater.inflate(R.layout.item_contacts_footer, null);
        mFriendsLv.addFooterView(footerView);

        //sv mFriendsCountTv = footerView.findViewById(R.id.tv_total);

        RelativeLayout mNewFriendsRl = headerView.findViewById(R.id.rl_new_friends);
        mNewFriendsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 1;
                startActivity(new Intent(getActivity(), UserInfoActivity.class));
//sv                startActivity(new Intent(getActivity(), NewFriendsActivity.class));
//sv                PreferencesUtil.getInstance().setNewFriendsUnreadNumber(0);
            }
        });

        RelativeLayout mGroupChatsRl = headerView.findViewById(R.id.rl_group_chats);
        mGroupChatsRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mNewFriendsUnreadNumTv = headerView.findViewById(R.id.tv_new_friends_unread);

//        mStarFriendList = mUserDao.getAllStarredContactList();
//        mFriendList = mUserDao.getAllFriendList();
        // 对list进行排序
        //svCollections.sort(mFriendList, new PinyinComparator() {        });

//sv        mStarFriendList.addAll(mFriendList);

//sv         mFriendsAdapter = new FriendsAdapter(getActivity(), R.layout.item_contacts, mStarFriendList);
        mFriendsLv.setAdapter(mFriendsAdapter);

//        mFriendsCountTv.setText(mUserDao.getContactsCount() + "位联系人");

        mFriendsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    public void refreshNewFriendsUnreadNum() {
        int newFriendsUnreadNum = L0_SharedPreferences.getInstance().getNewFriendsUnreadNumber();
        if (newFriendsUnreadNum > 0) {
            mNewFriendsUnreadNumTv.setVisibility(View.VISIBLE);
            mNewFriendsUnreadNumTv.setText(String.valueOf(newFriendsUnreadNum));
        } else {
            mNewFriendsUnreadNumTv.setVisibility(View.GONE);
        }
    }

    public void refreshFriendsList() {
    }
}

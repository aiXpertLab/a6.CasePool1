package com.reapex.sv.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.reapex.sv.R;
import com.reapex.sv.adapter.L_ConversationAdapter;
import com.reapex.sv.entitydaodb.Chat;
import com.reapex.sv.entitydaodb.ChatAdapter;
import com.reapex.sv.entitydaodb.UserDao;

import androidx.fragment.app.Fragment;

import java.util.List;

public class ChatsFragment extends Fragment {

    private ListView mConversationListView;
    private L_ConversationAdapter mConversationAdapter;
    private ChatAdapter mChatAdapter;
    List<Chat> mChatList;

    private static final int REFRESH_CONVERSATION_LIST = 0x3000;
    private UserDao mUserDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.a_fragment_conversation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //sv mUserDao = new UserDao();
        mConversationListView = getView().findViewById(R.id.lv_conversation);
        mChatAdapter  = new ChatAdapter(getActivity(), mChatList);
        mConversationListView.setAdapter(mChatAdapter);
//        mConversationAdapter = new L_ConversationAdapter(getActivity(), mConversationList);
  //      mConversationLv.setAdapter(mConversationAdapter);
    }

    public void refreshConversationList() {
        mHandler.sendMessage(mHandler.obtainMessage(REFRESH_CONVERSATION_LIST));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REFRESH_CONVERSATION_LIST) {
            }
        }
    };
}

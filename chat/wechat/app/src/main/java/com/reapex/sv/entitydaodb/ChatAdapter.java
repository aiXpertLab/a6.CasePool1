package com.reapex.sv.entitydaodb;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxd.chatview.moudle.ChatView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.R;
import com.reapex.sv.entitydaodb.enums.MessageStatus;
import com.reapex.sv.user.UserInfoActivity;
import com.reapex.sv.utils.TimestampUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    User user;

    private List<Chat> chatList;
    private boolean isSender;

    public ChatAdapter(Context context, List<Chat> chatList) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        L0_SharedPreferences.getInstance().init(context);
        user = L0_SharedPreferences.getInstance().getUser();
        this.chatList = chatList;
    }

    public void setData(List<Chat> chatList) {
        this.chatList = chatList;
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;       //MESSAGE_TYPE_SENT_TEXT
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Chat chat = chatList.get(position);
        ChatAdapter.ViewHolder viewHolder;
        isSender = user.getUserId().equals(chat.getFromUserId());
        if (convertView == null) {
            viewHolder = new ChatAdapter.ViewHolder();
            viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
            viewHolder.mContentTv = convertView.findViewById(R.id.tv_chat_content);
            viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
            viewHolder.mSendingPb = convertView.findViewById(R.id.pb_sending);
            viewHolder.mStatusIv = convertView.findViewById(R.id.iv_msg_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChatAdapter.ViewHolder) convertView.getTag(); //// get object from View to ViewHolder
        }
        handleTextMessage(chat, viewHolder, position);

        // 点击头像进入用户详情页
        // 做非空判断防止通知类消息
        if (null != viewHolder.mAvatarSdv) {
            viewHolder.mAvatarSdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 进入自己详情页
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("userId", chat.getFromUserId());
                    mContext.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        // text
        TextView mTimeStampTv, mContentTv;
        SimpleDraweeView mAvatarSdv;
        ProgressBar mSendingPb;
        ImageView mStatusIv;

        RelativeLayout mMessageRl;

        // 聊天内容
        ChatView mChatContentCv;
    }

    private View createViewByMessageType(String messageType, boolean isSender) {
        return isSender ? inflater.inflate(R.layout.a_item_sent_text, null) :
                inflater.inflate(R.layout.a_item_received_text, null);
    }

    private void sendMessage(String targetType, String targetId, String fromId, String msgType, String body, final int messageIndex) {
        String url = L0_Constant.BASE_URL + "messages";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("targetType", targetType);
        paramMap.put("targetId", targetId);
        paramMap.put("fromId", fromId);
        paramMap.put("msgType", msgType);
        paramMap.put("body", body);
    }

    /**
     * 处理文字消息
     *
     * @param chat       消息
     * @param viewHolder viewHolder
     * @param position   位置
     */
    private void handleTextMessage(final Chat chat, ChatAdapter.ViewHolder viewHolder, final int position) {
        // 好友头像和昵称从sqlite中读取，防止脏数据
        //sv User friend = mUserDao.getUserById(message.getFromUserId());

        if (chat.getStatus() == MessageStatus.SENDING.value()) {
            viewHolder.mSendingPb.setVisibility(View.VISIBLE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (chat.getStatus() == MessageStatus.SEND_SUCCESS.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (chat.getStatus() == MessageStatus.SEND_FAIL.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.VISIBLE);
        }

        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(chat.getTimestamp()));
        chat.setContent("ddd");
        viewHolder.mContentTv.setText(chat.getContent());

        if (user.getUserId().equals(chat.getFromUserId())) {
            if (!TextUtils.isEmpty(user.getUserAvatar())) {
                viewHolder.mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
            }
        }

        if (position != 0) {
            Chat lastChat = chatList.get(position - 1);

            if (chat.getTimestamp() - lastChat.getTimestamp() < 10 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }
    }

    private void handleEventNotificationMessage(final L_Message message, L_MessageAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(message.getTimestamp()));
        viewHolder.mSystemMessageTv.setVisibility(View.VISIBLE);
        viewHolder.mSystemMessageTv.setText(message.getContent());
        viewHolder.mMessageRl.setVisibility(View.GONE);

        if (position != 0) {
            Chat lastChat = chatList.get(position - 1);
            if (message.getTimestamp() - lastChat.getTimestamp() < 10 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }
    }
}

package com.reapex.sv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reapex.sv.R;
import com.reapex.sv.daodb.MessageDao;
import com.reapex.sv.entitydaodb.Chat;
import com.reapex.sv.entitydaodb.UserDao;
import com.reapex.sv.utils.TimestampUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.List;

public class L_ConversationAdapter extends BaseAdapter {

    private List<Chat> conversationList;
    private Context mContext;
    private LayoutInflater inflater;
    private MessageDao messageDao;
    private UserDao mUserDao;

    public L_ConversationAdapter(Context context, List<Chat> conversationList) {
        this.mContext = context;
        this.conversationList = conversationList;
        inflater = LayoutInflater.from(context);
        messageDao = new MessageDao();
        //sv mUserDao = new UserDao();
    }

    public void setData(List<Chat> conversationList) {
        if (null != conversationList) {
            this.conversationList.clear();
            this.conversationList.addAll(conversationList);
        }
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Chat getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat conversation = conversationList.get(position);

        // 单聊
        convertView = creatConvertView(0);
        TextView mNickNameTv = convertView.findViewById(R.id.tv_nick_name);
        TextView mLastMsgTv = convertView.findViewById(R.id.tv_last_msg);
        SimpleDraweeView mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
        TextView mUnreadTv = convertView.findViewById(R.id.tv_unread);
        TextView mCreateTimeTv = convertView.findViewById(R.id.tv_create_time);

/*        mNickNameTv.setText(user.getUserName());
        if (!TextUtils.isEmpty(user.getUserAvatar())) {
            mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
        }
*/
        // 如果消息被清除
        // conversation.getLastestMessage() == null
        // conversation由极光维护
        // 如果消息被清除，conversation.getLastestMessage() == null
        // 这个时间不好显示
            mCreateTimeTv.setText(TimestampUtil.getTimePoint(new Date().getTime()));
        return convertView;
    }

    private View creatConvertView(int size) {
        View convertView;
        convertView = inflater.inflate(R.layout.item_conversation_single,null, false);
        return convertView;
    }

    private String generateGroupDesc(String myNickName, String... userNickNames) {
        StringBuffer groupDescBuffer = new StringBuffer();
        for (String userNickName : userNickNames) {
            if (!userNickName.equals(myNickName)) {
                groupDescBuffer.append(userNickName).append("、");
            }
        }
        if (groupDescBuffer.length() > 1) {
            groupDescBuffer.deleteCharAt(groupDescBuffer.length() - 1);
        }
        return groupDescBuffer.toString();
    }
}

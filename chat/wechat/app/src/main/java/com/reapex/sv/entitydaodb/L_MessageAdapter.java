package com.reapex.sv.entitydaodb;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
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

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reapex.sv.L0_Constant;
import com.reapex.sv.L0_SharedPreferences;
import com.reapex.sv.R;
import com.reapex.sv.user.UserInfoActivity;
import com.reapex.sv.activity.MessageBigImageActivity;
import com.reapex.sv.daodb.MessageDao;
import com.reapex.sv.entitydaodb.enums.MessageStatus;
import com.reapex.sv.utils.CalculateUtil;
import com.reapex.sv.utils.OssUtil;
import com.reapex.sv.utils.TimestampUtil;
import com.reapex.sv.utils.VolleyUtil;
import com.cxd.chatview.moudle.ChatView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
public class L_MessageAdapter extends BaseAdapter {
    private static final int DEFAULT_WIDTH_1 = 300;
    private static final int DEFAULT_WIDTH_2 = 400;
    private static final int DEFAULT_WIDTH_3 = 500;

    private static final int MESSAGE_TYPE_SENT_TEXT = 0;
    private static final int MESSAGE_TYPE_RECV_TEXT = 1;
    private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
    private static final int MESSAGE_TYPE_RECV_IMAGE = 3;
    private static final int MESSAGE_TYPE_SENT_LOCATION = 4;
    private static final int MESSAGE_TYPE_RECV_LOCATION = 5;

    private Context mContext;
    private LayoutInflater inflater;

    private List<L_Message> messageList;

    private User user;
    private VolleyUtil volleyUtil;
    private MessageDao messageDao;
    private UserDao mUserDao;

    private boolean isSender;

    public L_MessageAdapter(Context context, List<L_Message> messageList) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        L0_SharedPreferences.getInstance().init(context);
        user = L0_SharedPreferences.getInstance().getUser();
//        volleyUtil = VolleyUtil.getInstance(mContext);
        messageDao = new MessageDao();
        //sv mUserDao = new UserDao();
        this.messageList = messageList;
    }

    public void setData(List<L_Message> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        L_Message message = messageList.get(position);
        isSender = user.getUserId().equals(message.getFromUserId());
        if (L0_Constant.MSG_TYPE_TEXT.equals(message.getMessageType())) {
            return isSender ? MESSAGE_TYPE_SENT_TEXT : MESSAGE_TYPE_RECV_TEXT;
        } else if (L0_Constant.MSG_TYPE_IMAGE.equals(message.getMessageType())) {
            return isSender ? MESSAGE_TYPE_SENT_IMAGE : MESSAGE_TYPE_RECV_IMAGE;
        } else if (L0_Constant.MSG_TYPE_LOCATION.equals(message.getMessageType())) {
            return isSender ? MESSAGE_TYPE_SENT_LOCATION : MESSAGE_TYPE_RECV_LOCATION;
        }
        // invalid
        return -1;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final L_Message message = messageList.get(position);
        ViewHolder viewHolder;
        isSender = user.getUserId().equals(message.getFromUserId());
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = createViewByMessageType(message.getMessageType(), isSender);
            if (L0_Constant.MSG_TYPE_TEXT.equals(message.getMessageType())) {
                // 文字消息
                viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
                viewHolder.mContentTv = convertView.findViewById(R.id.tv_chat_content);
                viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
                viewHolder.mSendingPb = convertView.findViewById(R.id.pb_sending);
                viewHolder.mStatusIv = convertView.findViewById(R.id.iv_msg_status);

            } else if (L0_Constant.MSG_TYPE_IMAGE.equals(message.getMessageType())) {
                // 图片消息
                viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
                viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
                viewHolder.mImageContentSdv = convertView.findViewById(R.id.sdv_image_content);

                viewHolder.mSendingPb = convertView.findViewById(R.id.pb_sending);
                viewHolder.mStatusIv = convertView.findViewById(R.id.iv_msg_status);

            } else if (L0_Constant.MSG_TYPE_LOCATION.equals(message.getMessageType())) {
                // 定位消息
                viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
                viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
                viewHolder.mAddressTv = convertView.findViewById(R.id.tv_address);
                viewHolder.mAddressDetailTv = convertView.findViewById(R.id.tv_address_detail);
                viewHolder.mLocationImgSdv = convertView.findViewById(R.id.sdv_location_img);

                viewHolder.mSendingPb = convertView.findViewById(R.id.pb_sending);
                viewHolder.mStatusIv = convertView.findViewById(R.id.iv_msg_status);

                viewHolder.mChatContentCv = convertView.findViewById(R.id.cv_chat_content);

            } else if (L0_Constant.MSG_TYPE_SYSTEM.equals(message.getMessageType())) {
                viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
                viewHolder.mSystemMessageTv = convertView.findViewById(R.id.tv_system_message);
                viewHolder.mMessageRl = convertView.findViewById(R.id.rl_message);
            } else {
                // 默认文字信息
                viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
                viewHolder.mContentTv = convertView.findViewById(R.id.tv_chat_content);
                viewHolder.mAvatarSdv = convertView.findViewById(R.id.sdv_avatar);
                viewHolder.mSendingPb = convertView.findViewById(R.id.pb_sending);
                viewHolder.mStatusIv = convertView.findViewById(R.id.iv_msg_status);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (L0_Constant.MSG_TYPE_TEXT.equals(message.getMessageType())) {
            handleTextMessage(message, viewHolder, position);
        } else if (L0_Constant.MSG_TYPE_IMAGE.equals(message.getMessageType())) {
            handleImageMessage(message, viewHolder, position);
        } else if (L0_Constant.MSG_TYPE_LOCATION.equals(message.getMessageType())) {
            handleLocationMessage(message, viewHolder, position);
        } else if (L0_Constant.MSG_TYPE_SYSTEM.equals(message.getMessageType())) {
            handleEventNotificationMessage(message, viewHolder, position);
        } else {
            handleTextMessage(message, viewHolder, position);
        }

        if (L0_Constant.MSG_TYPE_TEXT.equals(message.getMessageType())) {

        } else if (L0_Constant.MSG_TYPE_IMAGE.equals(message.getMessageType())) {
            viewHolder.mImageContentSdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MessageBigImageActivity.class);
                    Map<String, Object> imageMap = JSON.parseObject(message.getMessageBody(), Map.class);
                    final String imgUrl = imageMap.get("imgUrl") == null ? "" : String.valueOf(imageMap.get("imgUrl"));
                    final String localPath = imageMap.get("localPath") == null ? "" : String.valueOf(imageMap.get("localPath"));
                    intent.putExtra("imgUrl", imgUrl);
                    intent.putExtra("localPath", localPath);
                    mContext.startActivity(intent);
                }
            });
        } else if (L0_Constant.MSG_TYPE_LOCATION.equals(message.getMessageType())) {

        } else {

        }

        // 点击头像进入用户详情页
        // 做非空判断防止通知类消息
        if (null != viewHolder.mAvatarSdv) {
            viewHolder.mAvatarSdv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 进入自己详情页
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtra("userId", message.getFromUserId());
                    mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        // text
        TextView mTimeStampTv;
        TextView mContentTv;
        SimpleDraweeView mAvatarSdv;
        ProgressBar mSendingPb;
        ImageView mStatusIv;

        RelativeLayout mMessageRl;

        // sys
        TextView mSystemMessageTv;

        // image
        SimpleDraweeView mImageContentSdv;

        // location
        // 地址
        TextView mAddressTv;
        // 详细地址
        TextView mAddressDetailTv;
        SimpleDraweeView mLocationImgSdv;

        // 聊天内容
        ChatView mChatContentCv;

    }

    private View createViewByMessageType(String messageType, boolean isSender) {
        if (L0_Constant.MSG_TYPE_TEXT.equals(messageType)) {
            return isSender ? inflater.inflate(R.layout.a_item_sent_text, null) :
                    inflater.inflate(R.layout.a_item_received_text, null);
        } else if (L0_Constant.MSG_TYPE_IMAGE.equals(messageType)) {
            return isSender ? inflater.inflate(R.layout.item_sent_image, null) :
                    inflater.inflate(R.layout.item_received_image, null);
        } else if (L0_Constant.MSG_TYPE_LOCATION.equals(messageType)) {
            return isSender ? inflater.inflate(R.layout.item_sent_location, null) :
                    inflater.inflate(R.layout.item_received_location, null);
        } else {
            return isSender ? inflater.inflate(R.layout.a_item_sent_text, null) :
                    inflater.inflate(R.layout.a_item_received_text, null);
        }
    }

    private void sendMessage(String targetType, String targetId, String fromId, String msgType, String body, final int messageIndex) {
        String url = L0_Constant.BASE_URL + "messages";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("targetType", targetType);
        paramMap.put("targetId", targetId);
        paramMap.put("fromId", fromId);
        paramMap.put("msgType", msgType);
        paramMap.put("body", body);

        volleyUtil.httpPostRequest(url, paramMap, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L_Message message = messageList.get(messageIndex);
                message = messageDao.getMessageByMessageId(message.getMessageId());
                message.setStatus(MessageStatus.SEND_SUCCESS.value());
                message.setTimestamp(new Date().getTime());
                messageList.set(messageIndex, message);

                L_Message.delete(message);
                message.setId(null);
                L_Message.save(message);
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L_Message message = messageList.get(messageIndex);
                message = messageDao.getMessageByMessageId(message.getMessageId());
                message.setStatus(MessageStatus.SEND_FAIL.value());
                message.setTimestamp(new Date().getTime());
                messageList.set(messageIndex, message);

                L_Message.delete(message);
                message.setId(null);
                L_Message.save(message);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 处理文字消息
     *
     * @param message    消息
     * @param viewHolder viewHolder
     * @param position   位置
     */
    private void handleTextMessage(final L_Message message, ViewHolder viewHolder, final int position) {
        // 好友头像和昵称从sqlite中读取，防止脏数据
        //sv User friend = mUserDao.getUserById(message.getFromUserId());

        if (message.getStatus() == MessageStatus.SENDING.value()) {
            viewHolder.mSendingPb.setVisibility(View.VISIBLE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (message.getStatus() == MessageStatus.SEND_SUCCESS.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (message.getStatus() == MessageStatus.SEND_FAIL.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.VISIBLE);
        }

        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(message.getTimestamp()));
        message.setContent("ddd");
        viewHolder.mContentTv.setText(message.getContent());

        if (user.getUserId().equals(message.getFromUserId())) {
            if (!TextUtils.isEmpty(user.getUserAvatar())) {
                viewHolder.mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
            }
        } else {

        }

        if (position != 0) {
            L_Message lastMessage = messageList.get(position - 1);

            if (message.getTimestamp() - lastMessage.getTimestamp() < 10 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }

        if (null != viewHolder.mStatusIv) {
            viewHolder.mStatusIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, Object> body = new HashMap<>();
                    body.put("extras", new HashMap<>());
                    body.put("text", message.getContent());
                    L_Message resendMessage = messageDao.getMessageByMessageId(message.getMessageId());
                    resendMessage.setStatus(MessageStatus.SENDING.value());
                    resendMessage.setTimestamp(new Date().getTime());
                    L_Message.delete(resendMessage);
                    resendMessage.setId(null);
                    L_Message.save(resendMessage);
                    // 重发消息移至最后
                    messageList.remove(position);
                    messageList.add(resendMessage);

                    notifyDataSetChanged();
                    String targetType = message.getTargetType();
                    if (L0_Constant.TARGET_TYPE_SINGLE.equals(targetType)) {
                        sendMessage(targetType, message.getToUserId(),
                                user.getUserId(), "text", JSON.toJSONString(body), messageList.size() - 1);
                    } else {
                        // 群聊
                        sendMessage(targetType, message.getGroupId(), user.getUserId(),
                                message.getMessageType(), JSON.toJSONString(body), messageList.size() - 1);
                    }
                }
            });
        }
    }

    /**
     * 处理图片消息
     *
     * @param message    消息
     * @param viewHolder viewHolder
     * @param position   位置
     */
    private void handleImageMessage(final L_Message message, final ViewHolder viewHolder, final int position) {
        // 好友头像和昵称从sqlite中读取，防止脏数据
        //sv User friend = mUserDao.getUserById(message.getFromUserId());

        // 消息状态
        if (message.getStatus() == MessageStatus.SENDING.value()) {
            viewHolder.mSendingPb.setVisibility(View.VISIBLE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (message.getStatus() == MessageStatus.SEND_SUCCESS.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (message.getStatus() == MessageStatus.SEND_FAIL.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.VISIBLE);
        }

        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(message.getTimestamp()));
        if (user.getUserId().equals(message.getFromUserId())) {
            if (!TextUtils.isEmpty(user.getUserAvatar())) {
                viewHolder.mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
            }
        } else {
            if (!TextUtils.isEmpty(message.getFromUserAvatar())) {

            }
        }
        Map<String, Object> imageMap = JSON.parseObject(message.getMessageBody(), Map.class);
        final String imgUrl = imageMap.get("imgUrl") == null ? "" : String.valueOf(imageMap.get("imgUrl"));
        final String localPath = imageMap.get("localPath") == null ? "" : String.valueOf(imageMap.get("localPath"));
        Uri uri;
        if (!TextUtils.isEmpty(localPath)) {
            // 本地读
            uri = Uri.fromFile(new File(localPath));
        } else {
            // 网络获取
            uri = Uri.parse(OssUtil.resize(imgUrl));
        }


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(viewHolder.mImageContentSdv.getController())
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {

                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        int originWidth = imageInfo.getWidth();
                        int originHeight = imageInfo.getHeight();
                        int adjustWidth;

                        if (originWidth < originHeight) {
                            adjustWidth = DEFAULT_WIDTH_1;
                        } else if (originWidth > originHeight) {
                            adjustWidth = DEFAULT_WIDTH_3;
                        } else {
                            adjustWidth = DEFAULT_WIDTH_2;
                        }

                        ViewGroup.LayoutParams params = viewHolder.mImageContentSdv.getLayoutParams();
                        params.width = adjustWidth;
                        Double resetHeight = CalculateUtil.mul(CalculateUtil.div(imageInfo.getHeight(), imageInfo.getWidth(), 5), adjustWidth);
                        params.height = resetHeight.intValue();
                        viewHolder.mImageContentSdv.setLayoutParams(params);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {

                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {

                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }

                    @Override
                    public void onRelease(String id) {

                    }
                })
                .setUri(uri)
                .build();
        viewHolder.mImageContentSdv.setController(controller);


        if (position != 0) {
            L_Message lastMessage = messageList.get(position - 1);

            if (message.getTimestamp() - lastMessage.getTimestamp() < 10 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }
    }

    private void handleEventNotificationMessage(final L_Message message, ViewHolder viewHolder, final int position) {
        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(message.getTimestamp()));
        viewHolder.mSystemMessageTv.setVisibility(View.VISIBLE);
        viewHolder.mSystemMessageTv.setText(message.getContent());
        viewHolder.mMessageRl.setVisibility(View.GONE);

        if (position != 0) {
            L_Message lastMessage = messageList.get(position - 1);

            if (message.getTimestamp() - lastMessage.getTimestamp() < 10 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 处理位置消息
     *
     * @param message    消息
     * @param viewHolder viewHolder
     * @param position   位置
     */
    private void handleLocationMessage(final L_Message message, ViewHolder viewHolder, final int position) {
        // 好友头像和昵称从sqlite中读取，防止脏数据
        //sv User friend = mUserDao.getUserById(message.getFromUserId());

        if (message.getStatus() == MessageStatus.SENDING.value()) {
            viewHolder.mSendingPb.setVisibility(View.VISIBLE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (message.getStatus() == MessageStatus.SEND_SUCCESS.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.GONE);
        } else if (message.getStatus() == MessageStatus.SEND_FAIL.value()) {
            viewHolder.mSendingPb.setVisibility(View.GONE);
            viewHolder.mStatusIv.setVisibility(View.VISIBLE);
        }

        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(message.getTimestamp()));

        if (user.getUserId().equals(message.getFromUserId())) {
            if (!TextUtils.isEmpty(user.getUserAvatar())) {
                viewHolder.mAvatarSdv.setImageURI(Uri.parse(user.getUserAvatar()));
            }
        } else {
        }

        if (position != 0) {
            L_Message lastMessage = messageList.get(position - 1);
            if (message.getTimestamp() - lastMessage.getTimestamp() < 10 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }

       }
    }


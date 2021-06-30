package com.hypech.case83_listview_room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class AMessage {
    @PrimaryKey(autoGenerate = true)
    int id;

    String messageId;
    String messageType;     //type
    String content;         //content
    String createTime;
    String fromUserId;
    String fromUserName;
    String fromUserAvatar;
    String toUserId;
    String toUserName;
    String toUserAvatar;
    String messageBody;
    String imageUrl;
    int status;
    long timestamp;
    String targetType;
    String groupId;

    @Ignore
    public static final int TYPE_RECEIVED = 0;

    @Ignore
    public static final int TYPE_SENT = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public AMessage(){}

    @Ignore
    public AMessage(String messageId, String messageType, String content, String fromUserId) {
        this.messageId = messageId;
        this.messageType = messageType;
        this.content = content;
        this.createTime = "19:30";
        this.fromUserId = fromUserId;
        this.fromUserName = "一般环境";
        this.fromUserAvatar = "fromUserAvatar";
        this.toUserId = "toUserId";
        this.toUserName = "见声";
        this.toUserAvatar = "toUserAvatar";
        this.messageBody = "messageBody";
        this.imageUrl = "imageUrl";
        this.status = 1;
        this.timestamp = 99999;
        this.targetType = "Friend";
        this.groupId = "GroupID";
    }


}
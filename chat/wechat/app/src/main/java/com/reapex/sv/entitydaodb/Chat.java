package com.reapex.sv.entitydaodb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Chat {
    @PrimaryKey(autoGenerate = true)
    int id;

    String fromUserId;
    String fromUserName;
    String fromUserAvatar;
    String toUserId;
    String toUserName;
    String toUserAvatar;

    String chatId;
    String chatType;
    String content;
    String createTime;
    String chatBody;
    String imageUrl;
    int status;
    long timestamp;
    String targetType;
    String groupId;

    public int getId() {        return id;    }
    public void setId(int id){               this.id = id;    }

    public String getchatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
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

    public String getChatBody() {
        return chatBody;
    }

    public void setChatBody(String chatBody) {
        this.chatBody = chatBody;
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
}
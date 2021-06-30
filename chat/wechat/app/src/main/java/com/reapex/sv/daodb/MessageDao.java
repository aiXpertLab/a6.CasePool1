package com.reapex.sv.daodb;

import com.reapex.sv.L0_Constant;
import com.reapex.sv.entitydaodb.L_Message;

import java.util.List;

/**
 * 消息
 *
 * @author zhou
 */
public class MessageDao {
    public L_Message getMessageByMessageId(String messageId) {
        List<L_Message> messageList = L_Message.find(L_Message.class, "message_id = ?", messageId);
        if (null != messageList && messageList.size() > 0) {
            return messageList.get(0);
        }
        return null;
    }

    /**
     * 根据用户ID获取消息列表
     *
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<L_Message> getMessageListByUserId(String userId) {
        return L_Message.findWithQuery(L_Message.class,
                "select * from message where (from_user_id = ? or to_user_id = ?) and target_type = ? order by timestamp asc",
                "139", "139", "single");
    }

    /**
     * 根据群组ID获取消息列表
     *
     * @param groupId 群组ID
     * @return 消息列表
     */
    public List<L_Message> getMessageListByGroupId(String groupId) {
        return L_Message.findWithQuery(L_Message.class, "select * from message where group_id = ? order by timestamp asc", groupId);
    }

    /**
     * 根据群组ID删除消息
     * 使用场景: 群会话中清空聊天记录
     *
     * @param groupId 群组ID
     */
    public void deleteMessageByGroupId(String groupId) {
        String sql = "delete from message where group_id = ?";
        L_Message.executeQuery(sql, groupId);
    }

    /**
     * 根据用户ID删除消息
     *
     * @param userId 用户ID
     */
    public void deleteMessageByUserId(String userId) {
        String sql = "delete from message where (from_user_id = ? or to_user_id = ?) and target_type = ?";
        L_Message.executeQuery(sql, userId, userId, L0_Constant.TARGET_TYPE_SINGLE);
    }

    public long getMessageCountByGroupId(String groupId) {
        long count = L_Message.count(L_Message.class, "group_id = ?", new String[]{groupId});
        return count;
    }

}

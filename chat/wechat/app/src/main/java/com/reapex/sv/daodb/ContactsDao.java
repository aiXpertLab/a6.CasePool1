package com.reapex.sv.daodb;

/**
 * 通讯录
 *
 * @author zhou
 */
public class ContactsDao {
    /**
     * 检查是否为好友
     *
     * @param contactsId 联系人用户ID
     * @return true:是 false:否
     *
    public boolean checkIsFriend(String contactsId) {
        List<L_UserBean> userList = L_UserBean.findWithQuery(L_UserBean.class,
                "select * from user where is_friend = ? and user_id = ?",
                L0_Constant.IS_FRIEND, contactsId);
        if (null != userList && userList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }*/
}

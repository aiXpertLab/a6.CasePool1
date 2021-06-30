package com.reapex.sv.daodb;

import com.reapex.sv.entitydaodb.FriendApply;

import java.util.List;

public class FriendApplyDao {
    public FriendApply getFriendApplyByApplyId(String applyId) {
        List<FriendApply> friendApplyList = FriendApply.find(FriendApply.class, "apply_id = ?", applyId);
        if (null != friendApplyList && friendApplyList.size() > 0) {
            return friendApplyList.get(0);
        }
        return null;
    }
}

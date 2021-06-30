package com.hypech.case83_listview_room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AUser {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String userId;
    private String userWxId;
    private String userType;        //"REG": 普通注册用户* "WEIXIN": 微信团队* "FILEHELPER": 文件传输助手
    private String userNickName;
    private String userPhone;
    private String userPassword;
    private String userImPassword;
    private String userAvatar;
    private String userHeader;
    private String userSex;
    private String userRegion;
    private String userSign;
    private String userEmail;
    private String userIsEmailLinked;

    private String userQqId;
    private String userQqPassword;
    private String userIsQqLinked;

    private String userQrCode;
    private String isFriend;

    private String userWxIdModifyFlag;
    private String userLastestCirclePhotos;

    /**
     * 联系人来源
     */
    private String userContactFrom;

    // 联系人相关
    private String userContactMobiles;
    private String userContactAlias;
    private String userContactDesc;

    /**
     * 联系人权限相关
     */
    private String userContactPrivacy;
    private String userContactHideMyPosts;
    private String userContactHideHisPosts;

    /**
     * 是否星标好友
     */
    private String isStarred;

    /**
     * 是否在黑名单中
     */
    private String isBlocked;

    /**
     * 用户联系人标签(json格式)
     */
    private String userContactTags;

    /**
     * 所有标签(json格式)
     */
    private String userTags;

    public int getId() {        return id;    }
    public void setId(int id){               this.id = id;    }

    public String getUserId() {
        return userId;
    }
    public void   setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserWxId() {
        return userWxId;
    }

    public void setUserWxId(String userWxId) {
        this.userWxId = userWxId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserImPassword() {
        return userImPassword;
    }

    public void setUserImPassword(String userImPassword) {
        this.userImPassword = userImPassword;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserRegion() {
        return userRegion;
    }

    public void setUserRegion(String userRegion) {
        this.userRegion = userRegion;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserIsEmailLinked() {
        return userIsEmailLinked;
    }

    public void setUserIsEmailLinked(String userIsEmailLinked) {
        this.userIsEmailLinked = userIsEmailLinked;
    }

    public String getUserQqId() {
        return userQqId;
    }

    public void setUserQqId(String userQqId) {
        this.userQqId = userQqId;
    }

    public String getUserQqPassword() {
        return userQqPassword;
    }

    public void setUserQqPassword(String userQqPassword) {
        this.userQqPassword = userQqPassword;
    }

    public String getUserIsQqLinked() {
        return userIsQqLinked;
    }

    public void setUserIsQqLinked(String userIsQqLinked) {
        this.userIsQqLinked = userIsQqLinked;
    }

    public String getUserQrCode() {
        return userQrCode;
    }

    public void setUserQrCode(String userQrCode) {
        this.userQrCode = userQrCode;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getUserWxIdModifyFlag() {
        return userWxIdModifyFlag;
    }

    public void setUserWxIdModifyFlag(String userWxIdModifyFlag) {
        this.userWxIdModifyFlag = userWxIdModifyFlag;
    }

    public String getUserLastestCirclePhotos() {
        return userLastestCirclePhotos;
    }

    public void setUserLastestCirclePhotos(String userLastestCirclePhotos) {
        this.userLastestCirclePhotos = userLastestCirclePhotos;
    }

    public String getUserContactFrom() {
        return userContactFrom;
    }

    public void setUserContactFrom(String userContactFrom) {
        this.userContactFrom = userContactFrom;
    }

    public String getUserContactMobiles() {
        return userContactMobiles;
    }

    public void setUserContactMobiles(String userContactMobiles) {
        this.userContactMobiles = userContactMobiles;
    }

    public String getUserContactAlias() {
        return userContactAlias;
    }

    public void setUserContactAlias(String userContactAlias) {
        this.userContactAlias = userContactAlias;
    }

    public String getUserContactDesc() {
        return userContactDesc;
    }

    public void setUserContactDesc(String userContactDesc) {
        this.userContactDesc = userContactDesc;
    }

    public String getUserContactPrivacy() {
        return userContactPrivacy;
    }

    public void setUserContactPrivacy(String userContactPrivacy) {
        this.userContactPrivacy = userContactPrivacy;
    }

    public String getUserContactHideMyPosts() {
        return userContactHideMyPosts;
    }

    public void setUserContactHideMyPosts(String userContactHideMyPosts) {
        this.userContactHideMyPosts = userContactHideMyPosts;
    }

    public String getUserContactHideHisPosts() {
        return userContactHideHisPosts;
    }

    public void setUserContactHideHisPosts(String userContactHideHisPosts) {
        this.userContactHideHisPosts = userContactHideHisPosts;
    }

    public String getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(String isStarred) {
        this.isStarred = isStarred;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getUserContactTags() {
        return userContactTags;
    }

    public void setUserContactTags(String userContactTags) {
        this.userContactTags = userContactTags;
    }


    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
    }

    public AUser(String userNickName, String userPhone, String userPassword, String userAvatar) {
        this.userId      = userPhone;
        this.userNickName= userNickName;
        this.userPhone   = userPhone;
        this.userPassword= userPassword;
        this.userAvatar  = userAvatar;
        this.userWxId    = userPhone;
        this.userImPassword= userPassword;
        this.isFriend   ="Y";
        this.isStarred  ="Y";
        this.isBlocked  ="N";

        this.userType   ="REG";        //"REG": 普通注册用户* "WEIXIN": 微信团队* "FILEHELPER": 文件传输助手
        this.userHeader ="Header";
        this.userSex    ="F";
        this.userRegion ="86";
        this.userQrCode ="QR Code";
        this.userSign   ="F";
        this.userEmail  ="860563368@qq.com";
        this.userIsEmailLinked="Y";
        this.userQqId   ="860563368";
        this.userQqPassword="Y";;
        this.userIsQqLinked="Y";;
        this.userWxIdModifyFlag="Y";;
        this.userLastestCirclePhotos="Y";;
        this.userContactFrom="SV public";;
        this.userContactMobiles="Y";;
        this.userContactAlias="None";;
        this.userContactDesc="contact desc";;
        this.userContactPrivacy="Y";
        this.userContactHideMyPosts="N";
        this.userContactHideHisPosts="N";
        this.userContactTags="Friend";
        this.userTags="Friend";
    }
}

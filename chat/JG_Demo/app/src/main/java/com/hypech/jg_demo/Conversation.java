package com.hypech.jg_demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Conversation {
    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package cn.jpush.im.android.api.model;

import cn.jmessage.a.d.e;
import cn.jmessage.biz.a.a;
import cn.jmessage.biz.g.c;
import cn.jmessage.support.google.gson.JsonObject;
import cn.jmessage.support.google.gson.JsonPrimitive;
import cn.jpush.im.android.api.content.MessageContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.ContentType;
import cn.jpush.im.android.api.enums.ConversationType;
import cn.jpush.im.android.api.exceptions.JMFileSizeExceedException;
import cn.jpush.im.api.BasicCallback;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

    public abstract class Conversation implements Serializable {
        private static final String TAG;
        protected String id;
        protected ConversationType type;
        protected String targetId;
        protected Object targetInfo;
        protected String title;
        protected int unReadMsgCnt;
        protected File avatar;
        protected Message latestMessage;
        protected String latestText;
        protected ContentType latestType;
        protected long lastMsgDate;
        protected String extra;
        private static final String[] z;

        public Conversation() {
            this.type = ConversationType.single;
            this.targetId = "";
            this.title = "";
            this.avatar = null;
            this.latestText = "";
            this.latestType = ContentType.text;
            this.extra = "";
        }

        public String getId() {
            return this.id;
        }

        public ConversationType getType() {
            return this.type;
        }

        public String getExtra() {
            return this.extra;
        }

        /** @deprecated */
        @Deprecated
        public String getTargetId() {
            return this.targetId;
        }

        public abstract String getTargetAppKey();

        /** @deprecated */
        @Deprecated
        public String getLatestText() {
            if (this.latestMessage != null) {
                switch(this.latestMessage.getContentType()) {
                    case text:
                        TextContent var1 = (TextContent)this.latestMessage.getContent();
                        this.latestText = var1.getText();
                        break;
                    default:
                        this.latestText = "";
                }
            }

            return this.latestText;
        }

        /** @deprecated */
        @Deprecated
        public long getLastMsgDate() {
            if (this.latestMessage != null) {
                this.lastMsgDate = this.latestMessage.getCreateTime();
            }

            return this.lastMsgDate;
        }

        /** @deprecated */
        @Deprecated
        public ContentType getLatestType() {
            if (this.latestMessage != null) {
                this.latestType = this.latestMessage.getContentType();
            }

            return this.latestType;
        }

        public int getUnReadMsgCnt() {
            return this.unReadMsgCnt < 0 ? 0 : this.unReadMsgCnt;
        }

        public abstract boolean setUnReadMessageCnt(int var1);

        public abstract String getTitle();

        public File getAvatarFile() {
            return this.avatar;
        }

        public abstract Message getLatestMessage();

        public abstract Object getTargetInfo();

        public static Conversation createGroupConversation(long var0) {
            return a.a().a(var0);
        }

        public static Conversation createSingleConversation(String var0) {
            return a.a().a(var0, cn.jmessage.b.a.a.a());
        }

        public static Conversation createSingleConversation(String var0, String var1) {
            return a.a().a(var0, var1);
        }

        public static Conversation createChatRoomConversation(long var0) {
            return a.a().a(ConversationType.chatroom, String.valueOf(var0), "");
        }

        /** @deprecated */
        @Deprecated
        public static Conversation createConversation(ConversationType var0, long var1) {
            return createConversation(var0, String.valueOf(var1));
        }

        /** @deprecated */
        @Deprecated
        public static Conversation createConversation(ConversationType var0, String var1) {
            return a.a().a(var0, var1, cn.jmessage.b.a.a.a());
        }

        public abstract boolean resetUnreadCount();

        public abstract Message getMessage(int var1);

        public abstract Message getMessage(long var1);

        public abstract List<Message> getAllMessage();

        public abstract List<Message> getMessagesFromOldest(int var1, int var2);

        public abstract List<Message> getMessagesFromNewest(int var1, int var2);

        public abstract boolean deleteMessage(int var1);

        public abstract boolean deleteAllMessage();

        public abstract boolean updateConversationExtra(String var1);

        public abstract boolean updateMessageExtra(Message var1, String var2, String var3);

        public abstract boolean updateMessageExtra(Message var1, String var2, Number var3);

        public abstract boolean updateMessageExtra(Message var1, String var2, Boolean var3);

        public abstract boolean updateMessageExtras(Message var1, Map<String, String> var2);

        public abstract Message createSendMessage(MessageContent var1);

        public abstract Message createSendMessage(MessageContent var1, String var2);

        public abstract Message createSendMessage(MessageContent var1, List<UserInfo> var2, String var3);

        public abstract Message createSendMessageAtAllMember(MessageContent var1, String var2);

        public abstract Message createSendTextMessage(String var1);

        public abstract Message createSendTextMessage(String var1, String var2);

        public abstract Message createSendImageMessage(File var1) throws FileNotFoundException;

        public abstract Message createSendImageMessage(File var1, String var2) throws FileNotFoundException;

        public abstract Message createSendVoiceMessage(File var1, int var2) throws FileNotFoundException;

        public abstract Message createSendVoiceMessage(File var1, int var2, String var3) throws FileNotFoundException;

        public abstract Message createSendCustomMessage(Map<? extends String, ? extends String> var1);

        public abstract Message createSendCustomMessage(Map<? extends String, ? extends String> var1, String var2);

        public abstract Message createSendFileMessage(File var1, String var2, String var3) throws FileNotFoundException, JMFileSizeExceedException;

        public abstract Message createSendFileMessage(File var1, String var2) throws FileNotFoundException, JMFileSizeExceedException;

        public abstract Message createLocationMessage(double var1, double var3, int var5, String var6);

        public abstract void retractMessage(Message var1, BasicCallback var2);

        public String toJson() {
            return e.c(this);
        }

        public static String collectionToJson(Collection<Conversation> var0) {
            return e.c(var0);
        }

        public String toJsonString() {
            return e.c(this);
        }

        public static Conversation fromJson(String var0) {
            c var5;
            try {
                JsonObject var1;
                JsonObject var2 = (var1 = (JsonObject)e.a(var0, JsonObject.class)).getAsJsonObject(z[3]);
                JsonObject var3 = var1.getAsJsonObject(z[2]);
                var1.remove(z[3]);
                var1.remove(z[2]);
                JsonPrimitive var6;
                if ((var6 = var1.getAsJsonPrimitive(z[0])) != null && var6.getAsString().equals(ConversationType.chatroom.toString())) {
                    var5 = (c)e.a(var1, cn.jmessage.biz.g.a.class);
                } else {
                    var5 = (c)e.a(var1, c.class);
                }

                if (var2 != null) {
                    switch(var5.type) {
                        case single:
                            var5.targetInfo = UserInfo.fromJson(var2.toString());
                            break;
                        case group:
                            var5.targetInfo = GroupInfo.fromJson(var2.toString());
                            break;
                        case chatroom:
                            var5.targetInfo = ChatRoomInfo.fromJson(var2.toString());
                    }
                }

                if (var3 != null) {
                    var5.latestMessage = Message.fromJson(var3.toString());
                }
            } catch (Exception var4) {
                var5 = null;
                cn.jmessage.a.c.c.j(TAG, z[1] + var4.getMessage());
                var4.printStackTrace();
            }

            return var5;
        }

        public static Collection<Conversation> fromJsonToCollection(String var0) {
            return e.a(var0, new cn.jmessage.a.d.e.a<Conversation>() {
                public final Conversation fromJson(String var1) {
                    return Conversation.fromJson(var1);
                }
            });
        }

        static {
            String[] var10000 = new String[4];
            String[] var10001 = var10000;
            byte var10002 = 0;
            String var10003 = "_ym+Sneb)_sx";
            byte var10004 = 3;

            while(true) {
                char[] var5;
                label39: {
                    char[] var3 = var10003.toCharArray();
                    int var10006 = var3.length;
                    int var0 = 0;
                    var5 = var3;
                    int var4 = var10006;
                    char[] var7;
                    int var10007;
                    if (var10006 <= 1) {
                        var7 = var3;
                        var10007 = var0;
                    } else {
                        var5 = var3;
                        var4 = var10006;
                        if (var10006 <= var0) {
                            break label39;
                        }

                        var7 = var3;
                        var10007 = var0;
                    }

                    while(true) {
                        char var10008 = var7[var10007];
                        byte var10009;
                        switch(var0 % 5) {
                            case 0:
                                var10009 = 28;
                                break;
                            case 1:
                                var10009 = 22;
                                break;
                            case 2:
                                var10009 = 3;
                                break;
                            case 3:
                                var10009 = 93;
                                break;
                            default:
                                var10009 = 54;
                        }

                        var7[var10007] = (char)(var10008 ^ var10009);
                        ++var0;
                        if (var4 == 0) {
                            var10007 = var4;
                            var7 = var5;
                        } else {
                            if (var4 <= var0) {
                                break;
                            }

                            var7 = var5;
                            var10007 = var0;
                        }
                    }
                }

                String var8 = (new String(var5)).intern();
                switch(var10004) {
                    case 0:
                        var10001[var10002] = var8;
                        var10001 = var10000;
                        var10002 = 2;
                        var10003 = "pww8Eh[f.E}qf";
                        var10004 = 1;
                        break;
                    case 1:
                        var10001[var10002] = var8;
                        var10001 = var10000;
                        var10002 = 3;
                        var10003 = "hwq:Sh_m;Y";
                        var10004 = 2;
                        break;
                    case 2:
                        var10001[var10002] = var8;
                        z = var10000;
                        return;
                    case 3:
                        TAG = var8;
                        var10003 = "hos8";
                        var10004 = -1;
                        break;
                    default:
                        var10001[var10002] = var8;
                        var10001 = var10000;
                        var10002 = 1;
                        var10003 = "ydq2D<y`>Cndf9\u0016ux#;Ds{I.Yr:#0E{6>}";
                        var10004 = 0;
                }
            }
        }
    }

}

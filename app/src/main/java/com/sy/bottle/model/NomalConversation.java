package com.sy.bottle.model;

import android.content.Context;
import android.text.TextUtils;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.ext.message.TIMConversationExt;

/**
 * 好友或群聊的会话
 */
public class NomalConversation extends Conversation {
    private static final String TAG = "NomalConversation";

    private TIMConversation conversation;

    //最后一条消息
    private Message lastMessage;

    public NomalConversation(TIMConversation conversation) {
        this.conversation = conversation;
        type = conversation.getType();
        identify = conversation.getPeer();
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String getAvatar() {
        switch (type) {
            case C2C:
                TIMUserProfile profile = FriendshipInfo.getInstance().getProfile(identify);

                if (profile != null) {

                    faceurl = profile.getFaceUrl();

                }
                break;
            case Group:
                faceurl = null;
                break;
        }
        return faceurl;
    }

    @Override
    public Integer getAvatarID() {
        return R.drawable.head_other;
    }

    /**
     * 跳转到聊天界面或会话详情
     *
     * @param context 跳转上下文
     */
    @Override
    public void navToDetail(Context context) {
        ChatActivity.navToChat(context, identify, type);
    }

    /**
     * 获取最后一条消息摘要
     */
    @Override
    public String getLastMessageSummary() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()) {
            TextMessage textMessage = new TextMessage(ext.getDraft());
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()) {
                return "[草稿]" + textMessage.getSummary();
            } else {
                return lastMessage.getSummary();
            }
        } else {
            if (lastMessage == null) return "";
            return lastMessage.getSummary();
        }
    }

    /**
     * 获取名称
     */
    @Override
    public String getName() {
        if (type == TIMConversationType.Group) {
            name = GroupInfo.getInstance().getGroupName(identify);
            if (name.equals("")) name = identify;
        } else {
            TIMUserProfile profile = FriendshipInfo.getInstance().getProfile(identify);

            if (profile != null) {

                if (!TextUtils.isEmpty(profile.getRemark())) {
                    name = profile.getRemark();
                } else {
                    name = profile.getNickName();
                }

                faceurl = profile.getFaceUrl();

            }

        }
        return name;
    }

    /**
     * 获取未读消息数量
     */
    @Override
    public long getUnreadNum() {
        if (conversation == null) return 0;
        TIMConversationExt ext = new TIMConversationExt(conversation);
        return ext.getUnreadMessageNum();
    }

    /**
     * 将所有消息标记为已读
     */
    @Override
    public void readAllMessage() {
        if (conversation != null) {
            TIMConversationExt ext = new TIMConversationExt(conversation);
            ext.setReadMessage(null, null);
        }
    }

    /**
     * 获取最后一条消息的时间
     */
    @Override
    public long getLastMessageTime() {
        TIMConversationExt ext = new TIMConversationExt(conversation);
        if (ext.hasDraft()) {
            if (lastMessage == null || lastMessage.getMessage().timestamp() < ext.getDraft().getTimestamp()) {
                return ext.getDraft().getTimestamp();
            } else {
                return lastMessage.getMessage().timestamp();
            }
        }
        if (lastMessage == null) return 0;
        return lastMessage.getMessage().timestamp();
    }

    /**
     * 获取会话类型
     */
    public TIMConversationType getType() {
        return conversation.getType();
    }
}

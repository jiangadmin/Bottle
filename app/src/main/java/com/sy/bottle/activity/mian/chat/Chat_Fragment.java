package com.sy.bottle.activity.mian.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Base_Fragment;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.adapters.ConversationAdapter;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.model.Conversation;
import com.sy.bottle.model.CustomMessage;
import com.sy.bottle.model.FriendshipConversation;
import com.sy.bottle.model.GroupManageConversation;
import com.sy.bottle.model.MessageFactory;
import com.sy.bottle.model.NomalConversation;
import com.sy.bottle.presenter.ConversationPresenter;
import com.sy.bottle.presenter.FriendshipManagerPresenter;
import com.sy.bottle.presenter.GroupManagerPresenter;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.viewfeatures.ConversationView;
import com.sy.bottle.viewfeatures.FriendshipMessageView;
import com.sy.bottle.viewfeatures.GroupManageMessageView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 消息
 */
public class Chat_Fragment extends Base_Fragment implements ConversationView, FriendshipMessageView, GroupManageMessageView, View.OnClickListener {
    private static final String TAG = "Chat_Fragment";

    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationAdapter adapter;
    private ListView listView;
    private ConversationPresenter presenter;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private GroupManagerPresenter groupManagerPresenter;
    private List<String> groupList;
    private FriendshipConversation friendshipConversation;
    private GroupManageConversation groupManageConversation;

    LinearLayout viewnull;

    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        setTitle(view, "消息");

        //增加一键清空
        setMenu(view, R.drawable.ic_clean);

        listView = view.findViewById(R.id.chat_list);
        viewnull = view.findViewById(R.id.view_null);

        adapter = new ConversationAdapter(getActivity(), R.layout.item_conversation, conversationList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                conversationList.get(position).navToDetail(getActivity());
                if (conversationList.get(position) instanceof GroupManageConversation) {
                    groupManagerPresenter.getGroupManageLastMessage();
                }
            }
        });
        friendshipManagerPresenter = new FriendshipManagerPresenter(this);
        groupManagerPresenter = new GroupManagerPresenter(this);
        presenter = new ConversationPresenter(this);
        presenter.getConversation();
        registerForContextMenu(listView);

        updateview();

    }

    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */
    @Override
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
        groupList = new ArrayList<>();
        for (TIMConversation item : conversationList) {
            switch (item.getType()) {
                case C2C:
                case Group:
                    this.conversationList.add(new NomalConversation(item));
                    groupList.add(item.getPeer());
                    break;
            }
        }
        friendshipManagerPresenter.getFriendshipLastMessage();
        groupManagerPresenter.getGroupManageLastMessage();

        setTitle(v, "消息");
    }

    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null) {
            updateview();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System) {
            groupManagerPresenter.getGroupManageLastMessage();
            return;
        }
        if (MessageFactory.getMessage(message) instanceof CustomMessage) {
            if (((CustomMessage) MessageFactory.getMessage(message)).getType() != CustomMessage.Type.GIFT) {
                return;
            }

        }

        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation c = iterator.next();
            if (conversation.equals(c)) {
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));

        conversationList.add(conversation);
        Collections.sort(conversationList);

        refresh();
    }

    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while (iterator.hasNext()) {
            Conversation conversation = iterator.next();
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(identify)) {
                iterator.remove();
                updateview();
                return;

            }
        }
    }

    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {
        for (Conversation conversation : conversationList) {
            if (conversation.getIdentify() != null && conversation.getIdentify().equals(info.getGroupInfo().getGroupId())) {
                updateview();
                return;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    /**
     * 刷新
     */
    @Override
    public void refresh() {
        Collections.sort(conversationList);
        updateview();
        if (getActivity() instanceof Main_Activity)
            ((Main_Activity) getActivity()).setMsgUnread(getTotalUnreadNum() == 0);
    }


    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
        if (friendshipConversation == null) {
            friendshipConversation = new FriendshipConversation(message);
            conversationList.add(friendshipConversation);
        } else {
            friendshipConversation.setLastMessage(message);
        }
        friendshipConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
        friendshipManagerPresenter.getFriendshipLastMessage();
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {
        if (groupManageConversation == null) {
            groupManageConversation = new GroupManageConversation(message);
            conversationList.add(groupManageConversation);
        } else {
            groupManageConversation.setLastMessage(message);
        }
        groupManageConversation.setUnreadCount(unreadCount);
        Collections.sort(conversationList);
        refresh();
    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Conversation conversation = conversationList.get(info.position);
        if (conversation instanceof NomalConversation) {
            menu.add(0, 1, Menu.NONE, "删除消息");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Conversation conversation = conversationList.get(info.position);
        LogUtil.e(TAG, item.getItemId());
        switch (item.getItemId()) {

            case 1:
                if (conversation != null) {
                    if (conversation instanceof NomalConversation) {
                        if (presenter.delConversation(((NomalConversation) conversation).getType(), conversation.getIdentify())) {
                            conversationList.remove(conversation);
                            updateview();
                        }
                    }
                }
                break;

            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private long getTotalUnreadNum() {
        long num = 0;
        for (Conversation conversation : conversationList) {
            num += conversation.getUnreadNum();
        }
        return num;
    }

    /**
     * 更新页面显示
     */
    public void updateview() {
        adapter.notifyDataSetChanged();

        if (adapter.getCount() == 0) {
            viewnull.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            viewnull.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                Base_Dialog base_dialog = new Base_Dialog(getActivity());
                base_dialog.setTitle("警告");
                base_dialog.setMessage("确认清楚所有聊天吗？");
                base_dialog.setOk("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Iterator<Conversation> iterator = conversationList.iterator();
                        while (iterator.hasNext()) {
                            Conversation conversation = iterator.next();
                            if (conversation.getIdentify() != null && conversation instanceof NomalConversation) {
                                if (presenter.delConversation(((NomalConversation) conversation).getType(), conversation.getIdentify())) {
                                    iterator.remove();

                                }
                            }
                        }
                        updateview();

                    }
                });
                base_dialog.setEsc("取消", null);
                break;
        }
    }
}

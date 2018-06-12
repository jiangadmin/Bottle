package com.sy.bottle.model;


import com.sy.bottle.event.FriendshipEvent;
import com.sy.bottle.event.RefreshEvent;
import com.sy.bottle.utils.LogUtil;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * TODO:好友列表缓存数据结构
 */
public class FriendshipInfo extends Observable implements Observer {

    private final String TAG = "FriendshipInfo";

    private List<String> groups;
    private Map<String, List<TIMUserProfile>> friends;

    private static FriendshipInfo instance;

    private FriendshipInfo() {
        groups = new ArrayList<>();
        friends = new HashMap<>();
        FriendshipEvent.getInstance().addObserver(this);
        RefreshEvent.getInstance().addObserver(this);
        refresh();
    }

    public synchronized static FriendshipInfo getInstance() {
        if (instance == null) {
            instance = new FriendshipInfo();
        }
        return instance;
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        TIMManager.getInstance().getEnv();
        if (observable instanceof FriendshipEvent) {
            if (data instanceof FriendshipEvent.NotifyCmd) {
                FriendshipEvent.NotifyCmd cmd = (FriendshipEvent.NotifyCmd) data;
                LogUtil.e(TAG, "get notify type:" + cmd.type);
                switch (cmd.type) {
                    case REFRESH:
                    case DEL:
                    case ADD:
                    case PROFILE_UPDATE:
                    case ADD_REQ:
                    case GROUP_UPDATE:
                        refresh();
                        break;
                    default:
                        break;
                }
            }
        } else {
            refresh();
        }
    }

    private void refresh() {

        TIMFriendshipManagerExt.getInstance().getFriendList(new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "Error:" + s);
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                groups.clear();
                friends.clear();

                for (TIMUserProfile group : timUserProfiles) {

                    //设置组信息
                    for (TIMUserProfile res : timUserProfiles) {

                        //获得分组名称
                        String groupname = res.getFriendGroups().size() == 0 ? "默认分组" : res.getFriendGroups().get(0);

                        if (groups.size() == 0) {
                            groups.add(groupname);
                        } else {
                            for (int i = 0; i < groups.size(); i++) {
                                if (!groups.get(i).equals(groupname)) {
                                    groups.add(groupname);
                                }
                            }
                        }
                    }

                    //设置分组成员
                    for (String groupname : groups) {

                        List<TIMUserProfile> friendProfiles = new ArrayList<>();
                        for (TIMUserProfile res : timUserProfiles) {
                            if (groupname.equals(res.getFriendGroups().size() == 0 ? "默认分组" : res.getFriendGroups().get(0))) {
                                friendProfiles.add(res);
                            }
                        }

                        friends.put(groupname, friendProfiles);
                    }
                }
                setChanged();
                notifyObservers();
            }
        });
    }

    /**
     * 获取分组列表
     */
    public List<String> getGroups() {
        return groups;
    }

    public String[] getGroupsArray() {
        return groups.toArray(new String[groups.size()]);
    }

    /**
     * 获取好友列表摘要
     */
    public Map<String, List<TIMUserProfile>> getFriends() {
        return friends;
    }

    /**
     * 判断是否是好友
     *
     * @param identify 需判断的identify
     */
    public boolean isFriend(String identify) {
        for (String key : friends.keySet()) {
            for (TIMUserProfile profile : friends.get(key)) {
                if (identify.equals(profile.getIdentifier())) return true;
            }
        }
        return false;
    }

    /**
     * 获取好友资料
     *
     * @param identify 好友id
     */
    public TIMUserProfile getProfile(String identify) {
        for (String key : friends.keySet()) {
            for (TIMUserProfile profile : friends.get(key)) {
                if (identify.equals(profile.getIdentifier())) return profile;
            }
        }
        return null;
    }

    /**
     * 清除数据
     */
    public void clear() {
        if (instance == null) return;
        groups.clear();
        friends.clear();
        instance = null;
    }


}

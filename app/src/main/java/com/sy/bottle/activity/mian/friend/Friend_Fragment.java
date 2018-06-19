package com.sy.bottle.activity.mian.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Base_Fragment;
import com.sy.bottle.adapters.Adapter_Friends;
import com.sy.bottle.adapters.ExpandGroupListAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.SearchFriend_Dialog;
import com.sy.bottle.model.FriendshipInfo;
import com.sy.bottle.utils.LogUtil;
import com.tencent.imsdk.TIMUserProfile;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 好友
 */
public class Friend_Fragment extends Base_Fragment implements View.OnClickListener, ExpandableListView.OnChildClickListener, Observer, AdapterView.OnItemClickListener {
    private static final String TAG = "Mine_Fragment";

    /**
     * 好友列表适配器
     */
    private ExpandGroupListAdapter mGroupListAdapter;
    /**
     * 好友列表
     */
    private ExpandableListView mGroupListView;
    ListView friends_List;
    Adapter_Friends adapter_friends;

    LinearLayout view_null;

    /**
     * 分组
     */
    List<String> groups = new LinkedList<>();

    /**
     * 分组成员
     */
    Map<String, List<TIMUserProfile>> friends = new LinkedHashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(view, "好友列表");

        setMenu(view, R.drawable.ic_add, "添加");

        mGroupListView = view.findViewById(R.id.friend_groupList);
        friends_List = view.findViewById(R.id.friends);
        view_null = view.findViewById(R.id.view_null);

        mGroupListView.setOnChildClickListener(this);
        friends_List.setOnItemClickListener(this);
        adapter_friends = new Adapter_Friends(getActivity());
        friends_List.setAdapter(adapter_friends);

    }

    @Override
    public void onResume() {
        friends = FriendshipInfo.getInstance().getFriends();
        groups = FriendshipInfo.getInstance().getGroups();

        if (MyApp.friendsbeans.size() == 0) {
            view_null.setVisibility(View.VISIBLE);
            mGroupListView.setVisibility(View.GONE);
        } else {
            view_null.setVisibility(View.GONE);
            mGroupListView.setVisibility(View.VISIBLE);
            adapter_friends.setListData(MyApp.friendsbeans);
        }

        adapter_friends.notifyDataSetChanged();

        mGroupListAdapter = new ExpandGroupListAdapter(getActivity(), groups, friends);
        mGroupListView.setAdapter(mGroupListAdapter);
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:

                //查找好友
                new SearchFriend_Dialog(getActivity());
//                SearchFriend_Activity.start(getActivity());
//                new Move_Dialog(getActivity());
                break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Profile_Activity.start(getActivity(), friends.get(groups.get(groupPosition)).get(childPosition).getIdentifier());
        return false;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof FriendshipInfo) {
            mGroupListAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FriendInfo_Activity.start(getActivity(), MyApp.friendsbeans.get(i).getFriend_id());
    }
}

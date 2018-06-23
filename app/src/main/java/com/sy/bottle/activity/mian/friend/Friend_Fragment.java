package com.sy.bottle.activity.mian.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Base_Fragment;
import com.sy.bottle.adapters.Adapter_Friends;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.SearchFriend_Dialog;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 好友
 */
public class Friend_Fragment extends Base_Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "Mine_Fragment";

    ListView friends_List;
    Adapter_Friends adapter_friends;

    LinearLayout view_null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(view, "好友列表");

        setMenu(view, "添加");

        friends_List = view.findViewById(R.id.friends);
        view_null = view.findViewById(R.id.view_null);

        friends_List.setOnItemClickListener(this);
        adapter_friends = new Adapter_Friends(getActivity());
        friends_List.setAdapter(adapter_friends);

    }

    @Override
    public void onResume() {

        if (MyApp.friendsbeans.size() == 0) {
            view_null.setVisibility(View.VISIBLE);
            friends_List.setVisibility(View.GONE);
        } else {
            view_null.setVisibility(View.GONE);
            friends_List.setVisibility(View.VISIBLE);
            adapter_friends.setListData(MyApp.friendsbeans);
        }
        adapter_friends.notifyDataSetChanged();

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:

                //查找好友
                new SearchFriend_Dialog(getActivity());

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FriendInfo_Activity.start(getActivity(), MyApp.friendsbeans.get(i).getFriend_id());
    }
}

package com.sy.bottle.activity.mian;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.bottle.Bottle_Fragment;
import com.sy.bottle.activity.mian.chat.Chat_Fragment;
import com.sy.bottle.activity.mian.friend.Friend_Fragment;
import com.sy.bottle.activity.mian.mine.Mine_Fragment;

public class Main_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    Button chat, bottle, friend, mine;

    Chat_Fragment chat_fragment;
    Bottle_Fragment bottle_fragment;
    Friend_Fragment friend_fragment;
    Mine_Fragment mine_fragment;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Main_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();

        initeven();

        ShowFragmet(1);
    }

    private void initview() {

        chat = findViewById(R.id.main_chat);
        bottle = findViewById(R.id.main_bottle);
        friend = findViewById(R.id.main_friend);
        mine = findViewById(R.id.main_mine);
    }

    private void initeven() {

        chat.setOnClickListener(this);
        bottle.setOnClickListener(this);
        friend.setOnClickListener(this);
        mine.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.main_chat:
                ShowFragmet(1);
                break;
            case R.id.main_bottle:
                ShowFragmet(2);
                break;
            case R.id.main_friend:
                ShowFragmet(3);
                break;
            case R.id.main_mine:
                ShowFragmet(4);
                break;
        }

    }

    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(boolean noUnread) {
//        msgUnread.setVisibility(noUnread ? View.GONE : View.VISIBLE);
    }


    /**
     * 控制二级显示
     *
     * @param vid
     */
    public void ShowFragmet(int vid) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        if (chat_fragment == null) {
            chat_fragment = new Chat_Fragment();
            transaction.add(R.id.main_layout, chat_fragment);
        }
        if (bottle_fragment == null) {
            bottle_fragment = new Bottle_Fragment();
            transaction.add(R.id.main_layout, bottle_fragment);
        }

        if (friend_fragment == null) {
            friend_fragment = new Friend_Fragment();
            transaction.add(R.id.main_layout, friend_fragment);
        }

        if (mine_fragment == null) {
            mine_fragment = new Mine_Fragment();
            transaction.add(R.id.main_layout, mine_fragment);
        }

        transaction.hide(chat_fragment);
        transaction.hide(bottle_fragment);
        transaction.hide(friend_fragment);
        transaction.hide(mine_fragment);

        chat.setEnabled(true);
        bottle.setEnabled(true);
        friend.setEnabled(true);
        mine.setEnabled(true);

        switch (vid) {
            case 1:
                chat.setEnabled(false);
                transaction.show(chat_fragment);

                break;
            case 2:
                bottle.setEnabled(false);
                transaction.show(bottle_fragment);
                break;
            case 3:
                friend.setEnabled(false);
                transaction.show(friend_fragment);
                friend_fragment.onResume();
                break;
            case 4:
                mine.setEnabled(false);
                transaction.show(mine_fragment);
                mine_fragment.onResume();
                break;
        }

        transaction.commit();
    }

    @Override
    protected void onResume() {
        if (friend_fragment != null) {
            friend_fragment.onResume();
        }
        super.onResume();

    }
}

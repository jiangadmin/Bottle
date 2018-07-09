package com.sy.bottle.activity.mian.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

/**
 * @author: jiangyao
 * @date: 2018/7/5
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 最近在线的人
 */
public class Friend_Online_Activity extends Base_Activity {
    private static final String TAG = "Friend_Online_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Friend_Online_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        setTitle("有缘人");
        setBack(true);
    }
}

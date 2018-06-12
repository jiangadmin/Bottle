package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

/**
 * @author: jiangyao
 * @date: 2018/6/6
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 关于
 */
public class About_Activity extends Base_Activity {
    private static final String TAG = "About_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, About_Activity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        setTitle("关于我们");
        setBack(true);

    }
}

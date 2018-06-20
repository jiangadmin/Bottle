package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sy.bottle.activity.Base_Activity;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 帮助与客服
 */
public class Help_Activity extends Base_Activity {
    private static final String TAG = "Help_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,Help_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}

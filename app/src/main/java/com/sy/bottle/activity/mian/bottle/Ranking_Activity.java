package com.sy.bottle.activity.mian.bottle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

/**
 * @author: jiangyao
 * @date: 2018/7/16
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 排行榜
 */
public class Ranking_Activity extends Base_Activity {
    private static final String TAG = "Ranking_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Ranking_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("排行榜");
        setBack(true);
    }



}

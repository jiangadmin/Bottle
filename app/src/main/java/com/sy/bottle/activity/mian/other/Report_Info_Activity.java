package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

/**
 * @author: jiangyao
 * @date: 2018/6/21
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 投诉须知
 */
public class Report_Info_Activity extends Base_Activity {
    private static final String TAG = "Report_Info_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Report_Info_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_info);

        setTitle("投诉须知");
        setBack(true);

    }
}

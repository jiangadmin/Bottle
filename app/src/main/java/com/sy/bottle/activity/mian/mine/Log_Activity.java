package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.adapters.Gift_Log_Adapter;
import com.sy.bottle.entity.Gift_Log_Entity;
import com.sy.bottle.servlet.Gift_Log_Servlet;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 赠送记录
 */
public class Log_Activity extends Base_Activity {
    private static final String TAG = "Gift_Activity";

    ListView listView;

    LinearLayout view_null;

    Gift_Log_Adapter gift_log_adapter;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Log_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        setTitle("赠送记录");

        setBack(true);

        listView = findViewById(R.id.gift_log_list);
        view_null = findViewById(R.id.view_null);
        gift_log_adapter = new Gift_Log_Adapter(this);
        listView.setAdapter(gift_log_adapter);


        new Gift_Log_Servlet(this).execute();
    }

    /**
     * 数据返回
     *
     * @param beans
     */
    public void CallBack(List<Gift_Log_Entity.DataBean> beans) {

        if (beans != null && beans.size() > 0) {
            view_null.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            gift_log_adapter.setDataBeans(beans);
            gift_log_adapter.notifyDataSetChanged();
        } else {
            view_null.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }
}

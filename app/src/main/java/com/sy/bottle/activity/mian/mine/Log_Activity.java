package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.other.NewWebActivity;
import com.sy.bottle.activity.mian.other.Put_forward_Activity;
import com.sy.bottle.adapters.Gift_Get_Log_Adapter;
import com.sy.bottle.adapters.Gift_Set_Log_Adapter;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Gift_Get_Log_Entity;
import com.sy.bottle.entity.Gift_Set_Log_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Gift_Get_Log_Servlet;
import com.sy.bottle.servlet.Gift_Set_Log_Servlet;
import com.sy.bottle.utils.SaveUtils;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 赠送记录
 */
public class Log_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Gift_Activity";

    ListView listView;

    LinearLayout view_null;

    TextView gifts_get, gifts_set, put_forward, ml, gift_agreement;

    Gift_Set_Log_Adapter gift_set_log_adapter;
    Gift_Get_Log_Adapter gift_get_log_adapter;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Log_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        setTitle("礼物记录");

        setBack(true);

        listView = findViewById(R.id.gift_log_list);
        view_null = findViewById(R.id.view_null);
        gifts_set = findViewById(R.id.log_gifts_set);
        gifts_get = findViewById(R.id.log_gifts_get);
        put_forward = findViewById(R.id.mybalance_put_forward);
        gift_agreement = findViewById(R.id.gift_agreement);
        ml = findViewById(R.id.ml);
        gift_get_log_adapter = new Gift_Get_Log_Adapter(this);
        gift_set_log_adapter = new Gift_Set_Log_Adapter(this);

        gifts_get.setOnClickListener(this);
        gifts_set.setOnClickListener(this);
        put_forward.setOnClickListener(this);
        gift_agreement.setOnClickListener(this);
        ml.setText("能量：" + SaveUtils.getInt(Save_Key.S_能量));

        new Gift_Get_Log_Servlet(this).execute();
        gifts_get.setEnabled(false);
        gifts_set.setEnabled(true);
        gifts_get.setTextColor(getResources().getColor(R.color.style_color));

    }

    /**
     * 送礼物记录
     *
     * @param beans
     */
    public void CallBack_Gift_Set(List<Gift_Set_Log_Entity.DataBean> beans) {

        if (beans != null && beans.size() > 0) {

            view_null.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            gift_set_log_adapter.setDataBeans(beans);
            listView.setAdapter(gift_set_log_adapter);

        } else {

            view_null.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    /**
     * 送礼物记录
     *
     * @param beans
     */
    public void CallBack_Gift_Get(List<Gift_Get_Log_Entity.DataBean> beans) {

        if (beans != null && beans.size() > 0) {

            view_null.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

            gift_get_log_adapter.setDataBeans(beans);
            listView.setAdapter(gift_get_log_adapter);
        } else {
            view_null.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mybalance_put_forward:
                Put_forward_Activity.start(this);
                break;
            case R.id.gift_agreement:
                NewWebActivity.start(this, Const.API + "Gift_Agreement.html");
                break;
            case R.id.log_gifts_get:
                Loading.show(this, "请稍后");
                gifts_get.setTextColor(getResources().getColor(R.color.style_color));
                gifts_set.setTextColor(getResources().getColor(R.color.gray_6));
                gifts_get.setEnabled(false);
                gifts_set.setEnabled(true);

                new Gift_Get_Log_Servlet(this).execute();
                break;
            case R.id.log_gifts_set:
                Loading.show(this, "请稍后");
                gifts_get.setTextColor(getResources().getColor(R.color.gray_6));
                gifts_set.setTextColor(getResources().getColor(R.color.style_color));
                gifts_get.setEnabled(true);
                gifts_set.setEnabled(false);

                new Gift_Set_Log_Servlet(this).execute();
                break;

        }
    }
}

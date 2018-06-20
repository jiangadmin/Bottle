package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.other.Put_forward_Activity;
import com.sy.bottle.activity.mian.other.Recharge_Activity;
import com.sy.bottle.adapters.Adapter_Goods;
import com.sy.bottle.dialog.ReCharge_Dialog;
import com.sy.bottle.entity.Goods_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Goods_Servlet;
import com.sy.bottle.utils.SaveUtils;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 我的能量
 */
public class MyBalance_Activity extends Base_Activity implements View.OnClickListener, Adapter_Goods.Listenner {
    private static final String TAG = "MyBalance_Activity";

    SlidingPaneLayout sl;

    TextView num, put_forward;

    ListView goods;

    RelativeLayout rmb_other;

    Adapter_Goods adapter_goods;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyBalance_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybalance);

        setTitle("我的能量");

        setBack(true);

        initview();

        new Goods_Servlet(this).execute();

    }

    public void CallBack_Goods(List<Goods_Entity.DataBean> dataBeans) {
        adapter_goods = new Adapter_Goods(this, this);
        adapter_goods.setListData(dataBeans);
        goods.setAdapter(adapter_goods);

    }

    private void initview() {

        sl = findViewById(R.id.mybalance_sl);
        num = findViewById(R.id.mybalance_num);
        put_forward = findViewById(R.id.mybalance_put_forward);
        goods = findViewById(R.id.goods);


        num.setText(String.valueOf(SaveUtils.getInt(Save_Key.S_能量)));
        rmb_other = findViewById(R.id.mybalance_rmb_other);

        sl.setEnabled(false);

        rmb_other.setOnClickListener(this);
        put_forward.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mybalance_rmb_other:
                new ReCharge_Dialog(this);
                break;

            case R.id.mybalance_put_forward:
                Put_forward_Activity.start(this);
                break;
        }
    }

    @Override
    public void Good(Goods_Entity.DataBean bean) {
        Recharge_Activity.start(this, bean);
    }
}

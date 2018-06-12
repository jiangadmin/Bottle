package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.entity.Gift_Entity;
import com.sy.bottle.servlet.GiftList_Servlet;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 道具商城
 */
public class Gift_Activity extends Base_Activity {
    private static final String TAG = "Gift_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context,Gift_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        setTitle("道具商城");

        setBack(true);

        new GiftList_Servlet(this).execute();
    }


    /**
     * 数据返回
     * @param beans
     */
    public void CallBack(List<Gift_Entity.DataBean> beans){

    }
}

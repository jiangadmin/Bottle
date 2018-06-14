package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.servlet.Black_Get_Servlet;

/**
 * @author: jiangyao
 * @date: 2018/6/7
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 黑名单
 */
public class Black_Activity extends Base_Activity {
    private static final String TAG = "Black_Activity";

    LinearLayout view_null;
    ListView black;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Black_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_black);

        setTitle("黑名单");

        setBack(true);

        black = findViewById(R.id.black_list);
        view_null = findViewById(R.id.view_null);

        //获取黑名单列表
        new Black_Get_Servlet(this).execute();
    }

    /**
     * 查询返回
     */
    public void CallBack(){

    }
}

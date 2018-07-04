package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.adapters.Adapter_Friends;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.servlet.Black_Get_Servlet;
import com.sy.bottle.servlet.Black_Out_Servlet;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/7
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 黑名单
 */
public class Black_Activity extends Base_Activity  {
    private static final String TAG = "Black_Activity";

    LinearLayout view_null;
    ListView black;

    Adapter_Friends adapter_friends;

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

        initeven();

        adapter_friends = new Adapter_Friends(this);

    }

    public void initeven(){
        //获取黑名单列表
        new Black_Get_Servlet(this).execute();
    }

    /**
     * 查询返回
     */
    public void CallBack(final List<Friends_Entity.DataBean> dataBeans) {
        if (dataBeans == null || dataBeans.size() == 0) {
            view_null.setVisibility(View.VISIBLE);
            black.setVisibility(View.GONE);
        } else {
            view_null.setVisibility(View.GONE);
            black.setVisibility(View.VISIBLE);

            adapter_friends.setListData(dataBeans);
            black.setAdapter(adapter_friends);

        }

        black.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Base_Dialog base_dialog = new Base_Dialog(Black_Activity.this);
                base_dialog.setMessage("将"+dataBeans.get(i).getNickname()+"移出黑名单?");
                base_dialog.setOk("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    new Black_Out_Servlet(Black_Activity.this).execute(dataBeans.get(i).getFriend_id());
                    }
                });
                base_dialog.setEsc("取消",null);
            }
        });
    }


}

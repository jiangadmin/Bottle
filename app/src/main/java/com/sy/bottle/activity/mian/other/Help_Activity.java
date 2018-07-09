package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.adapters.Text_Adapter;
import com.sy.bottle.entity.Help_Entity;
import com.sy.bottle.servlet.Help_Servlet;
import com.sy.bottle.servlet.Notice_Servlet;
import com.tencent.imsdk.TIMConversationType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 帮助与客服
 */
public class Help_Activity extends Base_Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "Help_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Help_Activity.class);
        context.startActivity(intent);
    }

    WebView webView;
    ListView listView;
    FloatingActionButton waiter;

    Text_Adapter button_adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        setTitle("咨询与帮助");

        setBack(true);

        initview();

        button_adapter = new Text_Adapter(this);
        listView.setAdapter(button_adapter);
        listView.setOnItemClickListener(this);

        //获取公告头
        new Notice_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "help_head");
        //获取列表
        new Help_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void initview() {
        webView = findViewById(R.id.help_web);
        listView = findViewById(R.id.help_list);
        waiter = findViewById(R.id.waiter);

        waiter.setOnClickListener(this);
    }

    /**
     * 网页代码返回
     *
     * @param s
     */
    public void CallBack_WebTxt(String s) {
        //能够的调用JavaScript代码
        webView.getSettings().setJavaScriptEnabled(true);
        //加载HTML字符串进行显示
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.loadData(s, "text/html; charset=UTF-8", null);//这种写法可以正确解码


    }

    List<String> title;
    List<Help_Entity.DataBean> dataBeans;

    public void CallBack_HelpList(final List<Help_Entity.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        title = new ArrayList();
        for (Help_Entity.DataBean bean : dataBeans) {
            title.add(bean.getTitle());
        }
        button_adapter.setListData(title);
        button_adapter.notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Web_Activity.start(Help_Activity.this, dataBeans.get(i).getTitle(), dataBeans.get(i).getValue());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.waiter:
                ChatActivity.navToChat(this, "100001", TIMConversationType.C2C);
                break;
        }
    }
}

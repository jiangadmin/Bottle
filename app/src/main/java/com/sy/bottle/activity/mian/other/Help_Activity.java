package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.entity.Help_Entity;
import com.sy.bottle.servlet.Help_Servlet;
import com.sy.bottle.servlet.Notice_Servlet;

import java.util.ArrayList;
import java.util.List;

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
        intent.setClass(context, Help_Activity.class);
        context.startActivity(intent);
    }

    WebView webView;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);

        setTitle("会员自助服务");

        setBack(true);

        initview();

        new Notice_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "notice_test");
        new Help_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void initview() {
        webView = findViewById(R.id.help_web);
        listView = findViewById(R.id.help_list);
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
        webView.loadData(s, "text/html", "utf-8");
    }

    List<String> title;

    public void CallBack_HelpList(final List<Help_Entity.DataBean> dataBeans) {
        title = new ArrayList();
        for (Help_Entity.DataBean bean : dataBeans) {
            title.add(bean.getTitle());
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, title));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Web_Activity.start(Help_Activity.this,title.get(position),dataBeans.get(position).getValue());
            }
        });
    }
}

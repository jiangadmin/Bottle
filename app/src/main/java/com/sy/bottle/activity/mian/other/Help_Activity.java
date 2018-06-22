package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.servlet.Notice_Servlet;

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

        new Notice_Servlet(this).execute("notice_test");

    }

    private void initview() {
        webView = findViewById(R.id.help_web);
        listView = findViewById(R.id.help_list);
    }

    /**
     * 网页代码返回
     * @param s
     */
    public void CallBack_WebTxt(String s){
        //能够的调用JavaScript代码
        webView.getSettings().setJavaScriptEnabled(true);
        //加载HTML字符串进行显示
        webView.loadData(s, "text/html", "utf-8");
    }
}

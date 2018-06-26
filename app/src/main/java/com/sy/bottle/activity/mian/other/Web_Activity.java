package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

/**
 * -
 * Created by Administrator on 2018-06-20.
 */
public class Web_Activity extends Base_Activity {
    private static final String TAG = "Web_Activity";
    private static final String URL = "URL";
    private static final String TITLE = "TITLE";

    WebView mWebView;
    String title,loadurl;

    public static void start(Context context,String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, Web_Activity.class);
        intent.putExtra(URL, url);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        setBack(true);

        loadurl = getIntent().getStringExtra(URL);
        title = getIntent().getStringExtra(TITLE);

        setTitle(title);
        initView();
    }

    private void initView() {
        mWebView = findViewById(R.id.web);

        //能够的调用JavaScript代码
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        //加载HTML字符串进行显示
        mWebView.loadData(loadurl, "text/html; charset=UTF-8", null);//这种写法可以正确解码



    }

}

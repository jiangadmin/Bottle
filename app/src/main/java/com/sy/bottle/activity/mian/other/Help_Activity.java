package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.widget.ListView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

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

    }

    private void initview() {
        webView = findViewById(R.id.help_web);
        listView = findViewById(R.id.help_list);
    }
}

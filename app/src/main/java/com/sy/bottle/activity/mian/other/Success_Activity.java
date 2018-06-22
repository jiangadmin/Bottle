package com.sy.bottle.activity.mian.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * @author: jiangadmin
 * @date: 2017/11/7.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 操作成功页
 */

public class Success_Activity extends Base_Activity {
    private static final String TAG = "Success_Activity";

    TextView message;
    LinearLayout item;
    RelativeLayout item_1, item_2, item_3, item_4;
    TextView item_1_key, item_2_key, item_3_key, item_4_key, item_1_value, item_2_value, item_3_value, item_4_value;

    Button ok;

    static LinkedHashMap<String, String> itemmessage;

    public static void start(LinkedHashMap map) {
        Intent intent = new Intent();
        intent.setClass(MyApp.currentActivity(), Success_Activity.class);
        itemmessage = map;
        MyApp.currentActivity().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        setTitle("结果详情");

        initview();

        initeven();

    }

    private void initview() {

        message = findViewById(R.id.success_message);
        item = findViewById(R.id.success_item);

        item_1 = findViewById(R.id.success_item_1);
        item_1_key = findViewById(R.id.success_item_1_key);
        item_1_value = findViewById(R.id.success_item_1_value);

        item_2 = findViewById(R.id.success_item_2);
        item_2_key = findViewById(R.id.success_item_2_key);
        item_2_value = findViewById(R.id.success_item_2_value);

        item_3 = findViewById(R.id.success_item_3);
        item_3_key = findViewById(R.id.success_item_3_key);
        item_3_value = findViewById(R.id.success_item_3_value);

        item_4 = findViewById(R.id.success_item_4);
        item_4_key = findViewById(R.id.success_item_4_key);
        item_4_value = findViewById(R.id.success_item_4_value);

        ok = findViewById(R.id.success_ok);
    }

    int mapi = 0;

    private void initeven() {
        Set set = itemmessage.keySet();
        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            switch (mapi) {
                case 0:
                    message.setText(itemmessage.get(key));
                    break;
                case 1:
                    item.setVisibility(View.VISIBLE);
                    item_1.setVisibility(View.VISIBLE);
                    item_1_key.setText(key);
                    item_1_value.setText(itemmessage.get(key));
                    break;
                case 2:
                    item_2.setVisibility(View.VISIBLE);
                    item_2_key.setText(key);
                    item_2_value.setText(itemmessage.get(key));
                    break;
                case 3:
                    item_3.setVisibility(View.VISIBLE);
                    item_3_key.setText(key);
                    item_3_value.setText(itemmessage.get(key));
                    break;
                case 4:
                    item_4.setVisibility(View.VISIBLE);
                    item_4_key.setText(key);
                    item_4_value.setText(itemmessage.get(key));
                    break;
            }
            mapi++;
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.finishActivity();
            }
        });
    }
}

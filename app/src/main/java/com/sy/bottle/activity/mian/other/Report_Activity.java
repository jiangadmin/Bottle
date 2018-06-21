package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;

/**
 * @author: jiangyao
 * @date: 2018/6/21
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 投诉
 */
public class Report_Activity extends Base_Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "Report_Activity";

    public static void start(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, Report_Activity.class);
        context.startActivity(intent);
    }

    GridView list;
    EditText message;
    ImageButton photos;
    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        setTitle("投诉");
        setBack(true);

        initview();
    }

    private void initview() {
        list = findViewById(R.id.report_list);
        message = findViewById(R.id.report_content);
        photos = findViewById(R.id.report_photo);
        submit = findViewById(R.id.report_submit);

        list.setOnItemClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {

    }
}

package com.sy.bottle.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.utils.ToolUtils;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 充值
 */
public class Recharge_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Recharge_Activity";

    TextView balance, rmb, agreement;

    RelativeLayout alipay, wechat;

    ImageView alipay_type, wechat_type;

    CheckBox checkBox;

    Button submit;

    static float money;

    public static void start(Context context, float i) {
        Intent intent = new Intent();
        intent.setClass(context, Recharge_Activity.class);
        money = i;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        setTitle("确认付款");

        setBack(true);

        initview();
    }

    private void initview() {
        balance = findViewById(R.id.recharge_balance);
        rmb = findViewById(R.id.recharge_rmb);
        agreement = findViewById(R.id.recharge_agreement);

        alipay = findViewById(R.id.recharge_alipay);
        wechat = findViewById(R.id.recharge_wechat);
        alipay_type = findViewById(R.id.recharge_alipay_type);
        wechat_type = findViewById(R.id.recharge_wechat_type);

        checkBox = findViewById(R.id.recharge_checkBox);

        submit = findViewById(R.id.recharge_submit);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                submit.setEnabled(b);
            }
        });

        alipay.setOnClickListener(this);
        wechat.setOnClickListener(this);
        submit.setOnClickListener(this);

        balance.setText((int) (money * 10) + "=");
        rmb.setText(ToolUtils.float2(money) + "元");

        submit.setText("确认充值" + ToolUtils.float2(money) + "元");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_alipay:
                alipay_type.setImageResource(R.drawable.ic_select);
                wechat_type.setImageResource(R.drawable.ic_unselect);
                break;
            case R.id.recharge_wechat:
                alipay_type.setImageResource(R.drawable.ic_unselect);
                wechat_type.setImageResource(R.drawable.ic_select);
                break;
            case R.id.recharge_submit:
                break;
            case R.id.recharge_agreement:
                break;

        }
    }
}

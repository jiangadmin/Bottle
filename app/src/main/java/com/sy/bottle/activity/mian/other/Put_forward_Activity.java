package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.servlet.Put_forward_Servlet;
import com.sy.bottle.servlet.Ratio_Serlvet;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 提取能量
 */
public class Put_forward_Activity extends Base_Activity implements TextWatcher, View.OnClickListener {
    private static final String TAG = "Put_forward_Activity";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Put_forward_Activity.class);
        context.startActivity(intent);
    }

    EditText alipay, alipay_name, balance;

    TextView money;

    Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_turn_out);

        setTitle("提取能量");
        setMenu("记录");
        setBack(true);

        alipay = findViewById(R.id.out_alipay);
        alipay_name = findViewById(R.id.out_alipay_name);
        balance = findViewById(R.id.out_alipay_balance);
        money = findViewById(R.id.out_alipay_money);
        submit = findViewById(R.id.out_submit);

        balance.addTextChangedListener(this);
        submit.setOnClickListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.toString() != null) {
            new Ratio_Serlvet(this).execute(editable.toString());
        } else {
            money.setText("");
        }
    }

    public void CallBack_Money(String s) {
        money.setText(s);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                Put_forward_Log_Activity.start(this);
                break;
            case R.id.out_submit:
                if (TextUtils.isEmpty(balance.getText().toString())) {
                    TabToast.makeText("提取不能为空");
                    return;
                }
                if (TextUtils.isEmpty(alipay.getText().toString()) || TextUtils.isEmpty(alipay_name.getText().toString())) {
                    TabToast.makeText("请完善支付宝信息");
                    return;
                }
                Loading.show(this, "申请中");
                Put_forward_Servlet.Info info = new Put_forward_Servlet.Info();
                info.setAccount(alipay.getText().toString());
                info.setMoney(money.getText().toString());
                info.setPrice(balance.getText().toString());
                info.setReal_name(alipay_name.getText().toString());

                new Put_forward_Servlet(this).execute(info);

                break;
        }
    }
}

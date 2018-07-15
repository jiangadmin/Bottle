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
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Ratio_List_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Put_forward_Servlet;
import com.sy.bottle.servlet.Ratio_List_Serlvet;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.ToolUtils;
import com.sy.bottle.view.EditTextWithClearButton;
import com.sy.bottle.view.TabToast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    EditTextWithClearButton alipay, alipay_name, balance;

    TextView money;

    Button submit;

    List<Ratio_List_Entity.DataBean> bean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_turn_out);

        setTitle("提取能量");
        setMenu("记录");
        setBack(true);

        Loading.show(this, "请稍后");
        new Ratio_List_Serlvet(this).execute();

        alipay = findViewById(R.id.out_alipay);
        alipay_name = findViewById(R.id.out_alipay_name);
        balance = findViewById(R.id.out_alipay_balance);
        money = findViewById(R.id.out_alipay_money);
        submit = findViewById(R.id.out_submit);

        balance.addTextChangedListener(this);
        submit.setOnClickListener(this);

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_ALINAME))) {
            alipay_name.setText(SaveUtils.getString(Save_Key.S_ALINAME));
        }

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_ALIPHONE))) {
            alipay.setText(SaveUtils.getString(Save_Key.S_ALIPHONE));
        }

        if (SaveUtils.getInt(Save_Key.S_能量) >= 1500) {
            balance.setHint("可输入最大转出能量：" + SaveUtils.getInt(Save_Key.S_能量));
        } else {
            balance.setHint("能量不足，无法提取");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        if (!TextUtils.isEmpty(editable.toString())) {
            for (Ratio_List_Entity.DataBean dataBean : bean) {
                if (Integer.valueOf(editable.toString()) >= dataBean.getNum()) {
                    if (dataBean.getRatio() != 0) {
                        money.setText(String.valueOf(ToolUtils.div(editable.toString(), String.valueOf(dataBean.getRatio()), 1)));

                    } else {
                        money.setText("0");
                    }
                    return;
                }
            }
        } else {
            money.setText("0");
        }
    }

    public void CallBack_Money(List<Ratio_List_Entity.DataBean> dataBean) {
        if (dataBean != null) {
            Collections.sort(dataBean, new Comparator<Ratio_List_Entity.DataBean>() {

                @Override
                public int compare(Ratio_List_Entity.DataBean dataBean, Ratio_List_Entity.DataBean t1) {
                    Ratio_List_Entity.DataBean stu1 = dataBean;
                    Ratio_List_Entity.DataBean stu2 = t1;
                    if (stu1.getNum() < stu2.getNum()) {
                        return 1;
                    } else if (stu1.getNum() == stu2.getNum()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });

        }
        bean = dataBean;

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

                if (Integer.valueOf(balance.getText().toString()) > SaveUtils.getInt(Save_Key.S_能量)) {
                    TabToast.makeText("提取能量不能大于您拥有的能量");
                    return;
                }

                if (Integer.valueOf(balance.getText().toString()) < 1500) {
                    TabToast.makeText("最小提取值为:1500");
                    return;
                }

                Loading.show(this, "申请中");

                SaveUtils.setString(Save_Key.S_ALINAME, alipay_name.getText().toString());
                SaveUtils.setString(Save_Key.S_ALIPHONE, alipay.getText().toString());

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

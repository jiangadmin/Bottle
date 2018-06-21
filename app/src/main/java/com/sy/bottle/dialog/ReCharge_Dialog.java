package com.sy.bottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/16
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 充值弹框
 */
public class ReCharge_Dialog extends MyDialog implements TextWatcher, View.OnClickListener {
    private static final String TAG = "ReCharge_Dialog";

    EditText money;
    TextView balance, message;
    Button sumbit;

    public ReCharge_Dialog(@NonNull Context context) {
        super(context, R.style.myDialogTheme);
        setCanceledOnTouchOutside(true);
        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_recharge);

        money = findViewById(R.id.recharge_money);
        balance = findViewById(R.id.recharge_balance);
        message = findViewById(R.id.recharge_meaage);
        sumbit = findViewById(R.id.recharge_submit);

        money.addTextChangedListener(this);

        sumbit.setOnClickListener(this);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
//        if (!TextUtils.isEmpty(editable.toString())) {
//            if (Integer.getInteger(editable.toString()) >= 10) {
//                balance.setText(CalculateUtils.mul(editable.toString(), "100"));
//            } else {
//                balance.setText("");
//            }
//        } else {
//            balance.setText("");
//        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_submit:
                if (TextUtils.isEmpty(balance.getText().toString())) {
                    TabToast.makeText("金额不合法");
                    return;
                }
//                Recharge_Activity.start(MyApp.currentActivity(), Float.parseFloat(money.getText().toString()));
                dismiss();
                break;
        }
    }
}

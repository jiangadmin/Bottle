package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.Recharge_Activity;
import com.sy.bottle.dialog.ReCharge_Dialog;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 我的星星
 */
public class MyBalance_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "MyBalance_Activity";

    SlidingPaneLayout sl;

    TextView num, put_forward;

    Button rmb_6, rmb_25, rmb_88, rmb_318, rmb_518, rmb_998;

    RelativeLayout rmb_other;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyBalance_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_mybalance);

        setTitle("我的星星");

        setBack(true);

        setMenu("明细");

        initview();


    }

    private void initview() {

        sl = findViewById(R.id.mybalance_sl);
        num = findViewById(R.id.mybalance_num);
        put_forward = findViewById(R.id.mybalance_put_forward);
        rmb_6 = findViewById(R.id.mybalance_rmb6);
        rmb_25 = findViewById(R.id.mybalance_rmb25);
        rmb_88 = findViewById(R.id.mybalance_rmb88);
        rmb_318 = findViewById(R.id.mybalance_rmb318);
        rmb_518 = findViewById(R.id.mybalance_rmb518);
        rmb_998 = findViewById(R.id.mybalance_rmb998);
        rmb_other = findViewById(R.id.mybalance_rmb_other);

        sl.setEnabled(false);

        rmb_6.setOnClickListener(this);
        rmb_25.setOnClickListener(this);
        rmb_88.setOnClickListener(this);
        rmb_318.setOnClickListener(this);
        rmb_518.setOnClickListener(this);
        rmb_998.setOnClickListener(this);
        rmb_other.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mybalance_rmb6:
                Recharge_Activity.start(this, 6);
                break;
            case R.id.mybalance_rmb25:
                Recharge_Activity.start(this, 25);
                break;
            case R.id.mybalance_rmb88:
                Recharge_Activity.start(this, 88);
                break;
            case R.id.mybalance_rmb318:
                Recharge_Activity.start(this, 318);
                break;
            case R.id.mybalance_rmb518:
                Recharge_Activity.start(this, 518);
                break;
            case R.id.mybalance_rmb998:
                Recharge_Activity.start(this, 998);
                break;
            case R.id.mybalance_rmb_other:
               new ReCharge_Dialog(this);
                break;

            case R.id.mybalance_put_forward:

                break;

        }
    }
}

package com.sy.bottle.activity.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.CheckSmsCode_Servlet;
import com.sy.bottle.servlet.GetSMSCode_Servlet;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.Util;
import com.sy.bottle.view.EditTextWithClearButton;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 绑定手机号
 */
public class BindPhone_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "BindPhone_Activity";

    public static int Intenttype;

    EditTextWithClearButton phone, smscode;

    Button getsms;

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, BindPhone_Activity.class);
        Intenttype = type;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindphone);

        setTitle("绑定手机号");

        setMenu("绑定");

        setBack(true);

        initview();

        initeven();
    }

    private void initview() {
        phone = findViewById(R.id.bindphone_phone);
        smscode = findViewById(R.id.bindphone_smscode);
        getsms = findViewById(R.id.bindphone_getsms);
    }


    private void initeven() {

        getsms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String phonenum = phone.getText().toString();
        String code = smscode.getText().toString();

        switch (view.getId()) {
            case R.id.menu:
                if (phonenum.length() != 11) {
                    TabToast.makeText("请输入正确的手机号！");
                    return;
                }

                if (code.length() != 6) {
                    TabToast.makeText("请输入正确的验证码！");
                    return;
                }

                Loading.show(this, "验证中");
                SaveUtils.setString(Save_Key.S_手机号,phonenum);
                new CheckSmsCode_Servlet(this).execute(phonenum, code);

                break;
            case R.id.bindphone_getsms:
                if (phonenum.length() == 11) {
                    Loading.show(this, "请稍后");
                    Util.startTimer(getsms, "获取验证码", "重新获取", 60, 1);
                    new GetSMSCode_Servlet().execute(phonenum);
                } else {
                    TabToast.makeText("请输入正确的手机号！");
                }

                break;
        }
    }
}

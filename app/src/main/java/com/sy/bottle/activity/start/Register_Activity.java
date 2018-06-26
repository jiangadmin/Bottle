package com.sy.bottle.activity.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.other.NewWebActivity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
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
 * TODO: 注册
 */
public class Register_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "BindPhone_Activity";

    public static int Intenttype;

    EditTextWithClearButton nickname, sign, phone, smscode;

    Button getsms, submit;

    RadioButton boy, girl;

    String phonenum;

    CheckBox checkBox;

    TextView agreement;

    public static void start(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, Register_Activity.class);
        Intenttype = type;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setTitle("新用户");

        setBack(true);

        initview();

    }

    private void initview() {
        nickname = findViewById(R.id.register_nickname);
        sign = findViewById(R.id.register_sign);
        phone = findViewById(R.id.register_phone);
        smscode = findViewById(R.id.register_sms_code);
        getsms = findViewById(R.id.register_sms_get);
        boy = findViewById(R.id.register_boy);
        girl = findViewById(R.id.register_girl);
        submit = findViewById(R.id.register_submit);
        checkBox = findViewById(R.id.checkBox);
        agreement = findViewById(R.id.agreement);

        getsms.setOnClickListener(this);
        submit.setOnClickListener(this);
        agreement.setOnClickListener(this);

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_昵称))) {
            nickname.setText(SaveUtils.getString(Save_Key.S_昵称));
        }

        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_性别))) {
            switch (SaveUtils.getString(Save_Key.S_性别)) {
                case "1":
                    boy.setChecked(true);
                    break;
                case "2":
                    girl.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        String phonenum = phone.getText().toString();
        String code = smscode.getText().toString();
        String name = nickname.getText().toString();
        String sig = sign.getText().toString();

        switch (view.getId()) {
            case R.id.agreement:
                NewWebActivity.start(this, Const.API + "User_Agreement.html");
                break;
            case R.id.register_submit:

                if (phonenum.length() != 11) {
                    TabToast.makeText("请输入正确的手机号！");
                    return;
                }

                if (code.length() != 6) {
                    TabToast.makeText("请输入正确的验证码！");
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    SaveUtils.setString(Save_Key.S_昵称, "随缘" + phonenum.substring(8));
                }

                if (TextUtils.isEmpty(sig)) {
                    SaveUtils.setString(Save_Key.S_签名, "这个人很懒 什么都没留下");
                }

                if (!boy.isChecked() && !girl.isChecked()) {
                    TabToast.makeText("请选择性别！");
                    return;
                }

                if (!checkBox.isChecked()) {
                    TabToast.makeText("请阅读并同意《用户协议》");
                    return;
                }
                SaveUtils.setString(Save_Key.S_性别, boy.isChecked() ? "1" : "2");
                Loading.show(this, "验证中");
                SaveUtils.setString(Save_Key.S_手机号, phonenum);
                new CheckSmsCode_Servlet(this).execute(phonenum, code);

                break;
            case R.id.register_sms_get:
                phonenum = phone.getText().toString();

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

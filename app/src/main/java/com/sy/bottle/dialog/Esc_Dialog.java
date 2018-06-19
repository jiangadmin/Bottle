package com.sy.bottle.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.mian.mine.Setting_Activity;
import com.sy.bottle.activity.start.Login_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 退出
 */
public class Esc_Dialog extends MyDialog implements View.OnClickListener {
    private static final String TAG = "Esc_Dialog";
    Activity context;

    public Esc_Dialog(@NonNull Activity context) {
        super(context, R.style.myDialogTheme);
        this.context = context;
        show();
    }

    Button change_user, esc_app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_esc);

        change_user = findViewById(R.id.change_user);
        esc_app = findViewById(R.id.esc_app);

        change_user.setOnClickListener(this);
        esc_app.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_user:
                //登出
                TIMManager.getInstance().logout(new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        LogUtil.e(TAG, "logout failed. code: " + code + " errmsg: " + desc);
                        //错误码 code 和错误描述 desc，可用于定位请求失败原因
                        //错误码 code 列表请参见错误码表
                    }

                    @Override
                    public void onSuccess() {
                        //登出成功


                        /**
                         * OpenId
                         */
                       SaveUtils.setString(Save_Key.OPENID,null) ;
                        /**
                         * 昵称
                         */
                        SaveUtils.setString(Save_Key.S_昵称,null) ;
                        /**
                         * 签名
                         */
                        SaveUtils.setString(Save_Key.S_签名,null) ;
                        /**
                         * 头像
                         */
                        SaveUtils.setString(Save_Key.S_头像,null) ;
                        /**
                         * 省份
                         */
                        SaveUtils.setString(Save_Key.S_省份,null) ;
                        /**
                         * 城市
                         */
                        SaveUtils.setString(Save_Key.S_城市,null) ;
                        /**
                         * 性别
                         */
                        SaveUtils.setString(Save_Key.S_性别,null) ;
                        /**
                         * 手机号
                         */
                        SaveUtils.setString(Save_Key.S_手机号,null) ;
                        /**
                         * 登录类型
                         */
                        SaveUtils.setString(Save_Key.S_登录类型,null) ;
                        /**
                         * UID
                         */
                        SaveUtils.setString(Save_Key.UID,null) ;
                        /**
                         * 校验
                         */
                        SaveUtils.setString(Save_Key.S_校验,null) ;
                        /**
                         * 密码
                         */
                        SaveUtils.setString(Save_Key.S_密码,null) ;
                        /**
                         * 积分
                         */
                        SaveUtils.setInt(Save_Key.S_积分,0) ;

                        /**
                         * 能量
                         */
                        SaveUtils.setInt(Save_Key.S_能量,0) ;

                        Login_Activity.start(context);
                        MyApp.finishActivity(Setting_Activity.class);
                        MyApp.finishActivity(Main_Activity.class);

                    }
                });

                break;
            case R.id.esc_app:
                MyApp.AppExit();
                break;
        }
        dismiss();
    }
}

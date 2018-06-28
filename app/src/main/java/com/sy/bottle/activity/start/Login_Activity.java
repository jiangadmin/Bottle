package com.sy.bottle.activity.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Login_Servlet;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * @author: jiangyao
 * @date: 2018/5/18
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录
 */
public class Login_Activity extends Base_Activity implements View.OnClickListener, PlatformActionListener {
    private static final String TAG = "Login_Activity";

    LinearLayout wechat, qq;

    String loginType;

    TextView textView;
    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Login_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        MyApp.finishActivity(Welcome_Activity.class);

        initview();

    }

    private void initview() {

        textView = findViewById(R.id.id);

        wechat = findViewById(R.id.login_wechat);
        qq = findViewById(R.id.login_qq);

        wechat.setOnClickListener(this);
        qq.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Loading.show(this, "启动中");
        switch (view.getId()) {
            case R.id.login_wechat:
                loginType = "1";
                Login(Wechat.NAME);
                break;
            case R.id.login_qq:
                loginType = "2";
                Login(QQ.NAME);

                break;
        }
    }

    /**
     * 社会化登录
     *
     * @param name 平台名
     */
    public void Login(String name) {

        Platform plat = ShareSDK.getPlatform(name);
        plat.removeAccount(true); //移除授权状态和本地缓存，下次授权会重新授权
        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
        plat.setPlatformActionListener(this);//授权回调监听，监听oncomplete，onerror，oncancel三种状态
        if (plat.isClientValid()) {
            //判断是否存在授权凭条的客户端，true是有客户端，false是无
        }
        if (plat.isAuthValid()) {
            //判断是否已经存在授权状态，可以根据自己的登录逻辑设置
            TabToast.makeText("已经授权过了");
            return;
        }
//        plat.authorize();    //要功能，不要数据
        plat.showUser(null);    //要数据不要功能，主要体现在不会重复出现授权界面
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Loading.dismiss();
        LogUtil.e(TAG, hashMap.toString());

        SaveUtils.setString(Save_Key.S_登录类型, loginType);
        SaveUtils.setString(Save_Key.OPENID, platform.getDb().getUserId());
        if (TextUtils.isEmpty((CharSequence) hashMap.get("nickname"))) {
            SaveUtils.setString(Save_Key.S_昵称, "无名氏");
        } else {
            SaveUtils.setString(Save_Key.S_昵称, String.valueOf(hashMap.get("nickname")));
        }
        SaveUtils.setString(Save_Key.S_省份, (String) hashMap.get("province"));
        SaveUtils.setString(Save_Key.S_城市, (String) hashMap.get("city"));

        //微信
        if ("1".equals(loginType)) {
            SaveUtils.setString(Save_Key.S_头像, (String) hashMap.get("headimgurl"));
            SaveUtils.setString(Save_Key.S_性别, String.valueOf(hashMap.get("sex")));
        } else {
            SaveUtils.setString(Save_Key.S_头像, (String) hashMap.get("figureurl_qq_2"));

            SaveUtils.setString(Save_Key.S_性别, (hashMap.get("gender").equals("男") ? "1" : "2"));
        }

        LogUtil.e(TAG, "性别:" + SaveUtils.getString(Save_Key.S_性别));

        LogUtil.e(TAG, "发起登录");

        new Login_Servlet().execute(loginType, platform.getDb().getUserId());

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        LogUtil.e(TAG, throwable.getMessage());
        Loading.dismiss();

    }

    @Override
    public void onCancel(Platform platform, int i) {
        LogUtil.e(TAG, "onCancel");
        Loading.dismiss();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        Loading.dismiss();

    }
}

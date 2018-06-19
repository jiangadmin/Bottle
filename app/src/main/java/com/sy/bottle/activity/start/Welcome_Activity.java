package com.sy.bottle.activity.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Login_Servlet;
import com.sy.bottle.servlet.Register_Servlet;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 欢迎页
 */
public class Welcome_Activity extends Base_Activity {
    private static final String TAG = "Welcome_Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //判定是否有登录数据
        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.OPENID)) && SaveUtils.getBoolean(Save_Key.S_登录)) {

            LogUtil.e(TAG, "快捷登录");

            new Login_Servlet().execute(SaveUtils.getString(Save_Key.S_登录类型), SaveUtils.getString(Save_Key.OPENID));

        }else {
            Login_Activity.start(this);
        }


//        if (SaveUtils.getBoolean(Save_Key.S_跳过引导)) {
//            //判断是否登录过
//
//        } else {
//            Guide_Activity.start(this);
//            MyApp.finishActivity();
//        }
    }
}

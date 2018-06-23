package com.sy.bottle.activity.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.lahm.library.EasyProtectorLib;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Login_Servlet;
import com.sy.bottle.servlet.Update_Servlet;
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

        if (EasyProtectorLib.checkIsRunningInEmulator()) {
            Base_Dialog dialog = new Base_Dialog(this);
            dialog.setTitle("抱歉");
            dialog.setMessage("检测到你当前的设备为模拟器,我们无法对您此设备提供服务！");
            dialog.setOk("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApp.AppExit();
                }
            });

        } else {
            //检测更新
            new Update_Servlet(this).execute();

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

package com.sy.bottle.activity.start;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.servlet.Update_Servlet;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PermissionUtil;

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

        LogUtil.e(TAG, "启动1" );
        PackageManager pm = getPackageManager();

        // 获取是否支持电话
        boolean telephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);

        // 是否支持GSM
        boolean gsm = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM);

        //  是否支持WIFI
        boolean wifi = pm.hasSystemFeature(PackageManager.FEATURE_WIFI);

        //是否支持相机
        boolean cam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);

        //是否支持陀螺仪
        boolean gyr = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);

        //是否支持光线传感器
        boolean light = pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);

        LogUtil.e(TAG, "是否支持电话：" + telephony);
        LogUtil.e(TAG, "是否支持GSM：" + gsm);
        LogUtil.e(TAG, "是否支持WIFI：" + wifi);
        LogUtil.e(TAG, "是否支持相机：" + cam);
        LogUtil.e(TAG, "是否支持陀螺仪：" + gyr);
        LogUtil.e(TAG, "是否支持光线传感器：" + light);


        requestPower();

//        new Update_Servlet(this).execute();
        if (telephony && gsm && wifi && cam && light) {
            LogUtil.e(TAG, "启动2" );
            //检测更新
            new Update_Servlet(this).execute();
        } else {
            Base_Dialog dialog = new Base_Dialog(this);
            dialog.setTitle("抱歉");
            dialog.setMessage("检测到你当前的设备为模拟器,我们无法对您此设备提供服务！");
            dialog.setOk("确定", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyApp.AppExit();
                }
            });
        }

//        if (SaveUtils.getBoolean(Save_Key.S_跳过引导)) {
//            //判断是否登录过
//
//        } else {
//            Guide_Activity.start(this);
//            MyApp.finishActivity();
//        }

    }


    public void requestPower() {
        //判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {//这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限
            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        }
    }
}

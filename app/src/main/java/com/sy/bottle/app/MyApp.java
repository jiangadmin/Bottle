package com.sy.bottle.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.mob.MobSDK;
import com.sy.bottle.R;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.servlet.Timeing_Servlet;
import com.sy.bottle.utils.Foreground;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.bugly.imsdk.crashreport.CrashReport;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class MyApp extends Application {
    private static final String TAG = "MyApp";

    static List<Activity> activities;

    private static Context context;

    public static boolean LogShow = true;
    public static boolean Update_Need = false;

    public static Context getInstance() {
        return context;
    }

    public static IWXAPI api;

    /**
     * 更新地址
     */
    public static String Update_URL;

    /**
     * 正在聊天人的ID
     */
    public static String ChatId = "";

    /**
     * 好友列表
     */
    public static List<Friends_Entity.DataBean> friendsbeans = new ArrayList<>();

    /**
     * 我的数据
     */
    public static UserInfo_Entity.DataBean mybean = new UserInfo_Entity.DataBean();

    public static PayReq request;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //崩溃记录
        CrashReport.initCrashReport(getApplicationContext(), Const.BUGLY_APPID, LogShow);

        Calendar calendar = Calendar.getInstance();
        //获取系统的日期
        //年
        int year = calendar.get(Calendar.YEAR);
        //月
        int month = calendar.get(Calendar.MONTH) + 1;
        //日
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (SaveUtils.getString(Save_Key.S_日期) == null) {
            SaveUtils.setInt(Save_Key.S_捡星, 10);
            SaveUtils.setString(Save_Key.S_日期, year + "." + month + "." + day);
        } else if (!SaveUtils.getString(Save_Key.S_日期).equals(year + "." + month + "." + day)) {
            SaveUtils.setInt(Save_Key.S_捡星, 10);
            SaveUtils.setString(Save_Key.S_日期, year + "." + month + "." + day);
        }

        Foreground.init(this);

        //模拟心跳
        new Timeing_Servlet().onBind(new Intent());


        //初始化 SDK 基本配置
        TIMSdkConfig config = new TIMSdkConfig(Const.SDK_APPID)
                .enableCrashReport(false)
                .enableLogPrint(true)
                .setLogLevel(TIMLogLevel.DEBUG)
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/bottle/");
//        TIMSdkConfig = new TIMSdkConfig(Const.SDK_APPID);
        TIMManager.getInstance().init(this, config);

        //初始化Mob
        MobSDK.init(this);

        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Const.Wechat_AppID);

        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.logo);
                    }
                }
            });
        }
    }

    /**
     * add Activity 添加Activity到栈
     */
    public static void addActivity(Activity activity) {
        if (activities == null) {
            activities = new ArrayList<>();
        }
        activities.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public static Activity currentActivity() {
        Activity activity = activities.get(activities.size() - 1);
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public static void finishActivity() {
        try {
            Activity activity = activities.get(activities.size() - 1);
            finishActivity(activity);
        } catch (Exception e) {
            LogUtil.e(TAG, "出错了");
        }

    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */

    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activities.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束其他Activity
     */
    public static void finishOtherActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (!activity.getClass().equals(cls)) {
                    activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public static void finishActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    /**
     * 查询栈中是否有这个
     *
     * @param cls
     */
    public static boolean QueryActivity(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public static void finishAllActivity() {
        for (int i = 0, size = activities.size(); i < size; i++) {
            if (null != activities.get(i)) {
                activities.get(i).finish();
            }
        }
        activities.clear();
        System.exit(0);
    }

    /**
     * 退出应用程序
     */
    public static void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }


    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        AppExit();
        super.onLowMemory();
    }

    public boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                LogUtil.e(TAG, "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
//                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
////                    MyLogUtils.Loge(context.getPackageName(), "处于后台"
////                           + appProcess.processName);
//                    return true;
//                } else {
////                    MyLogUtils.Loge(context.getPackageName(), "处于前台"
////                            + appProcess.processName);
//                    return false;
//                }
            }
        }
        return false;
    }

}

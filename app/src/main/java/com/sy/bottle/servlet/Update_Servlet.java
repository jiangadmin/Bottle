package com.sy.bottle.servlet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.mian.mine.Setting_Activity;
import com.sy.bottle.activity.start.Login_Activity;
import com.sy.bottle.activity.start.Welcome_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.UpdateManger;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.Update_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.ToolUtils;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMManager;

/**
 * Created by jiang
 * on 2017/2/16.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:TODO 获取版本号
 * update：
 */
public class Update_Servlet extends AsyncTask<String, ProgressDialog, Update_Entity> {
    private static final String TAG = "Update_Servlet";

    Activity activity;

    public Update_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Update_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET, Const.API + "versions", null);
        LogUtil.e(TAG, res);
        Update_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Update_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败!");
        } else {
            try {
                entity = new Gson().fromJson(res, Update_Entity.class);
            } catch (Exception e) {
                entity = new Update_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败!");
                LogUtil.e(TAG, "解析失败" + e.getMessage());

            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Update_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:

                MyApp.Update_URL = entity.getData().getUrl();     //更新包地址
                versionControl(entity);

                break;

            default:
                TabToast.makeText(entity.getMessage());
                break;
        }

    }


    /**
     * 版本验证
     */
    private void versionControl(Update_Entity res) {
        LogUtil.e(TAG, "版本验证");
        //判断网络版本和本地版本
        if (Integer.valueOf(res.getData().getVer().replace(".", "")) >
                Integer.valueOf(ToolUtils.getVersionName().replace(".", ""))) {
            LogUtil.e(TAG, "需要更新");
            //记录升级
            SaveUtils.setBoolean(Save_Key.S_升级成功 + res.getData().getVer(), false);
            //判断是否是强制更新
            if (res.getData().getIs_must().equals("1")) {
                MyApp.Update_Need = true;

            }

            new UpdateManger(activity, Integer.valueOf(res.getData().getVer().replace(".", ""))).showNoticeDialog(res.getData().getContent());

        } else {

            if (activity instanceof Setting_Activity) {
                TabToast.makeText("已是最新版本");
            }

            if (activity instanceof Welcome_Activity) {
                //判断腾讯云有无登录
                if (!TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())) {
                    //查询个人资料
                    new UserInfo_Servlet(MyApp.currentActivity()).execute();

                    SaveUtils.setBoolean(Save_Key.S_登录, true);
                    Main_Activity.start(MyApp.currentActivity());
                    MyApp.finishActivity(Login_Activity.class);
                    MyApp.finishActivity(Welcome_Activity.class);

                    return;
                }

                //判定是否有登录数据
                if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.OPENID)) && SaveUtils.getBoolean(Save_Key.S_登录)) {

                    LogUtil.e(TAG, "快捷登录");

                    new Login_Servlet().execute(SaveUtils.getString(Save_Key.S_登录类型), SaveUtils.getString(Save_Key.OPENID));

                } else {
                    LogUtil.e(TAG, "启动到登录");
                    Login_Activity.start(activity);
                }
            }

            LogUtil.e(TAG, "无需更新");
        }
    }


}

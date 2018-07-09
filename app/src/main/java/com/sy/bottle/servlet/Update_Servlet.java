package com.sy.bottle.servlet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Setting_Activity;
import com.sy.bottle.activity.start.Login_Activity;
import com.sy.bottle.activity.start.Welcome_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.Update_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.ToolUtils;
import com.sy.bottle.view.TabToast;

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
        String res = HttpUtil.request(HttpUtil.GET, Const.API + "versions/" + ToolUtils.getVersionName() + "/" + Const.Channel, null);
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
                if (activity instanceof Setting_Activity) {
                    if (entity.getData() == null) {
                        TabToast.makeText("已是最新版本");
                    } else {
                        updat(entity.getData().getUrl());
                    }
                }
                if (activity instanceof Welcome_Activity) {

                    if (entity.getData() == null) {
                        LogUtil.e(TAG, "没有更新数据");
                        //判定是否有登录数据
                        if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.OPENID)) && SaveUtils.getBoolean(Save_Key.S_登录)) {

                            LogUtil.e(TAG, "快捷登录");

                            new Login_Servlet().execute(SaveUtils.getString(Save_Key.S_登录类型), SaveUtils.getString(Save_Key.OPENID));

                        } else {
                            LogUtil.e(TAG, "启动到登录");
                            Login_Activity.start(activity);
                        }
                    } else {
                        LogUtil.e(TAG, "有更新数据");
                        updat(entity.getData().getUrl());

                        if (entity.getData().getIs_must().equals("0")) {
                            //判定是否有登录数据
                            if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.OPENID)) && SaveUtils.getBoolean(Save_Key.S_登录)) {

                                LogUtil.e(TAG, "快捷登录");

                                new Login_Servlet().execute(SaveUtils.getString(Save_Key.S_登录类型), SaveUtils.getString(Save_Key.OPENID));

                            } else {
                                Login_Activity.start(activity);
                            }
                        }
                    }

                }
                break;

            default:
                TabToast.makeText(entity.getMessage());
                break;
        }

    }

    public void updat(String url) {

//        new Login_Servlet().execute(SaveUtils.getString(Save_Key.S_登录类型), SaveUtils.getString(Save_Key.OPENID));
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));//Url 就是你要打开的网址
            intent.setAction(Intent.ACTION_VIEW);
            activity.startActivity(intent); //启动浏览器
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }

}

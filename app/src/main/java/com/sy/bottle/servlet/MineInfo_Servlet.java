package com.sy.bottle.servlet;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.activity.mian.mine.Mine_Fragment;
import com.sy.bottle.activity.mian.mine.Mine_Info_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.MineInfo_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取个人信息
 */
public class MineInfo_Servlet extends AsyncTask<String, Integer, MineInfo_Entity> {
    private static final String TAG = "MineInfo_Servlet";

    Activity activity;

    Fragment fragment;

    public MineInfo_Servlet(Fragment fragment) {
        this.fragment = fragment;
    }

    public MineInfo_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected MineInfo_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET,Const.API + "users/" + SaveUtils.getString(Save_Key.UID),null);

        MineInfo_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new MineInfo_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                LogUtil.e(TAG, res);
                entity = new Gson().fromJson(res, MineInfo_Entity.class);
            } catch (Exception e) {
                entity = new MineInfo_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(MineInfo_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (fragment instanceof Mine_Fragment) {
                    ((Mine_Fragment) fragment).initeven(entity.getData());
                }
                if (activity instanceof Mine_Info_Activity) {
                    ((Mine_Info_Activity) activity).CallBack_Info(entity.getData());
                }
                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).CallBack_Info(entity.getData());
                }
                break;
            case 401:
                new ReLogin_Dialog(MyApp.currentActivity());
                break;
            default:
                break;
        }
    }
}

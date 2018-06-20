package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Log_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Gift_Get_Log_Entity;
import com.sy.bottle.entity.Gift_Set_Log_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangyao
 * @date: 2018/6/14
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 接收记录
 */
public class Gift_Get_Log_Servlet extends AsyncTask<String, Integer, Gift_Get_Log_Entity> {
    private static final String TAG = "Gift_Get_Log_Servlet";

    Activity activity;

    public Gift_Get_Log_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Gift_Get_Log_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "present_receive_records/" + SaveUtils.getString(Save_Key.UID), null);

        Gift_Get_Log_Entity entity;
        if (TextUtils.isEmpty(res)) {
            entity = new Gift_Get_Log_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Gift_Get_Log_Entity.class);

            } catch (Exception e) {
                entity = new Gift_Get_Log_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Gift_Get_Log_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:

                if (activity instanceof Log_Activity) {
                    ((Log_Activity) activity).CallBack_Gift_Get(entity.getData());
                }

                break;
            case 400:

                if (entity.getMessage().equals("无数据")) {
                    ((Log_Activity) activity).CallBack_Gift_Get(null);
                }
                break;

            case 401:
                new ReLogin_Dialog();
                break;
            default:
                break;
        }
    }
}

package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.sy.bottle.activity.mian.other.Put_forward_Log_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Put_Forward_Log_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.SaveUtils;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Put_Forward_Log_Servlet extends AsyncTask<String, Integer, Put_Forward_Log_Entity> {
    private static final String TAG = "Put_Forward_Log_Servlet";

    Activity activity;

    public Put_Forward_Log_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Put_Forward_Log_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "withdraws/" + SaveUtils.getString(Save_Key.UID), null);

        Put_Forward_Log_Entity entity;


        if (TextUtils.isEmpty(res)) {
            entity = new Put_Forward_Log_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Put_Forward_Log_Entity.class);
            } catch (Exception e) {
                entity = new Put_Forward_Log_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Put_Forward_Log_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Put_forward_Log_Activity){
                    ((Put_forward_Log_Activity) activity).CallBack(entity.getData());
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }

}

package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Bottle_Get_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/6/12
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 回复数量增加
 */
public class Bottle_CallBack_Servlet extends AsyncTask<String, Integer, Bottle_Get_Entity> {
    private static final String TAG = "Bottle_Get_Servlet";

    @Override
    protected Bottle_Get_Entity doInBackground(String... strings) {

        Map map = new HashMap();
        map.put("bottle_id", strings[0]);

        String res = HttpUtil.request(HttpUtil.PUT, Const.API + "bottles/" + SaveUtils.getString(Save_Key.UID), map);

        LogUtil.e(TAG, res);

        Bottle_Get_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Bottle_Get_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Bottle_Get_Entity.class);
            } catch (Exception e) {
                entity = new Bottle_Get_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Bottle_Get_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:

                break;
            case 401:
                new ReLogin_Dialog(MyApp.currentActivity());
                break;
            default:
                LogUtil.e(TAG, entity.getMessage());
                break;
        }
    }
}

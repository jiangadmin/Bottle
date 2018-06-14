package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.dialog.Bottle_Get_Dialog;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Bottle_Get_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangyao
 * @date: 2018/6/7
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 捡瓶子
 */
public class Bottle_Get_Servlet extends AsyncTask<String, Integer, Bottle_Get_Entity> {
    private static final String TAG = "Bottle_Get_Servlet";

    Activity activity;

    public Bottle_Get_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Bottle_Get_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET,Const.API + "bottles/" + SaveUtils.getString(Save_Key.UID),null);

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
                Bottle_Get_Dialog dialog = new Bottle_Get_Dialog(activity);
                dialog.init(entity.getData());
                break;
            case 400:
                if (entity.getMessage().equals("无数据")){
                    Base_Dialog base_dialog = new Base_Dialog(activity);
                    base_dialog.setMessage("没有捞到，再去捞一次吧");
                    base_dialog.setOk("好的",null);
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
            default:
                LogUtil.e(TAG, entity.getMessage());
                break;
        }
    }
}

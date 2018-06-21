package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.MyBalance_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Goods_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;

/**
 * @author: jiangadmin
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 商品列表
 */
public class Goods_Servlet extends AsyncTask<String, Integer, Goods_Entity> {
    private static final String TAG = "Goods_Servlet";

    Activity activity;

    public Goods_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Goods_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "recharges/"+strings[0], null);
        Goods_Entity entity;
        if (TextUtils.isEmpty(res)) {
            entity = new Goods_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Goods_Entity.class);

            } catch (Exception e) {
                entity = new Goods_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Goods_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof MyBalance_Activity){
                    ((MyBalance_Activity) activity).CallBack_Goods(entity.getData());
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}

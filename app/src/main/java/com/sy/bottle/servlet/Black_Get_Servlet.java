package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Black_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.utils.HttpUtil;

/**
 * @author: jiangadmin
 * @date: 2018/6/14
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 查询黑名单
 */
public class Black_Get_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "Black_Get_Servlet";

    Activity activity;

    public Black_Get_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "", null);
        Base_Entity entity;
        if (TextUtils.isEmpty(res)) {
            entity = new Base_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Base_Entity.class);
            } catch (Exception e) {
                entity = new Base_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Base_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Black_Activity) {
                    ((Black_Activity) activity).CallBack();
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}

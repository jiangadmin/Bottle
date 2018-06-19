package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangyao
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 好友列表
 */
public class Friends_Servlet extends AsyncTask<String, Integer, Friends_Entity> {
    private static final String TAG = "Friends_Servlet";

    Main_Activity activity;

    public Friends_Servlet(Main_Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Friends_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET, Const.API + "friends/" + SaveUtils.getString(Save_Key.UID), null);

        LogUtil.e(TAG, res);

        Friends_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Friends_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Friends_Entity.class);
            } catch (Exception e) {
                entity = new Friends_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Friends_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                MyApp.friendsbeans.clear();
                MyApp.friendsbeans.addAll(entity.getData());
                activity.CallBack_Friend();
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}

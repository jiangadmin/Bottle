package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.mian.other.Help_Activity;
import com.sy.bottle.activity.mian.other.Web_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Webtxt_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;

/**
 * @author: jiangadmin
 * @date: 2018/6/21
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取公告
 */
public class Notice_Servlet extends AsyncTask<String, Integer, Webtxt_Entity> {
    private static final String TAG = "Notice_Servlet";

    Activity activity;

    public Notice_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Webtxt_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "notices/" + strings[0], null);

        Webtxt_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Webtxt_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Webtxt_Entity.class);
            } catch (Exception e) {
                entity = new Webtxt_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Webtxt_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Main_Activity) {
                    Web_Activity.start(activity, "官方公告", entity.getData().getContent());
                }

                if (activity instanceof Help_Activity) {
                    ((Help_Activity) activity).CallBack_WebTxt(entity.getData().getContent());
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}

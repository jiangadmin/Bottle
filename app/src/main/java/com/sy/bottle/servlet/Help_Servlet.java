package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.other.Help_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Help_Entity;
import com.sy.bottle.utils.HttpUtil;

/**
 * @author: jiangadmin
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 帮助列表
 */
public class Help_Servlet extends AsyncTask<String,Integer,Help_Entity> {
    private static final String TAG = "Help_Servlet";

    Activity activity;

    public Help_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Help_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET, Const.API+"faq",null);

        Help_Entity entity;

        if (TextUtils.isEmpty(res)){
            entity = new Help_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        }else {
            try {
                entity = new Gson().fromJson(res,Help_Entity.class);
            }catch (Exception e){
                entity = new Help_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Help_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()){
            case 200:
                if (activity instanceof Help_Activity){
                    ((Help_Activity) activity).CallBack_HelpList(entity.getData());
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}

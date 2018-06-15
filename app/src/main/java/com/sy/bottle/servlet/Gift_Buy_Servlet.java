package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.MyBalance_Activity;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/6/14
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 购买礼物
 */
public class Gift_Buy_Servlet extends AsyncTask<Map, Integer, Base_Entity> {
    private static final String TAG = "Gift_Buy_Servlet";

    Activity activity;

    public Gift_Buy_Servlet(Activity activity) {
        this.activity = activity;
    }

    Map map;

    @Override
    protected Base_Entity doInBackground(Map... maps) {
        this.map = maps[0];
        Map map1 = new HashMap();
        map1.put("present_id",String.valueOf( map.get("Id")));

        String res = HttpUtil.request(HttpUtil.POST, Const.API + "presents/" + SaveUtils.getString(Save_Key.UID), map1);

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
                LogUtil.e(TAG, e.getMessage());
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
                new Gift_For_Servlet(activity).execute(map);
                break;
            case 400:
                Base_Dialog dialog = new Base_Dialog(activity);
                dialog.setMessage(entity.getMessage());
                dialog.setOk("去充值", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyBalance_Activity.start(activity);
                    }
                });
                dialog.setEsc("取消", null);
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }
}

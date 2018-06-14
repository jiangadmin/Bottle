package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.ToolUtils;
import com.sy.bottle.view.TabToast;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 捡积分
 */
public class Scores_Get_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "Scores_Get_Servlet";

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();

        map.put("111","一");

        String res = HttpUtil.request(HttpUtil.PUT, Const.API + "scores/" + SaveUtils.getString(Save_Key.UID), map);

        Base_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Base_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Base_Entity.class);
            } catch (Exception e) {
                entity = new Base_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
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
                TabToast.makeText(entity.getMessage());
                SaveUtils.setInt(Save_Key.S_积分, SaveUtils.getInt(Save_Key.S_积分) + ToolUtils.StringInInt(entity.getMessage()));
                break;
            case 401:
                new ReLogin_Dialog();;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }
    }
}

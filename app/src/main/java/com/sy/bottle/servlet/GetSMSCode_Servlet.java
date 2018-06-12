package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取短信验证码
 */
public class GetSMSCode_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "GetSMSCode_Servlet";

    String phone;

    @Override
    protected Base_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        phone = strings[0];
        map.put("phone", phone);
        String res = HttpUtil.request(HttpUtil.POST,Const.API + "codes", map);

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

        if (entity.getStatus() == 200) {
            SaveUtils.setString(Save_Key.S_手机号,phone);
        }

        LogUtil.e(TAG, entity.getMessage());
        TabToast.makeText(entity.getMessage());
    }
}

package com.sy.bottle.servlet;

import android.app.Activity;
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
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/5/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 校验短信验证码
 */
public class CheckSmsCode_Servlet extends AsyncTask<String, Integer, Base_Entity> {
    private static final String TAG = "CheckSmsCode_Servlet";

    Activity activity;

    String phone;

    public CheckSmsCode_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(String... strings) {
        phone = strings[0];
        String res = HttpUtil.request(HttpUtil.GET,Const.API + "codes/" + phone + "/" + strings[1],null);
        LogUtil.e(TAG, res);
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
                Loading.show(activity, "请稍后");
                SaveUtils.setString(Save_Key.S_手机号, phone);
                //注册操作
                new Register_Servlet().execute();

                break;
            case 401:
                new ReLogin_Dialog(MyApp.currentActivity());
                break;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }
    }
}

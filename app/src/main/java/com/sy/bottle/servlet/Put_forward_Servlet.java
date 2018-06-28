package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.MyBalance_Activity;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 申请提取
 */
public class Put_forward_Servlet extends AsyncTask<Put_forward_Servlet.Info, Integer, Base_Entity> {
    private static final String TAG = "Put_forward_Servlet";
    Activity activity;

    public Put_forward_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(Info... infos) {
        Info info = infos[0];

        Map map = new HashMap();
        map.put("type", "1");
        map.put("real_name", info.getReal_name());
        map.put("account", info.getAccount());
        map.put("price", info.getPrice());
        map.put("money", info.getMoney());

        String res = HttpUtil.request(HttpUtil.POST, Const.API+"withdraws/"+ SaveUtils.getString(Save_Key.UID),map);
        LogUtil.e(TAG,res);
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
        switch (entity.getStatus()){
            case 200:
                TabToast.makeText(entity.getMessage());
                MyApp.finishActivity(MyBalance_Activity.class);
                MyApp.finishActivity(activity);
                break;
            case 401:
                new ReLogin_Dialog();
                break;

            default:
                TabToast.makeText(entity.getMessage());
                break;

        }
    }

    public static class Info {
        private String real_name;
        private String account;
        private String price;
        private String money;

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}

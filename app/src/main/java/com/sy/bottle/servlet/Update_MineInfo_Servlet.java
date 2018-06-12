package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 修改个人资料
 */
public class Update_MineInfo_Servlet extends AsyncTask<Update_MineInfo_Servlet.Info, Integer, Base_Entity> {
    private static final String TAG = "Update_MineInfo_Servlet";

    Activity activity;

    public Update_MineInfo_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Base_Entity doInBackground(Info... infos) {
        Info info = infos[0];
        Map map = new HashMap();
        //昵称
        if (!TextUtils.isEmpty(info.getNikename())) {
            map.put("nikename", info.getNikename());
        }
        //签名
        if (!TextUtils.isEmpty(info.getSign())) {
            map.put("sign", info.getSign());
        }
        //省
        if (!TextUtils.isEmpty(info.getProvince())) {
            map.put("province", info.getProvince());
        }
        //市
        if (!TextUtils.isEmpty(info.getCity())) {
            map.put("city", info.getCity());
        }
        //区
        if (!TextUtils.isEmpty(info.getArea())) {
            map.put("area", info.getArea());
        }

        String res = HttpUtil.request(HttpUtil.PUT, Const.API + "users/" + SaveUtils.getString(Save_Key.UID), map);

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
                TabToast.makeText("修改成功");
                break;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }

    }

    public static class Info {
        private String nikename;
        private String sign;
        private String province;
        private String city;
        private String area;

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}

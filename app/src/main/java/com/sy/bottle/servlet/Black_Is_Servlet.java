package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.friend.FriendInfo_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangadmin
 * @date: 2018/6/14
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 查询时候是黑名单
 */
public class Black_Is_Servlet extends AsyncTask<String, Integer, Boolean> {
    private static final String TAG = "Black_Get_Servlet";

    Activity activity;

    public Black_Is_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "blacklists/" + SaveUtils.getString(Save_Key.UID), null);

        LogUtil.e(TAG,res);

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

        if (entity.getData() != null) {
            if (entity.getData().size() > 0) {
                for (Friends_Entity.DataBean bean : entity.getData()) {
                    if (bean.getFriend_id().equals(strings[0])) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Loading.dismiss();

        if (activity instanceof FriendInfo_Activity) {
            ((FriendInfo_Activity) activity).CallBack_IsBlack(aBoolean);
        }
    }
}

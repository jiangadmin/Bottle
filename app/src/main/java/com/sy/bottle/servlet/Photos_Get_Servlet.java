package com.sy.bottle.servlet;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.friend.FriendInfo_Activity;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.activity.mian.mine.Mine_Info_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Photos_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取相册
 */
public class Photos_Get_Servlet extends AsyncTask<String, Integer, Photos_Entity> {
    private static final String TAG = "Photos_Servlet";

    Fragment fragment;

    Activity activity;

    public Photos_Get_Servlet(Fragment fragment) {
        this.fragment = fragment;
    }

    public Photos_Get_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Photos_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET, Const.API + "photos/" + strings[0], null);

        LogUtil.e(TAG, res);

        Photos_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Photos_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Photos_Entity.class);
            } catch (Exception e) {
                entity = new Photos_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Photos_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:

                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).CallBack_Photos(entity.getData());
                }

                if (activity instanceof Mine_Info_Activity) {
                    ((Mine_Info_Activity) activity).CallBack_Phtots(entity.getData());
                }


                if (activity instanceof FriendInfo_Activity) {
                    ((FriendInfo_Activity) activity).CallBack_Photos(entity.getData());
                }

                break;
            case 400:

                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).CallBack_Photos(null);
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }
    }
}

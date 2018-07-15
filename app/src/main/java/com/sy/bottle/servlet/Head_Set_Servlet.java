package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Avatars_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 设置头像
 */
public class Head_Set_Servlet extends AsyncTask<String, Integer, Avatars_Entity> {
    private static final String TAG = "Photos_Set_Servlet";

    Activity activity;

    public Head_Set_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Avatars_Entity doInBackground(String... strings) {

        String res = HttpUtil.uploadFile(Const.API + "avatars/" + SaveUtils.getString(Save_Key.UID), strings[0]);
        LogUtil.e(TAG,res);
        Avatars_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Avatars_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Avatars_Entity.class);
            } catch (Exception e) {
                entity = new Avatars_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(final Avatars_Entity entity) {
        super.onPostExecute(entity);

        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).initinfo();
                }

                TIMFriendshipManager.ModifyUserProfileParam userProfileParam = new TIMFriendshipManager.ModifyUserProfileParam();
                userProfileParam.setFaceUrl(entity.getUrl());

                TIMFriendshipManager.getInstance().modifyProfile(userProfileParam, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        TabToast.makeText(s);
                    }

                    @Override
                    public void onSuccess() {
                        SaveUtils.setString(Save_Key.S_头像, entity.getUrl());
                        TabToast.makeText("修改成功");

                    }
                });
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

package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Gift_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Gift_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.ChatInput;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 礼物列表
 */

public class GiftList_Servlet extends AsyncTask<String, Integer, Gift_Entity> {
    private static final String TAG = "GiftList_Servlet";

    ChatInput chatInput;
    Activity activity;

    public GiftList_Servlet(Activity activity) {
        this.activity = activity;
    }

    public GiftList_Servlet(ChatInput chatInput) {
        this.chatInput = chatInput;
    }

    @Override
    protected Gift_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API + "presents/" + SaveUtils.getString(Save_Key.UID), null);

        Gift_Entity entity;
        if (TextUtils.isEmpty(res)) {
            entity = new Gift_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Gift_Entity.class);

            } catch (Exception e) {
                entity = new Gift_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Gift_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();
        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Gift_Activity) {
                    ((Gift_Activity) activity).CallBack(entity.getData());
                }

                if (chatInput != null) {
                    chatInput.CallBack_Gift(entity.getData());
                }
                break;
            default:
                TabToast.makeText(entity.getMessage());
                break;
        }
    }
}

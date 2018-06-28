package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.other.Put_forward_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Ratio_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:星星比率
 */
public class Ratio_Serlvet extends AsyncTask<String,Integer,Ratio_Entity> {
    private static final String TAG = "Ratio_Serlvet";

    Activity activity;

    public Ratio_Serlvet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Ratio_Entity doInBackground(String... strings) {

        String res = HttpUtil.request(HttpUtil.GET, Const.API+"moneys/"+strings[0],null);
        LogUtil.e(TAG,res);
        Ratio_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Ratio_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Ratio_Entity.class);
            } catch (Exception e) {
                entity = new Ratio_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Ratio_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();
        switch (entity.getStatus()){
            case 200:
                if (activity instanceof Put_forward_Activity){
                    ((Put_forward_Activity) activity).CallBack_Money(entity.getData().getMoney());
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

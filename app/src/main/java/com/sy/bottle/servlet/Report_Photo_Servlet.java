package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.other.Report_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Avatars_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Report_Photo_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangadmin
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 举报图片上传
 */
public class Report_Photo_Servlet extends AsyncTask<String, Integer, Report_Photo_Entity> {
    private static final String TAG = "Photos_Set_Servlet";

    Activity activity;

    public Report_Photo_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Report_Photo_Entity doInBackground(String... strings) {

        String res = HttpUtil.uploadFile(Const.API + "complaint_photos/" + SaveUtils.getString(Save_Key.UID), strings[0]);

        Report_Photo_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Report_Photo_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Report_Photo_Entity.class);
            } catch (Exception e) {
                entity = new Report_Photo_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Report_Photo_Entity entity) {
        super.onPostExecute(entity);

        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (activity instanceof Report_Activity) {
                    ((Report_Activity) activity).CallBcak(entity.getData().getUrl());
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
            default:
                break;
        }
    }
}

package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.Bottle_Random_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/7
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取随机内容
 */
public class Bottle_Random_Servlet extends AsyncTask<String, Integer, Bottle_Random_Entity> {
    private static final String TAG = "Bottle_Random_Servlet";

    EditText editText;

    public Bottle_Random_Servlet(EditText editText) {
        this.editText = editText;
    }

    @Override
    protected Bottle_Random_Entity doInBackground(String... strings) {
        String res = HttpUtil.request(HttpUtil.GET,Const.API + "random_bottles/" + SaveUtils.getString(Save_Key.UID),null);
        LogUtil.e(TAG,res);
        Bottle_Random_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Bottle_Random_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Bottle_Random_Entity.class);
            } catch (Exception e) {
                entity = new Bottle_Random_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(Bottle_Random_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                editText.setText(entity.getData());
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

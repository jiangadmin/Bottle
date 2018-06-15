package com.sy.bottle.servlet;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.start.Login_Activity;
import com.sy.bottle.activity.start.Register_Activity;
import com.sy.bottle.activity.start.Welcome_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Login_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.ToolUtils;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/5/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录
 */

public class Login_Servlet extends AsyncTask<String, Integer, Login_Entity> {
    private static final String TAG = "Login_Servlet";

    @Override
    protected Login_Entity doInBackground(String... strings) {
        Map map = new HashMap();
        map.put("channel", strings[0]);
        map.put("openid", strings[1]);
        map.put("device_id", ToolUtils.getMyUUID());

        String res = HttpUtil.request(HttpUtil.PUT, Const.API + "tokens", map);

        LogUtil.e(TAG, res);
        Login_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Login_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败");
        } else {
            try {
                entity = new Gson().fromJson(res, Login_Entity.class);
            } catch (Exception e) {
                entity = new Login_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败");
            }
        }
        return entity;
    }

    @Override
    protected void onPostExecute(Login_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:

                SaveUtils.setString(Save_Key.UID, entity.getData().getUid());
                SaveUtils.setString(Save_Key.S_校验, entity.getData().getAccess_token());
                SaveUtils.setString(Save_Key.S_密码, entity.getData().getUsersig());

                TIMManager.getInstance().login(entity.getData().getUid(), entity.getData().getUsersig(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {

                        if (MyApp.currentActivity() instanceof Welcome_Activity){
                            Login_Activity.start(MyApp.currentActivity());
                            MyApp.finishActivity(Welcome_Activity.class);

                        }else {
                            TabToast.makeText("登录失败，请稍后重试");
                        }
                    }

                    @Override
                    public void onSuccess() {
                        TabToast.makeText("登录成功");

                        SaveUtils.setBoolean(Save_Key.S_登录, true);
                        Main_Activity.start(MyApp.currentActivity());
                        MyApp.finishActivity(Login_Activity.class);
                        MyApp.finishActivity(Welcome_Activity.class);

                    }
                });

                break;
            //未注册
            case 202:
                Register_Activity.start(MyApp.currentActivity(), 1);
                break;
            default:
                TabToast.makeText(entity.getMessage());
                LogUtil.e(TAG, entity.getMessage());
                break;
        }
    }
}

package com.sy.bottle.servlet;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.friend.AddFriend_Activity;
import com.sy.bottle.activity.mian.friend.FriendInfo_Activity;
import com.sy.bottle.activity.mian.friend.Profile_Activity;
import com.sy.bottle.activity.mian.friend.UserInfo_Activity;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.activity.mian.mine.Mine_Fragment;
import com.sy.bottle.activity.mian.mine.Mine_Info_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.MyDialog;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.dialog.SearchFriend_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 获取用户信息
 */
public class UserInfo_Servlet extends AsyncTask<String, Integer, UserInfo_Entity> {
    private static final String TAG = "MineInfo_Servlet";

    Activity activity;

    Fragment fragment;

    ImageView imageView;

    SearchFriend_Dialog myDialog;

    public UserInfo_Servlet(SearchFriend_Dialog myDialog) {
        this.myDialog = myDialog;
    }

    public UserInfo_Servlet(ImageView imageView) {
        this.imageView = imageView;
    }

    public UserInfo_Servlet(Fragment fragment) {
        this.fragment = fragment;
    }

    public UserInfo_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected UserInfo_Entity doInBackground(String... strings) {

        String userid = SaveUtils.getString(Save_Key.UID);
        if (strings.length>0){
            userid = strings[0];
        }
        String res = HttpUtil.request(HttpUtil.GET, Const.API + "users/" + userid, null);

        LogUtil.e(TAG,res);
        UserInfo_Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new UserInfo_Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                LogUtil.e(TAG, res);
                entity = new Gson().fromJson(res, UserInfo_Entity.class);
            } catch (Exception e) {
                entity = new UserInfo_Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }

    @Override
    protected void onPostExecute(UserInfo_Entity entity) {
        super.onPostExecute(entity);
        Loading.dismiss();

        switch (entity.getStatus()) {
            case 200:
                if (fragment instanceof Mine_Fragment) {
                    ((Mine_Fragment) fragment).initeven(entity.getData());
                }
                if (activity instanceof Mine_Info_Activity) {
                    ((Mine_Info_Activity) activity).CallBack_Info(entity.getData());
                }
                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).CallBack_Info(entity.getData());
                }
                if (activity instanceof AddFriend_Activity) {
                    ((AddFriend_Activity) activity).CallBack(entity.getData());
                }
                if (activity instanceof Profile_Activity) {
                    ((Profile_Activity) activity).CallBack(entity.getData());
                }
                if (imageView!=null){
                    PicassoUtlis.img(entity.getData().getAvatar(),imageView);
                }
                if (myDialog!=null){
                    myDialog.dismiss();
                    UserInfo_Activity.start(MyApp.currentActivity(),entity.getData());
                }

                if (activity instanceof UserInfo_Activity){
                    ((UserInfo_Activity) activity).CallBack_UserInfo(entity.getData());
                }

                if (activity instanceof FriendInfo_Activity){
                    ((FriendInfo_Activity) activity).CallBack(entity.getData());
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

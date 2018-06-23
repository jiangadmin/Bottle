package com.sy.bottle.servlet;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.activity.mian.friend.AddFriend_Activity;
import com.sy.bottle.activity.mian.friend.FriendInfo_Activity;
import com.sy.bottle.activity.mian.friend.Profile_Activity;
import com.sy.bottle.activity.mian.friend.UserInfo_Activity;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.activity.mian.mine.Mine_Fragment;
import com.sy.bottle.activity.mian.mine.Mine_Info_Activity;
import com.sy.bottle.adapters.ConversationAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.dialog.SearchFriend_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendGenderType;
import com.tencent.imsdk.TIMFriendshipManager;

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

    ConversationAdapter.ViewHolder viewHolder;

    SearchFriend_Dialog myDialog;

    public UserInfo_Servlet(SearchFriend_Dialog myDialog) {
        this.myDialog = myDialog;
    }


    public UserInfo_Servlet(Fragment fragment) {
        this.fragment = fragment;
    }

    public UserInfo_Servlet(Activity activity) {
        this.activity = activity;
    }

    public UserInfo_Servlet(ConversationAdapter.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    @Override
    protected UserInfo_Entity doInBackground(String... strings) {

        String userid = SaveUtils.getString(Save_Key.UID);
        if (strings.length > 0) {
            userid = strings[0];
        }
        String res = HttpUtil.request(HttpUtil.GET, Const.API + "users/" + userid, null);

        LogUtil.e(TAG, res);
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

                //如果是自己
                if (entity.getData().getId().equals(SaveUtils.getString(Save_Key.UID))) {

                    //初始化参数，修改昵称为“cat”
                    TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
                    param.setNickname(entity.getData().getNikename());
                    param.setFaceUrl(entity.getData().getAvatar());
                    param.setSelfSignature(entity.getData().getSign());
                    if (entity.getData().getSex().equals("1")) {
                        param.setGender(TIMFriendGenderType.Male);
                    } else {
                        param.setGender(TIMFriendGenderType.Female);
                    }

                    TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {

                        }

                        @Override
                        public void onSuccess() {
                            LogUtil.e(TAG, "修改成功");
                        }
                    });

                    SaveUtils.setInt(Save_Key.S_积分, entity.getData().getScore());
                    SaveUtils.setInt(Save_Key.S_能量, entity.getData().getBalance());
                } else {
                    //如果不是自己
                    //存储对方昵称
                    SaveUtils.setString(Save_Key.S_昵称 + entity.getData().getId(),
                            TextUtils.isEmpty(entity.getData().getContent()) ? entity.getData().getNikename() : entity.getData().getContent());
                    //存储对方头像
                    SaveUtils.setString(Save_Key.S_头像 + entity.getData().getId(), entity.getData().getAvatar());
                }

                if (activity instanceof Main_Activity) {
                    MyApp.mybean = entity.getData();
                    ((Main_Activity) activity).CallBack_MyInfo();
                }

                if (activity instanceof ChatActivity) {
                    ((ChatActivity) activity).Callback_UserInfo(entity.getData());
                }
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
                if (viewHolder != null) {
                    viewHolder.tvName.setText(entity.getData().getNikename());
                    PicassoUtlis.img(entity.getData().getAvatar(), viewHolder.avatar);
                }
                if (myDialog != null) {
                    myDialog.dismiss();
                    UserInfo_Activity.start(MyApp.currentActivity(), entity.getData());
                }

                if (activity instanceof UserInfo_Activity) {
                    ((UserInfo_Activity) activity).CallBack_UserInfo(entity.getData());
                }

                if (activity instanceof FriendInfo_Activity) {
                    ((FriendInfo_Activity) activity).CallBack(entity.getData());
                }
                break;

            case 400:
                if (entity.getMessage().equals("无数据")) {
                    if (myDialog != null) {
                        myDialog.CallBack();
                    }
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

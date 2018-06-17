package com.sy.bottle.model;


import com.sy.bottle.utils.LogUtil;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;
import com.tencent.imsdk.ext.sns.TIMFutureFriendType;

/**
 * 好友关系链消息的界面绑定数据
 * 可用于本地操作后界面修改
 */
public class FriendFuture {
    private static final String TAG = "FriendFuture";

    TIMFriendFutureItem futureItem;

    private TIMFutureFriendType type;

    public FriendFuture(TIMFriendFutureItem item){
        futureItem = item;
        type = futureItem.getType();
    }


    public TIMFutureFriendType getType() {
        return type;
    }

    public void setType(TIMFutureFriendType type) {
        this.type = type;
    }

    public String getName(){
        return futureItem.getProfile().getNickName().equals("") ? futureItem.getIdentifier() : futureItem.getProfile().getNickName();
    }

    public String getFaceUrl(){
        LogUtil.e(TAG,futureItem.getProfile().getFaceUrl());
        return futureItem.getProfile().getFaceUrl().equals("") ? null : futureItem.getProfile().getFaceUrl();
    }

    public String getMessage(){
        return futureItem.getAddWording();
    }


    public String getIdentify(){
        return futureItem.getIdentifier();
    }



}

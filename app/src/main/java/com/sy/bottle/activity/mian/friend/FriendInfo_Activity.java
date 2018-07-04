package com.sy.bottle.activity.mian.friend;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.activity.mian.other.Report_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReName_Dialog;
import com.sy.bottle.dialog.ShowImage_Dialog;
import com.sy.bottle.entity.Banner_Entity;
import com.sy.bottle.entity.Photos_Entity;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.servlet.Black_Is_Servlet;
import com.sy.bottle.servlet.Black_Out_Servlet;
import com.sy.bottle.servlet.Black_Set_Servlet;
import com.sy.bottle.servlet.Friend_Del_Servlet;
import com.sy.bottle.servlet.Photos_Get_Servlet;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.CircleImageView;
import com.sy.bottle.view.ImageCycleView;
import com.sy.bottle.view.LineControllerView;
import com.tencent.imsdk.TIMConversationType;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:好友资料
 */

public class FriendInfo_Activity extends Base_Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "Profile_Activity";
    private static final String IDentify = "identify";

    private String identify;

    ImageView sex;
    TextView name, sign;
    LineControllerView id, remark, category, black, address;
    ImageCycleView photos;
    CircleImageView head;

    static Activity activity;

    boolean isrun = true;

    public static void start(Activity context, String identify) {
        Intent intent = new Intent(context, FriendInfo_Activity.class);
        intent.putExtra(IDentify, identify);
        activity = context;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendinfo);

        setTitle("详细资料");
        setMenu("投诉");
        setBack(true);

        initview();

        identify = getIntent().getStringExtra(IDentify);

        //获取用户信息
        new UserInfo_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, identify);

        //获取照片墙
        new Photos_Get_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, identify);

        //查询是否是黑名单
        new Black_Is_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, identify);
    }

    private void initview() {
        photos = findViewById(R.id.user_info_photos);
        head = findViewById(R.id.user_info_avatar);
        name = findViewById(R.id.user_info_name);
        sign = findViewById(R.id.user_info_sign);
        id = findViewById(R.id.user_info_id);
        remark = findViewById(R.id.user_info_remark);
        category = findViewById(R.id.user_info_group);
        black = findViewById(R.id.user_info_blackList);
        address = findViewById(R.id.user_info_address);
        sex = findViewById(R.id.user_info_sex);

        remark.setOnClickListener(this);

        if (activity instanceof Main_Activity || activity instanceof ChatActivity) {

            address.setCanNav(false);
        }

    }

    /**
     * 用户信息
     *
     * @param bean
     */
    public void CallBack(UserInfo_Entity.DataBean bean) {

        PicassoUtlis.img(bean.getAvatar(), head);
        name.setText(bean.getNickname());
        sex.setImageResource(bean.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);
        remark.setContent(bean.getContent());
        sign.setText(bean.getSign());
        address.setContent(bean.getProvince() + "-" + bean.getCity() + "-" + bean.getArea());

    }

    /**
     * 是否是黑名单
     *
     * @param isblack
     */
    public void CallBack_IsBlack(boolean isblack) {
        black.setSwitch(isblack);
        black.setCheckListener(this);
    }

    List<Banner_Entity.DBean> dBeans = new ArrayList<>();

    /**
     * 照片返回
     *
     * @param dataBeans
     */
    public void CallBack_Photos(List<Photos_Entity.DataBean> dataBeans) {
        dBeans.clear();
        for (Photos_Entity.DataBean bean : dataBeans) {
            Banner_Entity.DBean dBean = new Banner_Entity.DBean();
            dBean.setPicUrl(bean.getPic_url());
            dBean.setId(bean.getId());
            dBeans.add(dBean);
        }

        photos.setBeans(dBeans, new ImageCycleView.Listener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                PicassoUtlis.img(imageURL, imageView);
            }

            @Override
            public void onImageClick(Banner_Entity.DBean bean, View imageView) {
                new ShowImage_Dialog(FriendInfo_Activity.this, bean.getPicUrl()).show();
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                Report_Activity.start(this, identify);
                break;
            case R.id.user_info_remark:
                new ReName_Dialog(this, identify, remark.getContent().toString(), remark);
                break;
            case R.id.user_info_btnChat:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("identify", identify);
                intent.putExtra("type", TIMConversationType.C2C);
                startActivity(intent);
                MyApp.finishActivity();
                break;
            case R.id.user_info_btnDel:
                Loading.show(this, "删除中");
                new Friend_Del_Servlet(this).execute(identify);
//                friendshipManagerPresenter.delFriend(identify);
                break;
            case R.id.group:
//                final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
//                for (int i = 0; i < groups.length; ++i) {
//                    if (groups[i].equals("")) {
//                        groups[i] = getString(R.string.default_group_name);
//                        break;
//                    }
//                }
//                new ListPickerDialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (TextUtils.isEmpty(categoryStr)) return;
//                        if (groups[which].equals(categoryStr)) return;
//                        friendshipManagerPresenter.changeFriendGroup(identify,
//                                categoryStr.equals(getString(R.string.default_group_name)) ? null : categoryStr,
//                                groups[which].equals(getString(R.string.default_group_name)) ? null : groups[which]);
//                    }
//                });
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (isrun) {
            if (b) {
                Base_Dialog base_dialog = new Base_Dialog(this);
                base_dialog.setMessage("好友将解除，并进入您的黑名单中");
                base_dialog.setOk("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Black_Set_Servlet(FriendInfo_Activity.this).execute(identify);
                    }
                });
                base_dialog.setEsc("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isrun = false;
                        black.setSwitch(false);
                    }
                });
            } else {
                Base_Dialog base_dialog = new Base_Dialog(this);
                base_dialog.setMessage("确定要从黑名单中解除吗？");
                base_dialog.setOk("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new Black_Out_Servlet(FriendInfo_Activity.this).execute(identify);
                    }
                });
                base_dialog.setEsc("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isrun = false;
                        black.setSwitch(true);
                    }
                });
            }
        } else {
            isrun = true;
        }
    }
}

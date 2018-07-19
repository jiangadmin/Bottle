package com.sy.bottle.activity.mian.friend;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.activity.mian.other.Report_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.servlet.Black_Is_Servlet;
import com.sy.bottle.servlet.Black_Out_Servlet;
import com.sy.bottle.servlet.Black_Set_Servlet;
import com.sy.bottle.servlet.Friend_Add_Servlet;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.LineControllerView;
import com.tencent.imsdk.TIMConversationType;

/**
 * @author: jiangadmin
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 好友资料
 */
public class UserInfo_Activity extends Base_Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "UserInfo_Activity";

    ImageView head, sex;

    TextView name, sign;
    EditText remark;

    LineControllerView address, black;

    static String id;
    static UserInfo_Entity.DataBean entity;
    Button btnAdd, chat;

    boolean isrun = true;

    public static void start(Context context, UserInfo_Entity.DataBean userInfo_entity) {
        entity = userInfo_entity;
        id = entity.getId();
        start(context);
    }

    public static void start(Context context, String userid) {
        id = userid;
        start(context);
    }

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UserInfo_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        setBack(true);
        setTitle("个人信息");

        head = findViewById(R.id.avatar);
        sex = findViewById(R.id.sex);
        name = findViewById(R.id.name);
        remark = findViewById(R.id.remark);
        sign = findViewById(R.id.sign);
        address = findViewById(R.id.address);
        black = findViewById(R.id.user_info_blackList);

        chat = findViewById(R.id.add_friend_chat_btn);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);
        chat.setOnClickListener(this);

        if (entity != null) {
            Glide.with(this).load(entity.getAvatar()).apply(new RequestOptions().placeholder(R.drawable.head_other)).into(head);
            sex.setImageResource(entity.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);
            name.setText(entity.getNickname());
            sign.setText(entity.getSign());
            address.setContent(entity.getProvince() + "-" + entity.getCity() + "-" + entity.getArea());

        }

        if (id != null) {

            //获取用户信息
            new UserInfo_Servlet(this).execute(id);
            setMenu("举报");

            //查询是否是黑名单
            new Black_Is_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
        }
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

    /**
     * 查询数据返回
     */
    public void CallBack() {
        btnAdd.setVisibility(View.GONE);
    }

    /**
     * 好友数据返回
     *
     * @param entity
     */
    public void CallBack_UserInfo(UserInfo_Entity.DataBean entity) {
        this.entity = entity;
        Glide.with(this).load(entity.getAvatar()).apply(new RequestOptions().placeholder(R.drawable.head_other)).into(head);
        sex.setImageResource(entity.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);
        name.setText(entity.getNickname());
        sign.setText(entity.getSign());
        address.setContent(entity.getProvince() + "-" + entity.getCity() + "-" + entity.getArea());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.menu:
                Report_Activity.start(this, entity.getId());
                break;
            case R.id.btnAdd:
                //添加好友
                if (!TextUtils.isEmpty(remark.getText().toString())) {
                    new Friend_Add_Servlet(this).execute(entity.getId(), remark.getText().toString());
                } else {
                    new Friend_Add_Servlet(this).execute(entity.getId());
                }

                break;

            case R.id.add_friend_chat_btn:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("identify", entity.getId());
                intent.putExtra("type", TIMConversationType.C2C);
                startActivity(intent);
                MyApp.finishActivity();
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (isrun) {
            if (b) {
                Base_Dialog base_dialog = new Base_Dialog(this);
                base_dialog.setMessage("确定添加到您的黑名单中吗？");
                base_dialog.setOk("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Black_Set_Servlet(UserInfo_Activity.this).execute(id);
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

                        new Black_Out_Servlet(UserInfo_Activity.this).execute(id);
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

package com.sy.bottle.activity.mian.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.activity.mian.other.Report_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.UserInfo_Entity;
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
public class UserInfo_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "UserInfo_Activity";

    ImageView head, sex;

    TextView name, sign;
    EditText remark;

    LineControllerView address;

    static String id;
    static UserInfo_Entity.DataBean entity;
    Button btnAdd, chat;

    public static void start(Context context, UserInfo_Entity.DataBean userInfo_entity) {
        entity = userInfo_entity;
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

        chat = findViewById(R.id.add_friend_chat_btn);
        btnAdd = findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(this);
        chat.setOnClickListener(this);

        if (entity != null) {
            PicassoUtlis.img(entity.getAvatar(), head);
            sex.setImageResource(entity.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);
            name.setText(entity.getNikename());
            sign.setText(entity.getSign());
            address.setContent(entity.getProvince() + "-" + entity.getCity() + "-" + entity.getArea());

        }

        if (id != null) {

            //获取用户信息
            new UserInfo_Servlet(this).execute(id);
            setMenu("举报");
        }
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
        PicassoUtlis.img(entity.getAvatar(), head);
        sex.setImageResource(entity.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);
        name.setText(entity.getNikename());
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
}

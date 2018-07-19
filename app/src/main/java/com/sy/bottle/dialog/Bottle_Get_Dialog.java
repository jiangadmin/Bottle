package com.sy.bottle.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sy.bottle.R;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.entity.Bottle_Get_Entity;
import com.sy.bottle.servlet.Bottle_CallBack_Servlet;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.CircleImageView;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMConversationType;

/**
 * @author: jiangyao
 * @date: 2018/6/8
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 捞瓶子
 */
public class Bottle_Get_Dialog extends MyDialog implements View.OnClickListener {
    private static final String TAG = "Bottle_Get_Dialog";

    Context context;

    public Bottle_Get_Dialog(@NonNull Context context) {
        super(context, R.style.myDialogTheme);
        this.context = context;
        show();
    }

    TextView name, address, message;

    Button esc;

    Button submit;

    CircleImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_bottle_get);

        esc = findViewById(R.id.dialog_bottle_get_esc);
        avatar = findViewById(R.id.dialog_bottle_get_head);
        name = findViewById(R.id.dialog_bottle_get_name);
        address = findViewById(R.id.dialog_bottle_get_address);
        message = findViewById(R.id.dialog_bottle_get_content);
        submit = findViewById(R.id.dialog_bottle_get_submit);

        esc.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    Bottle_Get_Entity.DataBean bean;

    public void init(Bottle_Get_Entity.DataBean bean) {
        this.bean = bean;
        Glide.with(context).load(bean.getAvatar()).into(avatar);
        name.setText(bean.getNickname());
        address.setText("来自 " + bean.getCity());
        message.setText(bean.getContent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_bottle_get_submit:

                new Bottle_CallBack_Servlet().execute(String.valueOf(bean.getId()));

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("identify", bean.getUser_id());
                intent.putExtra("type", TIMConversationType.C2C);
                context.startActivity(intent);

                break;

            case R.id.dialog_bottle_get_esc:
                TabToast.makeText("您的瓶子已扔回大海");
                break;
        }
        dismiss();
    }
}

package com.sy.bottle.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.entity.Bottle_Get_Entity;
import com.sy.bottle.servlet.Bottle_CallBack_Servlet;
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

    TextView id, message;

    ImageView esc;

    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_bottle_get);


        id = findViewById(R.id.dialog_bottle_get_id);
        message = findViewById(R.id.dialog_bottle_get_content);
        esc = findViewById(R.id.dialog_bottle_get_esc);
        submit = findViewById(R.id.dialog_bottle_get_submit);


        esc.setOnClickListener(this);
        submit.setOnClickListener(this);

    }

    Bottle_Get_Entity.DataBean bean;

    public void init(Bottle_Get_Entity.DataBean bean) {
        this.bean = bean;
        id.setText("来自 ID:" + bean.getUser_id() );

        message.setText(bean.getContent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.dialog_bottle_get_submit:

               new  Bottle_CallBack_Servlet().execute(bean.getId());

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("identify", bean.getUser_id());
                intent.putExtra("type", TIMConversationType.C2C);
                context.startActivity(intent);

                break;
        }
        dismiss();
    }
}

package com.sy.bottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.servlet.Bottle_Random_Servlet;
import com.sy.bottle.servlet.Bottle_Set_Servlet;
import com.sy.bottle.view.TabToast;

/**
 * @author: jiangyao
 * @date: 2018/6/3
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 扔瓶子
 */
public class Bottle_Set_Dailog extends MyDialog implements View.OnClickListener {
    private static final String TAG = "Bottle_Set_Dailog";

    Button esc;

    EditText message;

    TextView random;

    Button submit;

    public Bottle_Set_Dailog(@NonNull Context context) {

        super(context, R.style.myDialogTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_bottle_set);

        initview();

    }

    private void initview() {
        esc = findViewById(R.id.dialog_bottle_set_esc);
        message = findViewById(R.id.dialog_bottle_set_message);
        random = findViewById(R.id.dialog_bottle_set_random);
        submit = findViewById(R.id.dialog_bottle_set_submit);

        random.setOnClickListener(this);
        submit.setOnClickListener(this);
        esc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_bottle_set_esc:
                dismiss();
                break;
            case R.id.dialog_bottle_set_random:
                new Bottle_Random_Servlet(message).execute();
                break;
            case R.id.dialog_bottle_set_submit:
                if (TextUtils.isEmpty(message.getText().toString())) {
                    TabToast.makeText("还是说点什么吧");
                    return;
                }

                new Bottle_Set_Servlet(this).execute("1", message.getText().toString());

                break;
        }

    }
}

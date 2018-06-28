package com.sy.bottle.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.servlet.Friend_Update_Servlet;
import com.sy.bottle.view.LineControllerView;

/**
 * @author: jiangyao
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 修改备注名
 */
public class ReName_Dialog extends MyDialog implements View.OnClickListener {
    private static final String TAG = "ReName_Dialog";

    Activity activity;

    EditText rename;
    Button submit;
    String id, name;
    TextView dialog_message;
    LineControllerView lineControllerView;

    public ReName_Dialog(@NonNull Activity activity, String id, String name, LineControllerView view) {
        super(activity, R.style.myDialogTheme);
        this.activity = activity;
        this.id = id;
        this.name = name;
        this.lineControllerView = view;
        setCanceledOnTouchOutside(true);
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rename);

        initview();
    }

    private void initview() {
        rename = findViewById(R.id.rename);
        submit = findViewById(R.id.submit);
        dialog_message = findViewById(R.id.dialog_message);

        rename.setText(name);

        rename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dialog_message.setText("");
                dialog_message.setVisibility(View.GONE);
            }
        });

        submit.setOnClickListener(this);
    }

    public void message(String m) {
        dialog_message.setVisibility(View.VISIBLE);
        dialog_message.setText(m);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit:
                String name = rename.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    name = "";
                }
                lineControllerView.setContent(name);
                Loading.show(activity, "修改中");
                new Friend_Update_Servlet(activity, this).execute(id, name);
                break;
        }
    }
}

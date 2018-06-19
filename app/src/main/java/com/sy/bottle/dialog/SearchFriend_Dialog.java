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
import com.sy.bottle.servlet.UserInfo_Servlet;

/**
 * @author: jiangyao
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 查找好友
 */
public class SearchFriend_Dialog extends MyDialog implements View.OnClickListener, TextWatcher {
    private static final String TAG = "SearchFriend_Dialog";

    Activity activity;

    EditText input;
    TextView message;
    Button search;

    public SearchFriend_Dialog(@NonNull Activity activity) {
        super(activity, R.style.myDialogTheme);
        this.activity = activity;
        setCanceledOnTouchOutside(true);
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_searchfriend);

        initview();
    }

    private void initview() {
        input = findViewById(R.id.inputSearch);
        message = findViewById(R.id.dialog_message);
        search = findViewById(R.id.dialog_ok);

        search.setOnClickListener(this);
        input.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {

        String id = input.getText().toString();
        switch (view.getId()) {
            case R.id.dialog_ok:
                if (TextUtils.isEmpty(id)) {
                    message.setVisibility(View.VISIBLE);
                    message.setText("请输入要查找的账号");
                    return;
                }

                new UserInfo_Servlet(this).execute(id);
                break;
        }
    }

    public void CallBack() {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (message.getVisibility() == View.VISIBLE) {
            message.setVisibility(View.GONE);
        }
    }
}

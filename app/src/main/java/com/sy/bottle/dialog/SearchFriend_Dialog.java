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
import com.sy.bottle.activity.mian.friend.FriendInfo_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.SaveUtils;

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
                //非空验证
                if (TextUtils.isEmpty(id)) {
                    message.setVisibility(View.VISIBLE);
                    message.setText("请输入要查找的账号");
                    return;
                }

                //判断是否是自己
                if (SaveUtils.getString(Save_Key.UID).equals(id)) {
                    message.setVisibility(View.VISIBLE);
                    message.setText("不能添加自己为好友");
                    input.setText("");
                    return;
                }

                //判断是否是好友
                for (Friends_Entity.DataBean bean : MyApp.friendsbeans) {
                    //判断是否是好友
                    if (bean.getFriend_id().equals(id)) {
                        FriendInfo_Activity.start(activity, id);
                        dismiss();
                        return;
                    }
                }

                new UserInfo_Servlet(this).execute(id);
                break;
        }
    }

    public void CallBack() {
        message.setVisibility(View.VISIBLE);
        message.setText("查无此用户");
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

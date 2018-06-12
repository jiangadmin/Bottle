package com.sy.bottle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.friend.SearchFriend_Activity;

/**
 * @author: jiangyao
 * @date: 2018/5/25
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Move_Dialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "Move_Dialog";

    Button dismiss, add_friend, add_group, manager_group;

    Context con;

    public Move_Dialog(@NonNull Context context) {
        super(context, R.style.dialog);
        con = context;

        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);

        setContentView(R.layout.dialog_more);

        initview();
    }

    private void initview() {
        dismiss = findViewById(R.id.more_dismiss);
        add_friend = findViewById(R.id.add_friend);
        add_group = findViewById(R.id.add_group);
        manager_group = findViewById(R.id.manager_group);

        dismiss.setOnClickListener(this);
        add_friend.setOnClickListener(this);
        add_group.setOnClickListener(this);
        manager_group.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.more_dismiss:
                dismiss();
                break;
            case R.id.add_friend:
                SearchFriend_Activity.start(con);
                break;
            case R.id.add_group:
//                SearchGroupActivity.start(con);
                break;
            case R.id.manager_group:
//                ManageFriendGroupActivity.start(con);
                break;
        }

        dismiss();
    }
}

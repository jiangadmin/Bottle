package com.sy.bottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.sy.bottle.R;
import com.sy.bottle.presenter.FriendshipManagerPresenter;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendAllowType;

/**
 * @author: jiangyao
 * @date: 2018/5/23
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 好友申请
 */
public class FriendShip_Dialog extends MyDialog implements View.OnClickListener {
    private static final String TAG = "FriendShip_Dialog";

    Button button0, button1, button2;

    public FriendShip_Dialog(@NonNull Context context) {
        super(context, R.style.myDialogTheme);
        try {
            setCanceledOnTouchOutside(true);
            show();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_friendship);

        initview();
    }

    private void initview() {
        button0 = findViewById(R.id.friendship_0);
        button1 = findViewById(R.id.friendship_1);
        button2 = findViewById(R.id.friendship_2);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //需要验
            case R.id.friendship_0:
                TIMFriendAllowType(TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM);
                break;
            //拒接任何人添加
            case R.id.friendship_1:
                TIMFriendAllowType(TIMFriendAllowType.TIM_FRIEND_DENY_ANY);
                break;
            //允许任何人添加
            case R.id.friendship_2:
                TIMFriendAllowType(TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY);
                break;
        }

        dismiss();
    }

    public void TIMFriendAllowType(TIMFriendAllowType type) {
        FriendshipManagerPresenter.setFriendAllowType(type, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                TabToast.makeText("修改验证类型失败");
            }

            @Override
            public void onSuccess() {
                TabToast.makeText("修改成功");
            }
        });
    }
}

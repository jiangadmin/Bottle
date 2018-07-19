package com.sy.bottle.servlet;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.sy.bottle.utils.LogUtil;
import com.tencent.imsdk.TIMManager;

/**
 * @author: jiangyao
 * @date: 2018/7/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 模拟心跳
 */
public class Timeing_Servlet extends Service {
    private static final String TAG = "Timeing_Servlet";



    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {

        CountDownTimer count = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                TIMManager.getInstance().getLoginUser();
                LogUtil.e(TAG, "跳一下");

                onBind(intent);

            }
        };

        count.start();
        return null;
    }
}

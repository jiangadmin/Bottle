package com.sy.bottle.model;

import android.view.View;

import com.sy.bottle.utils.LogUtil;

import java.util.Calendar;

public abstract class NoDoubleListener implements View.OnClickListener {
    private static final String TAG = "NoDoubleListener";
    public static final int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        } else {
            LogUtil.e(TAG, "点击过快");
        }
    }

    protected abstract void onNoDoubleClick(View v);
}

package com.sy.bottle.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sy.bottle.app.MyApp;

/**
 * Created by 垚垚
 * on 15/7/23.
 * Email: www.fangmu@qq.com
 * Phone：18661201018
 * Purpose: 优化提示框
 */

public class TabToast {

    /**
     * Toast字体大小
     */
    static float DEFAULT_TEXT_SIZE = 14;
    /**
     * Toast字体颜色
     */
    static int DEFAULT_TEXT_COLOR = 0xffffffff;
    /**
     * Toast背景颜色
     */
    static int DEFAULT_BG_COLOR = 0x5a000000;
    /**
     * Toast的高度(单位dp)
     */
    static final float DEFAULT_TOAST_HEIGHT = 50.0f;

    static Context mContext;
    volatile static TabToast mInstance;
    static Toast mToast;
    static View layout;
    static TextView tv;


    public TabToast(Context context) {
        mContext = context;
    }

    /**
     * 单例模式
     *
     * @param context 传入的上下文
     * @return TabToast实例
     */
    private static TabToast getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TabToast.class) {
                if (mInstance == null) {
                    mInstance = new TabToast(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * @param duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    private static void getToast(int duration) {
        if (mToast == null) {
            mToast = new Toast(mContext);
            mToast.setGravity(Gravity.TOP, 0, 0);
            mToast.setDuration(duration == Toast.LENGTH_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        }
    }

    public static void makeText(Context context, String text) {
        if (context != null) {
            if (!TextUtils.isEmpty(text)) {
                if (text.length() > 40) {
                    return;
                }
                if (text.length() > 20) {
                    text = text.substring(0, 20) + "\n" + text.substring(20, text.length());
                }
                makeText(context, text, Toast.LENGTH_SHORT);
            }
        }
    }

    public static void makeText(String text) {
        if (MyApp.getInstance() != null) {
            if (!TextUtils.isEmpty(text)) {
                if (text.length() > 40) {
                    return;
                }
                if (text.length() > 20) {
                    text = text.substring(0, 20) + "\n" + text.substring(20, text.length());
                }
                makeText(MyApp.getInstance(), text, Toast.LENGTH_LONG);
            }
        }
    }

    public static void makeText(Context context, String text, int duration) {
        if (context == null) {
            return;
        }
        getInstance(context);
        getToast(duration);
        if (mInstance.layout == null || mInstance.tv == null) {
            LinearLayout container = new LinearLayout(mContext);
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            container.setLayoutParams(rootParams);
            container.setBackgroundColor(DEFAULT_BG_COLOR);
            container.setGravity(Gravity.CENTER);

            mInstance.tv = new TextView(mContext);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(getScreenWidth(mContext), dp2px(DEFAULT_TOAST_HEIGHT));
            mInstance.tv.setLayoutParams(tvParams);
            mInstance.tv.setPadding(dp2px(8), dp2px(2), dp2px(8), dp2px(2));
            mInstance.tv.setGravity(Gravity.CENTER);
            mInstance.tv.setTextColor(DEFAULT_TEXT_COLOR);
            mInstance.tv.setMaxLines(2);
//            mInstance.tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            mInstance.tv.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            mInstance.tv.setBackgroundColor(DEFAULT_BG_COLOR);
            mInstance.tv.setTextSize(DEFAULT_TEXT_SIZE);

            container.addView(mInstance.tv);

            mInstance.layout = container;

            mToast.setView(mInstance.layout);
        }
        mInstance.tv.setText(text);
        mToast.show();
    }

    /**
     * dp转px
     *
     * @param value dp
     * @return px
     */
    public static int dp2px(float value) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context Context
     * @return px
     */
    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        float density = outMetrics.density;
        return (int) (outMetrics.widthPixels * density);
    }


}

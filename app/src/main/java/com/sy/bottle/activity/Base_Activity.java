package com.sy.bottle.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.ToolUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: jiangyao
 * @date: 2018/5/9
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 公共
 */
public class Base_Activity extends FragmentActivity {
    private static final String TAG = "Base_Activity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_title_bar);

        MyApp.addActivity(this);

        //把状态栏设置为透明
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            //透明状态栏
//            window.setStatusBarColor(Color.TRANSPARENT);
//            //透明导航栏
////            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
    }

    /**
     * ToolBar样式
     *
     * @param type 0 白  1 左右渐变变 2 上下渐变
     */
    public void ToolBarStyle(int type) {
        switch (type) {
            //白色
            case 0:
                findViewById(R.id.title_bar_tob).setBackgroundColor(getResources().getColor(R.color.white));
                ((TextView) findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.gray_3));
                ((ImageButton) findViewById(R.id.back)).setImageResource(R.drawable.ic_back_g);
                ((TextView) findViewById(R.id.menu)).setTextColor(getResources().getColor(R.color.gray_3));
                setDarkStatusIcon(true);

                break;
            //红色
            case 1:
                findViewById(R.id.title_bar_tob).setBackgroundResource(R.color.style_color);
                ((TextView) findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.white));
                ((ImageButton) findViewById(R.id.back)).setImageResource(R.drawable.ic_back);
                ((TextView) findViewById(R.id.menu)).setTextColor(getResources().getColor(R.color.white));
                setDarkStatusIcon(true);
                break;
        }
    }

    /**
     * 状态栏 亮暗
     *
     * @param bDark
     */
    public void setDarkStatusIcon(boolean bDark) {

        //如果是Android 6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
        String deviceMan = Build.MANUFACTURER;
        if (deviceMan.equals("Xiaomi")) {
            setMiuiStatusBarDarkMode(this, bDark);

        } else if (deviceMan.equals("meizu")) {
            setMeizuStatusBarDarkIcon(this, bDark);
        }

    }

    /**
     * MIUI 系统
     *
     * @param activity
     * @param darkmode 字体颜色为黑色。
     * @return
     */
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Flyme 系统
     *
     * @param activity
     * @param dark     字体颜色
     * @return
     */
    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
        return result;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
//        RelativeLayout titlebar = findViewById(R.id.title_bar_tob);
//
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
//        //获取状态栏高度 加上 要设置的标题栏高度 等于 标题栏实际高度
//        layoutParams.height = ToolUtils.getStatusHeight() + ToolUtils.dp2px(43);
//        titlebar.setLayoutParams(layoutParams);


        TextView tv = findViewById(R.id.title);

        tv.setText(title);
    }


    /**
     * 设置标题
     *
     * @param title
     */
    public void setRTitle(String title) {
//        RelativeLayout titlebar = findViewById(R.id.title_bar_tob);
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titlebar.getLayoutParams();
//        //获取状态栏高度 加上 要设置的标题栏高度 等于 标题栏实际高度
//        layoutParams.height = ToolUtils.getStatusHeight() + ToolUtils.dp2px(48);
//        titlebar.setLayoutParams(layoutParams);

        TextView tv = findViewById(R.id.title);

        tv.setText(title);
    }


    /**
     * 是否有返回键
     *
     * @param isvisible
     */
    public void setBack(boolean isvisible) {

//        if (RightOut && !SaveUtils.getBoolean(Save_Key.S_关闭手势)) {
//            new SlidingLayout(this).bindActivity(this);
//        }

        ImageButton backBtn = findViewById(R.id.back);
        if (isvisible) {
            backBtn.setVisibility(View.VISIBLE);
        } else {
            backBtn.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public static TextView MENU;

    /**
     * 设置菜单
     *
     * @param objects 图片或者文字/文字或者颜色/颜色
     */
    public void setMenu(Object... objects) {
        MENU = findViewById(R.id.menu);

        if (objects.length > 0) {
            MENU.setVisibility(View.VISIBLE);
            MENU.setOnClickListener((View.OnClickListener) this);
            //判断是不是图
            if (objects[0] instanceof Integer) {

                Drawable drawableleft = getResources().getDrawable((Integer) objects[0]);
                drawableleft.setBounds(0, 0, drawableleft.getIntrinsicWidth(), drawableleft.getMinimumHeight());
                MENU.setCompoundDrawables(drawableleft, null, null, null);//只放左边

                //判断是不是字
                if (objects.length > 1 && objects[1] instanceof String) {

                    MENU.setText((String) objects[1]);

                    //判断是不是颜色
                    if (objects.length > 2 && objects[2] instanceof Integer) {

                        MENU.setTextColor(getResources().getColor((Integer) objects[2]));

                    }
                }
            }

            //判断是不是字
            if (objects[0] instanceof String) {

                MENU.setText((String) objects[0]);
                //判断是不是颜色
                if (objects.length > 1 && objects[1] instanceof Integer) {
                    MENU.setTextColor(getResources().getColor((Integer) objects[1]));
                }
            }
        } else {
            MENU.setVisibility(View.GONE);
        }
    }
}

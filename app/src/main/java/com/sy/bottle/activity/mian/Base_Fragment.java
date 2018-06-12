package com.sy.bottle.activity.mian;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.utils.ToolUtils;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 公共
 */
public class Base_Fragment extends Fragment {

    /**
     * 设置标题
     *
     * @param view
     * @param title
     */
    public void setTitle(View view, String title) {
//        RelativeLayout titlebar = view.findViewById(R.id.title_bar_tob);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titlebar.getLayoutParams();
//        //获取状态栏高度 加上 要设置的标题栏高度 等于 标题栏实际高度
//        layoutParams.height = ToolUtils.getStatusHeight() + ToolUtils.dp2px(45);
//        titlebar.setLayoutParams(layoutParams);

        TextView tv = view.findViewById(R.id.title);

        tv.setText(title);
    }


    public static TextView MENU;

    /**
     * 设置菜单
     *
     * @param objects 图片或者文字/文字或者颜色/颜色
     */
    public void setMenu(View view, Object... objects) {
        MENU = view.findViewById(R.id.menu);
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

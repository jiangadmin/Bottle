package com.sy.bottle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sy.bottle.R;

/**
 * 设置等页面条状控制或显示信息的控件
 */
public class LineControllerView extends LinearLayout {

    private String name;
    private boolean isBottom;
    private String substance;
    private boolean canNav;
    private boolean isSwitch;

    public LineControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_line_controller, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineControllerView, 0, 0);
        try {
            name = ta.getString(R.styleable.LineControllerView_name);
            substance = ta.getString(R.styleable.LineControllerView_substance);
            isBottom = ta.getBoolean(R.styleable.LineControllerView_isBottom, false);
            canNav = ta.getBoolean(R.styleable.LineControllerView_canNav, false);
            isSwitch = ta.getBoolean(R.styleable.LineControllerView_isSwitch, false);
            setUpView();
        } finally {
            ta.recycle();
        }
    }

    private void setUpView() {
        TextView tvName = findViewById(R.id.name);
        tvName.setText(name);
        TextView tvContent = findViewById(R.id.content);
        tvContent.setText(substance);
        View bottomLine = findViewById(R.id.bottomLine);
        bottomLine.setVisibility(isBottom ? VISIBLE : GONE);
        ImageView navArrow = findViewById(R.id.rightArrow);
        navArrow.setVisibility(canNav ? VISIBLE : GONE);
        LinearLayout contentPanel = findViewById(R.id.contentText);
        contentPanel.setVisibility(isSwitch ? GONE : VISIBLE);
        Switch switchPanel = findViewById(R.id.btnSwitch);
        switchPanel.setVisibility(isSwitch ? VISIBLE : GONE);

    }

    /**
     * 设置文字内容
     *
     * @param substance 内容
     */
    public void setContent(String substance) {
        this.substance = substance;
        TextView tvContent = findViewById(R.id.content);
        tvContent.setText(substance);
    }


    /**
     * 获取内容
     */
    public String getContent() {
        TextView tvContent = findViewById(R.id.content);
        return tvContent.getText().toString();
    }


    /**
     * 设置是否可以跳转
     *
     * @param canNav 是否可以跳转
     */
    public void setCanNav(boolean canNav) {
        this.canNav = canNav;
        ImageView navArrow = findViewById(R.id.rightArrow);
        navArrow.setVisibility(canNav ? VISIBLE : GONE);
    }


    /**
     * 设置开关状态
     *
     * @param on 开关
     */
    public void setSwitch(boolean on) {
        Switch mSwitch = findViewById(R.id.btnSwitch);
        mSwitch.setChecked(on);
    }

    /**
     * 设置开关监听
     *
     * @param listener 监听
     */
    public void setCheckListener(CompoundButton.OnCheckedChangeListener listener) {
        Switch mSwitch = findViewById(R.id.btnSwitch);
        mSwitch.setOnCheckedChangeListener(listener);
    }


}

package com.sy.bottle.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sy.bottle.R;

/**
 * 设置等页面条状控制或显示信息的控件
 */
public class LinearEditLayout extends LinearLayout {

    private String title;
    private String message;
    private boolean icin;
    private boolean isEdit;

    public LinearEditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_line_edit, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineEditView, 0, 0);
        try {
            title = ta.getString(R.styleable.LineEditView_title);
            message = ta.getString(R.styleable.LineEditView_message);
            icin = ta.getBoolean(R.styleable.LineEditView_icin, false);
            isEdit = ta.getBoolean(R.styleable.LineEditView_isEdit, false);
            setUpView();
        } finally {
            ta.recycle();
        }
    }

    private void setUpView() {
        TextView tvName = findViewById(R.id.name);
        tvName.setText(title);

        EditText tvContent = findViewById(R.id.content);
        tvContent.setText(message);
        tvContent.setEnabled(isEdit);

        ImageView navArrow = findViewById(R.id.rightArrow);
        navArrow.setVisibility(icin ? VISIBLE : GONE);

    }

    /**
     * 设置文字内容
     *
     * @param substance 内容
     */
    public void setContent(String substance) {
        this.message = substance;
        EditText tvContent = findViewById(R.id.content);
        tvContent.setText(substance);
    }




    /**
     * 获取内容
     */
    public String getContent() {
        EditText tvContent = findViewById(R.id.content);
        return tvContent.getText().toString();
    }

  /**
     *
     */
    public void setEdit(boolean isEdit) {
        EditText tvContent = findViewById(R.id.content);
        tvContent.setEnabled(isEdit);
    }


    /**
     * 设置是否可以跳转
     *
     * @param icin 是否可以跳转
     */
    public void setCanNav(boolean icin) {
        this.icin = icin;
        ImageView navArrow = findViewById(R.id.rightArrow);
        navArrow.setVisibility(icin ? VISIBLE : GONE);
    }

}

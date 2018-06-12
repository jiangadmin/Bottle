package com.sy.bottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.utils.LogUtil;

/**
 * @author: jiangyao
 * @date: 2017/11/30.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 弹框
 */

public class Base_Dialog extends MyDialog {
    private static final String TAG = "Base_Dialog";

    TextView dialog_title, dialog_message;
    Button dialog_esc, dialog_ok;
    LinearLayout bottom;
    ImageView dialog_icon;

    public Base_Dialog(@NonNull Context context) {
        super(context, R.style.myDialogTheme);
        try {
            show();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_base);

        initview();

    }

    private void initview() {
        dialog_icon = findViewById(R.id.dialog_icon);
        dialog_title = findViewById(R.id.dialog_title);
        dialog_message = findViewById(R.id.dialog_message);
        dialog_esc = findViewById(R.id.dialog_esc);
        dialog_ok = findViewById(R.id.dialog_ok);
        bottom = findViewById(R.id.dialog_bottom);


    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    public void setIcon(int i) {
        dialog_icon.setVisibility(View.VISIBLE);
        dialog_icon.setImageResource(i);
    }

    public void setTitle(String t) {

        dialog_title.setVisibility(View.VISIBLE);
        dialog_title.setText(t);
    }

    public void setMessage(String m) {

        dialog_message.setVisibility(View.VISIBLE);
        dialog_message.setText(m);
    }

    public void setEsc(String e, final View.OnClickListener listener) {
        bottom.setVisibility(View.VISIBLE);
        dialog_esc.setVisibility(View.VISIBLE);
        dialog_esc.setText(e);
        dialog_esc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
    }

    public void setOk(String e, final View.OnClickListener listener) {

        bottom.setVisibility(View.VISIBLE);
        dialog_ok.setVisibility(View.VISIBLE);
        dialog_ok.setText(e);
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v);
                }
                dismiss();
            }
        });
    }

}

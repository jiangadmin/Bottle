package com.sy.bottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Const;
import com.sy.bottle.utils.LogUtil;

/**
 * @author: jiangyao
 * @date: 2018/7/10
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 阅后即焚 文字
 */
public class ReadDes_Text_Dialog extends MyDialog implements View.OnTouchListener {
    private static final String TAG = "ReadDes_Text_Dialog";

    RelativeLayout view;
    TextView message_text;
    static SpannableStringBuilder builder;

    public ReadDes_Text_Dialog(@NonNull Context context, SpannableStringBuilder builder1) {
        super(context, R.style.myDialogTheme);
        builder = builder1;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_readdes_text);

        view = findViewById(R.id.message_view);
        message_text = findViewById(R.id.message_text);

        view.setOnTouchListener(this);

        if (!builder.toString().contains(Const.ReadDes)) {
            message_text.setText("生命里有很多事情，注定得不到圆满，有人进来就会有人离去，一如青春的散场，带走的是欢笑，留下的却是叹息。");
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG, "按下");

                //本次未被查看
                if (builder.toString().contains(Const.ReadDes)) {
                    builder.delete(builder.length() - 12, builder.length());
                    message_text.setText(builder);
                }else {
                    message_text.setText("真的，不骗您，您真的看过了");
                }

                break;
            //抬起
            case MotionEvent.ACTION_UP:
                this.dismiss();
                LogUtil.e(TAG, "抬起");
                break;

        }
        return true;
    }
}

package com.sy.bottle.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;

/**
 * @author: jiangyao
 * @date: 2018/7/10
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 阅后即焚 图像
 */
public class ReadDes_Img_Dialog extends MyDialog implements View.OnTouchListener {
    private static final String TAG = "ReadDes_Text_Dialog";

    RelativeLayout view;

    ImageView imageView;

    TextView message_s;
    static String url;

    CountDownTimer countDownTimer;

    public ReadDes_Img_Dialog(@NonNull Context context, String imgurl) {
        super(context);
        url = imgurl;
        show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_readdes_img);

        view = findViewById(R.id.message_view);
        imageView = findViewById(R.id.message_img);
        message_s = findViewById(R.id.message_s);

        view.setOnTouchListener(this);


        countDownTimer = new CountDownTimer(7 * 1000, 1000 - 10) {
            @Override
            public void onTick(long l) {
                message_s.setText(String.valueOf((l + 15) / 1000));
            }

            @Override
            public void onFinish() {
                dismiss();
            }
        };

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG, "按下");

                //本次未被查看
                PicassoUtlis.img(url, imageView);

                countDownTimer.start();

                break;
            //抬起
            case MotionEvent.ACTION_UP:
                this.dismiss();
                LogUtil.e(TAG, "抬起");
                countDownTimer.cancel();
                break;

        }
        return true;
    }
}

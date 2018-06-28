package com.sy.bottle.dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;

import com.sy.bottle.R;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.TouchImageView;

/**
 * Created by  jiang
 * on 2017/7/20.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:TODO 图片
 * update：
 */
public class ShowImage_Dialog extends MyDialog {
    private static final String TAG = "ShowImage_Dialog";
    TouchImageView imageView;

    Object img;

    public ShowImage_Dialog(@NonNull Activity activity, Object img) {
        super(activity, R.style.myDialogTheme);
        this.img = img;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.CENTER);

        setContentView(R.layout.dialog_image);
        imageView = findViewById(R.id.dialog_image);

        if (img instanceof Bitmap) {
            imageView.setImageBitmap((Bitmap) img);
        }

        if (img instanceof String) {
            PicassoUtlis.img(String.valueOf(img), imageView, R.drawable.loading);
        }

        setCanceledOnTouchOutside(true);

    }
}

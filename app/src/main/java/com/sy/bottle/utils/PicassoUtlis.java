package com.sy.bottle.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.app.MyApp;

/**
 * Created by jiangyao
 * on 2017/11/16.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:TODO 图片加载库
 * update：
 */
public class PicassoUtlis {
    static Context context = MyApp.getInstance();

    /**
     * 网络加载图片到ImageView中
     *
     * @param imageUrl
     * @param imageView
     */
    public static void img(String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }

        Picasso.with(context).load(imageUrl).into(imageView);
    }

    public static void img(Uri imageUrl, ImageView imageView) {

        Picasso.with(context).load(imageUrl).into(imageView);
    }


    /**
     * 当加载网络图片时，由于加载过程中图片未能及时显示，此时可能需要设置等待时的图片，通过placeHolder()方法
     *
     * @param imageUrl
     * @param imageView
     * @param placeholder
     */
    public static void img(String imageUrl, ImageView imageView, int placeholder) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).placeholder(placeholder).into(imageView);

    }

    /**
     * 缩放图片
     * *
     *
     * @param imageUrl
     * @param imageView
     * @param placeholder
     */
    public static void img(String imageUrl, ImageView imageView, int placeholder, int with, int height) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).placeholder(placeholder).resize(with, height).into(imageView);

    }

    /**
     * 加载圆角图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void Cornersimg(String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).transform(new PicassoRoundTransform()).into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void rountimg(String imageUrl, ImageView imageView) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).transform(new CircleTransform()).into(imageView);
    }

    /**
     * 加载圆形图片
     *
     * @param imageUrl
     * @param imageView
     */
    public static void rountimg(String imageUrl, ImageView imageView, int placeholder) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        Picasso.with(context).load(imageUrl).placeholder(placeholder).transform(new CircleTransform()).into(imageView);

    }


    /**
     * 圆形
     */
    static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();//回收垃圾
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);//定义一个渲染器
            paint.setShader(shader);//设置渲染器
            paint.setAntiAlias(true);//设置抗拒齿，图片边缘相对清楚

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);//绘制图形

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    /**
     * 圆角
     */
    static class PicassoRoundTransform implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            int widthLight = source.getWidth();
            int heightLight = source.getHeight();

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(output);
            Paint paintColor = new Paint();
            paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

            RectF rectF = new RectF(new Rect(0, 0, widthLight, heightLight));

            canvas.drawRoundRect(rectF, widthLight / 5, heightLight / 5, paintColor);

            Paint paintImage = new Paint();
            paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            canvas.drawBitmap(source, 0, 0, paintImage);
            source.recycle();
            return output;
        }

        @Override
        public String key() {
            return "roundcorner";
        }

    }
}

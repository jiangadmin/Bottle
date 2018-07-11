package com.sy.bottle.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.ImageViewActivity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.FileUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMImage;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMImageType;
import com.tencent.imsdk.TIMMessage;

import java.io.File;
import java.io.IOException;

/**
 * 图片消息数据
 */
public class ImageMessage extends Message {

    private static final String TAG = "ImageMessage";
    private boolean isDownloading;

    public ImageMessage(TIMMessage message) {
        this.message = message;
    }

    public ImageMessage(String path) {
        this(path, 1);
    }

    /**
     * 图片消息构造函数
     *
     * @param path  图片路径
     * @param isOri 是否原图发送
     */
    public ImageMessage(String path, int isOri) {
        message = new TIMMessage();
        TIMImageElem elem = new TIMImageElem();
        elem.setPath(path);
        elem.setLevel(isOri);
        message.addElement(elem);
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(final ChatAdapter.ViewHolder viewHolder, final Context context, final int position) {
        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;

        viewHolder.rightMessage.setBackgroundResource(R.drawable.bg_bubble_blue);
        viewHolder.leftMessage.setBackgroundResource(R.drawable.bg_bubble_gray);

        final TIMImageElem e = (TIMImageElem) message.getElement(0);

        String friendfaceurl = SaveUtils.getString(Save_Key.S_头像 + message.getSender());
        if (!TextUtils.isEmpty(friendfaceurl)) {
            PicassoUtlis.img(friendfaceurl, viewHolder.leftAvatar, R.drawable.head_other);
        }
        PicassoUtlis.img(SaveUtils.getString(Save_Key.S_头像), viewHolder.rightAvatar, R.drawable.head_me);

        switch (message.status()) {
            case Sending:

                clearView(viewHolder);
                if (e.getLevel() == -1) {
                    TextView tv = new TextView(MyApp.getInstance());
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
                    tv.setText("【阅后即焚·图片】");
                    getBubbleView(viewHolder).addView(tv);
                } else {
                    ImageView imageView = new ImageView(MyApp.getInstance());
                    imageView.setImageBitmap(getThumb(e.getPath()));
                    getBubbleView(viewHolder).addView(imageView);

                }

                break;
            case SendSucc:
                for (final TIMImage image : e.getImageList()) {
                    if (image.getType() == TIMImageType.Thumb) {
                        final String uuid = image.getUuid();
                        if (FileUtil.isCacheFileExist(uuid)) {
                            if (e.getLevel() == -1) {
                                TextView tv = new TextView(MyApp.getInstance());
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
                                tv.setText("【点开、长按查看、松手即毁】");
                                getBubbleView(viewHolder).addView(tv);
                            } else {
                                showThumb(viewHolder, uuid);
                            }
                        } else {
                            image.getImage(FileUtil.getCacheFilePath(uuid), new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {//获取图片失败
                                    //错误码code和错误描述desc，可用于定位请求失败原因
                                    //错误码code含义请参见错误码表
                                    LogUtil.e(TAG, "getImage failed. code: " + code + " errmsg: " + desc);
                                }

                                @Override
                                public void onSuccess() {//成功，参数为图片数据
                                    if (e.getLevel() == -1) {
                                        TextView tv = new TextView(MyApp.getInstance());
                                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                        tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
                                        tv.setText("【点开、长按查看、松手即毁】");
                                        getBubbleView(viewHolder).addView(tv);
                                    } else {
                                        showThumb(viewHolder, uuid);
                                    }
                                }
                            });
                        }
                    }
                    if (image.getType() == TIMImageType.Original) {
                        final String uuid = image.getUuid();
                        setImageEvent(viewHolder, uuid, context);
                        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogUtil.e(TAG, "图片地址 " + image.getUrl());
                                LogUtil.e(TAG, "图片地址 " + e.getLevel());
                                try {
                                    android.os.Message message = android.os.Message.obtain();
                                    if (e.getLevel() == -1) {
                                        message.what = e.getLevel();
                                        remove();
                                    }else {
                                        message.what = 1;
                                    }
                                    message.obj = image.getUrl();
                                    LogUtil.e(TAG, image.getType().toString());

                                    message.arg1 = position;
                                    ChatActivity.mHandler.sendMessage(message);

                                } catch (Exception e) {
                                    LogUtil.e(TAG, e.getMessage());
                                }
                                navToImageview(image, context);
                            }
                        });
                    }
                }
                break;
        }
        showStatus(viewHolder);

    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null)
            return str;
        return "[图片]";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {
        final TIMImageElem e = (TIMImageElem) message.getElement(0);
        for (TIMImage image : e.getImageList()) {
            if (image.getType() == TIMImageType.Original) {
                final String uuid = image.getUuid();
                if (FileUtil.isCacheImgExist(uuid + ".jpg")) {
                    TabToast.makeText("文件已存在");
                    return;
                }
                image.getImage(FileUtil.file_path + (uuid + ".jpg"), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        LogUtil.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
                    }

                    @Override
                    public void onSuccess() {

                        TabToast.makeText("保存成功");
                    }
                });
            }
        }
    }

    /**
     * 生成缩略图
     * 缩略图是将原图等比压缩，压缩后宽、高中较小的一个等于198像素
     * 详细信息参见文档
     */
    private Bitmap getThumb(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
        if (width > height) {
            reqWidth = 198;
            reqHeight = (reqWidth * height) / width;
        } else {
            reqHeight = 198;
            reqWidth = (width * reqHeight) / height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try {
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        } catch (IOException e) {
            return null;
        }
    }

    private void showThumb(final ChatAdapter.ViewHolder viewHolder, String filename) {
        Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getCacheFilePath(filename));
        ImageView imageView = new ImageView(MyApp.getInstance());
        imageView.setImageBitmap(bitmap);
        getBubbleView(viewHolder).addView(imageView);
    }

    private void setImageEvent(final ChatAdapter.ViewHolder viewHolder, final String fileName, final Context context) {
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("filename", fileName);
                context.startActivity(intent);
            }
        });
    }

    private void navToImageview(final TIMImage image, final Context context) {
        if (FileUtil.isCacheFileExist(image.getUuid())) {
            String path = FileUtil.getCacheFilePath(image.getUuid());
            File file = new File(path);
            if (file.length() < image.getSize()) {
                TabToast.makeText("正在下载,请稍后");
                return;
            }

//            new ShowImage_Dialog(MyApp.currentActivity(),image.getUrl()).show();
//            Intent intent = new Intent(context, ImageViewActivity.class);
//            intent.putExtra("filename", image.getUuid());
//            context.startActivity(intent);
        } else {
            if (!isDownloading) {
                isDownloading = true;
                image.getImage(FileUtil.getCacheFilePath(image.getUuid()), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        //错误码code和错误描述desc，可用于定位请求失败原因
                        //错误码code含义请参见错误码表
                        LogUtil.e(TAG, "getImage failed. code: " + i + " errmsg: " + s);
                        TabToast.makeText("下载失败");
                        isDownloading = false;
                    }

                    @Override
                    public void onSuccess() {
                        isDownloading = false;
//                        new ShowImage_Dialog(MyApp.currentActivity(),image.getUrl()).show();
//                        Intent intent = new Intent(context, ImageViewActivity.class);
//                        intent.putExtra("filename", image.getUuid());
//                        context.startActivity(intent);
                    }
                });
            } else {
                TabToast.makeText("正在下载,请稍后");
            }
        }
    }
}

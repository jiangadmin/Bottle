package com.sy.bottle.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;

import com.sy.bottle.BuildConfig;
import com.sy.bottle.R;

import java.io.File;

/**
 * @author: jiangyao
 * @date: 2018/6/2
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 照片
 */
public class Photos_Dialog extends MyDialog implements View.OnClickListener {
    private static final String TAG = "Photos_Dialog";

    Button photos_camera, photos_gallery, photos_esc;

    Activity activity;

    /**
     * 请求相机
     */
    private static final int REQUEST_CAPTURE = 200;
    /**
     * 请求相册
     */
    private static final int REQUEST_PICK = 201;
    /**
     * 请求截图
     */
    private static final int REQUEST_CROP_PHOTO = 202;
    private Uri tempUri;
    private static File tempFile;

    public Photos_Dialog(@NonNull Activity context) {
        super(context, R.style.myDialogTheme);

        activity = context;
        show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_photos);

        initview();
    }

    private void initview() {
        photos_camera = findViewById(R.id.photos_camera);
        photos_gallery = findViewById(R.id.photos_gallery);
        photos_esc = findViewById(R.id.photos_esc);

        photos_camera.setOnClickListener(this);
        photos_gallery.setOnClickListener(this);
        photos_esc.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photos_camera:

                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", tempFile);
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                activity.startActivityForResult(openCameraIntent, REQUEST_CAPTURE);
                break;
            case R.id.photos_gallery:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activity.startActivityForResult(intent, REQUEST_PICK);// //适用于4.4及以上android版本
                break;

        }
        dismiss();
    }
}

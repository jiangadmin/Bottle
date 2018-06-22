package com.sy.bottle.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.sy.bottle.R;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.view.RateTextCircularProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/7/21.
 */
public class UpdateManger {
    private static final String TAG = "UpdateManger";
    // 应用程序Context
    private Context mContext;
    // 提示消息
    private Dialog noticeDialog;// 提示有软件更新的对话框
    private Dialog downloadDialog;// 下载对话框
    private static final String savePath = Environment.getExternalStorageDirectory() + "/update/";// 保存apk的文件夹
    private static String saveFileName = null;
    // 进度条与通知UI刷新的handler和msg常量
    private RateTextCircularProgressBar progressBar;
    private Button dialogsuccess, dialogesc;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;// 当前进度
    private Thread downLoadThread; // 下载线程
    private boolean interceptFlag = false;// 用户取消下载
    // 通知处理刷新界面的handler
    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    progressBar.setProgress(progress);
                    break;
                case DOWN_OVER:

                    progressBar.setVisibility(View.GONE);
                    dialogsuccess.setVisibility(View.VISIBLE);

                    installApk();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public UpdateManger(Context context, int version) {
        saveFileName = savePath + "sybottle" + version + ".apk";
        this.mContext = context;
    }

    // 显示更新程序对话框，供主程序调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    /**
     * 升级提示
     * 提示更新了什么
     */
    private void showNoticeDialog() {

        Base_Dialog base_dialog = new Base_Dialog(MyApp.currentActivity());
        base_dialog.setTitle("发现新版本");
        base_dialog.setOk("立即更新", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动下载框
                showDownloadDialog();
            }
        });


//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);// Builder，可以通过此builder设置改变AleartDialog的默认的主题样式及属性相关信息
//        builder.setTitle("发现新版本");
//        builder.setMessage(MDApp.Update_Message);
//        builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
        //判断是否强制更新
        if (!MyApp.Update_Type) {
            base_dialog.setEsc("以后再说", null);
        }

//        noticeDialog = builder.create();
        noticeDialog.setCanceledOnTouchOutside(false);
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }

    /**
     * 下载提示  圆形进度条
     */
    protected void showDownloadDialog() {
        downloadDialog = new Dialog(mContext, R.style.LoadingDialog);
        downloadDialog.setCancelable(false);
        downloadDialog.setContentView(R.layout.dialog_update_download);
        progressBar = downloadDialog.findViewById(R.id.update_progressbar);
        dialogsuccess = downloadDialog.findViewById(R.id.update_success);
        dialogesc = downloadDialog.findViewById(R.id.update_esc);
        if (MyApp.Update_Type) {
            dialogesc.setVisibility(View.GONE);
        }
        dialogsuccess.setVisibility(View.GONE);
        downloadDialog.show();
        dialogesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadDialog.dismiss();
                interceptFlag = true;
            }
        });
        dialogsuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                installApk();
            }
        });
        //先看看有没有下载好
        if (new File(saveFileName).exists()) {
            progressBar.setVisibility(View.GONE);
            dialogsuccess.setVisibility(View.VISIBLE);
            //安装吧
            installApk();
        } else {
            //启动下载
            downloadApk();
        }
    }

    /**
     * 启动下载
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 启动安装界面
     */
    protected void installApk() {

        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");// File.toString()会返回路径信息
        mContext.startActivity(i);
    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            URL url;
            FileOutputStream outStream = null;
            try {
                url = new URL(MyApp.Update_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream ins = conn.getInputStream();
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                outStream = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = ins.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 下载进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    outStream.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消停止下载

                ins.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outStream != null) {
                    try {
                        outStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
}




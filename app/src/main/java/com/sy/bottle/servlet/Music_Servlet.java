package com.sy.bottle.servlet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.utils.LogUtil;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMMessage;

/**
 * @author: jiangyao
 * @date: 2018/6/16
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 音乐播放
 */
public class Music_Servlet extends AsyncTask<TIMMessage, Integer, String> {
    private static final String TAG = "Music_Servlet";

    @Override
    protected String doInBackground(TIMMessage... messages) {
        TIMMessage timMessage = messages[0];

        //正在聊天 不提示
        if (MyApp.ChatId.equals(timMessage.getSender())) {
            return null;
        }

        //获取NotificationManager实例
        NotificationManager notifyManager = (NotificationManager) MyApp.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        //实例化NotificationCompat.Builde并设置相关属性
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getInstance());

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(uri);
        //设置小图标
        builder.setSmallIcon(R.mipmap.logo);
        //设置通知标题
        builder.setContentTitle(timMessage.getSenderProfile().getNickName());
        //设置通知内容
        LogUtil.e(TAG, timMessage.getElement(0).getType().toString());
        if (timMessage.getElement(0).getType() == TIMElemType.Image) {
            builder.setContentText("发来一张图片");
        }
        if (timMessage.getElement(0).getType() == TIMElemType.Custom) {
            builder.setContentText("发来一个礼物");
        }
        if (timMessage.getElement(0).getType() == TIMElemType.Text) {
            builder.setContentText("发来一条消息");
        }
        if (timMessage.getElement(0).getType() == TIMElemType.Face) {
            builder.setContentText("发来一条消息");
        }
        if (timMessage.getElement(0).getType() == TIMElemType.Sound) {
            builder.setContentText("发来一段语音");
        }

        Intent intent = new Intent(MyApp.currentActivity(), ChatActivity.class);//点击之后进入MainActivity
        intent.putExtra("identify", timMessage.getSender());
        intent.putExtra("type", TIMConversationType.C2C);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApp.getInstance(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);//设置意图
        //设置通知时间，默认为系统发出通知的时间，通常不用设置
        //.setWhen(System.currentTimeMillis());
        //通过builder.build()方法生成Notification对象,并发送通知,id=1

        notifyManager.notify(Integer.valueOf(timMessage.getSender()), builder.build());

//        //直接创建，不需要设置setDataSource
//        MediaPlayer mMediaPlayer = MediaPlayer.create(MyApp.getInstance(), R.raw.dudulu);
//        mMediaPlayer.start();
//        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mediaPlayer.stop();
//                mediaPlayer.release();
//                mediaPlayer = null;
//            }
//        });


        return null;
    }
}

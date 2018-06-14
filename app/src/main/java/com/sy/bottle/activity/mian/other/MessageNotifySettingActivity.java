package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.CompoundButton;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.view.LineControllerView;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMValueCallBack;

public class MessageNotifySettingActivity extends Base_Activity {
    private static final String TAG = "MessageNotifySettingAct";

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MessageNotifySettingActivity.class);
        context.startActivity(intent);
    }

    TIMOfflinePushSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notify_setting);

        setTitle("消息提醒");
        setBack(true);

        final Uri notifyMusic = Uri.parse("android.resource://com.sy.bottle/" + R.raw.dudulu);

        TIMManager.getInstance().getOfflinePushSettings(new TIMValueCallBack<TIMOfflinePushSettings>() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "错误" + s);
            }

            @Override
            public void onSuccess(TIMOfflinePushSettings timOfflinePushSettings) {
                LogUtil.e(TAG, timOfflinePushSettings.getC2cMsgRemindSound().toString());
                LogUtil.e(TAG, timOfflinePushSettings.getGroupMsgRemindSound().toString());
            }
        });
        TIMManager.getInstance().getOfflinePushSettings(new TIMValueCallBack<TIMOfflinePushSettings>() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "get offline push setting error " + s);
            }

            @Override
            public void onSuccess(TIMOfflinePushSettings timOfflinePushSettings) {
                settings = timOfflinePushSettings;


                LineControllerView messagePush = findViewById(R.id.messagePush);
                messagePush.setSwitch(settings.isEnabled());
                messagePush.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setEnabled(isChecked);
                        TIMManager.getInstance().setOfflinePushSettings(settings);
                    }
                });
                LineControllerView c2cMusic = findViewById(R.id.c2cMusic);
                c2cMusic.setSwitch(settings.getC2cMsgRemindSound() != null);
                c2cMusic.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setC2cMsgRemindSound(isChecked ? notifyMusic : null);
                        TIMManager.getInstance().setOfflinePushSettings(settings);
                    }
                });
                LineControllerView groupMusic = findViewById(R.id.groupMusic);
                groupMusic.setSwitch(settings.getGroupMsgRemindSound() != null);
                groupMusic.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        settings.setGroupMsgRemindSound(isChecked ? notifyMusic : null);
                        TIMManager.getInstance().setOfflinePushSettings(settings);
                    }
                });
            }
        });

    }
}

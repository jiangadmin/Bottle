package com.sy.bottle.model;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sy.bottle.R;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.FileUtil;
import com.sy.bottle.utils.MediaUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSoundElem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 语音消息数据
 */
public class VoiceMessage extends Message {

    private static final String TAG = "VoiceMessage";

    public VoiceMessage(TIMMessage message) {
        this.message = message;
    }

    /**
     * 语音消息构造方法
     *
     * @param duration 时长
     * @param filePath 语音数据地址
     */
    public VoiceMessage(long duration, String filePath) {
        message = new TIMMessage();
        TIMSoundElem elem = new TIMSoundElem();
        elem.setPath(filePath);
        elem.setDuration(duration);  //填写语音时长
        message.addElement(elem);
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context, int position) {
        if (checkRevoke(viewHolder)) return;
        LinearLayout linearLayout = new LinearLayout(MyApp.getInstance());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        ImageView voiceIcon = new ImageView(MyApp.getInstance());
        voiceIcon.setBackgroundResource(message.isSelf() ? R.drawable.right_voice : R.drawable.left_voice);
        final AnimationDrawable frameAnimatio = (AnimationDrawable) voiceIcon.getBackground();

        TextView tv = new TextView(MyApp.getInstance());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        tv.setText(String.valueOf(((TIMSoundElem) message.getElement(0)).getDuration()) + "’");

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, context.getResources().getDisplayMetrics());
        int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, context.getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, context.getResources().getDisplayMetrics());
        int width2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14 + 3 * ((TIMSoundElem) message.getElement(0)).getDuration(), context.getResources().getDisplayMetrics());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width2, height2);
        LinearLayout.LayoutParams imageLp = new LinearLayout.LayoutParams(width, height);

        if (message.isSelf()) {
            imageLp.setMargins(10, 0, 0, 0);
            voiceIcon.setLayoutParams(imageLp);
            tv.setLayoutParams(lp);
            linearLayout.addView(tv);
            linearLayout.addView(voiceIcon);
        } else {
            voiceIcon.setLayoutParams(imageLp);
            lp.setMargins(10, 0, 0, 0);
            tv.setLayoutParams(lp);
            linearLayout.addView(tv);
            linearLayout.addView(voiceIcon);
        }

        viewHolder.rightMessage.setBackgroundResource(R.drawable.bg_bubble_blue);
        viewHolder.leftMessage.setBackgroundResource(R.drawable.bg_bubble_gray);

        String friendfaceurl = SaveUtils.getString(Save_Key.S_头像 + message.getSender());
        if (!TextUtils.isEmpty(friendfaceurl)) {
            PicassoUtlis.img(friendfaceurl, viewHolder.leftAvatar, R.drawable.head_other);
            Glide.with(context).load(friendfaceurl).apply(new RequestOptions().placeholder(R.drawable.head_other)).into(viewHolder.leftAvatar);
        }
        PicassoUtlis.img(SaveUtils.getString(Save_Key.S_头像), viewHolder.rightAvatar, R.drawable.head_me);


        clearView(viewHolder);
        getBubbleView(viewHolder).addView(linearLayout);
        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceMessage.this.playAudio(frameAnimatio);

            }
        });
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        return "[语音]";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private void playAudio(final AnimationDrawable frameAnimatio) {

        if (MediaUtil.getInstance().IsPlay()) {
            MediaUtil.getInstance().stop();
            frameAnimatio.stop();
            frameAnimatio.selectDrawable(0);

            return;
        }

        TIMSoundElem elem = (TIMSoundElem) message.getElement(0);
        final File tempAudio = FileUtil.getTempFile(FileUtil.FileType.AUDIO);
        elem.getSoundToFile(tempAudio.getAbsolutePath(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess() {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(tempAudio);
                    MediaUtil.getInstance().play(fis);
                    frameAnimatio.start();
                    MediaUtil.getInstance().setEventListener(new MediaUtil.EventListener() {
                        @Override
                        public void onStop() {
                            frameAnimatio.stop();
                            frameAnimatio.selectDrawable(0);
                        }
                    });
                    fis.close();
                } catch (Exception e) {

                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }
}

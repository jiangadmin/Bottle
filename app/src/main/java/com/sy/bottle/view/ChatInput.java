package com.sy.bottle.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.mine.MyBalance_Activity;
import com.sy.bottle.adapters.Gift_Adapter;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.entity.Gift_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.GiftList_Servlet;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.viewfeatures.ChatView;
import com.tencent.imsdk.TIMLocationElem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

/**
 * TODO:聊天界面输入控件
 */
public class ChatInput extends RelativeLayout implements TextWatcher, View.OnClickListener {
    private static final String TAG = "ChatInput";
    Button btnSend;
    private ImageButton btnAdd, btnVoice, btnKeyboard, btnEmotion;
    private EditText editText;
    int isHoldVoiceBtn;
    private boolean isSendVisible, isEmoticonReady, isGiftReady;
    private InputMode inputMode = InputMode.NONE;
    private ChatView chatView;
    private LinearLayout morePanel, textPanel;
    private TextView voicePanel;
    private ScrollView emoticonPanel_sl;
    private LinearLayout emoticonPanel;
    private GridView giftPanel;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    Context context;

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        initView();
    }

    /**
     * 点击时坐标  离开时候坐标
     */
    float y1, y2;

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        textPanel = findViewById(R.id.text_panel);
        btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnVoice = findViewById(R.id.btn_voice);
        btnVoice.setOnClickListener(this);
        btnEmotion = findViewById(R.id.btnEmoticon);
        btnEmotion.setOnClickListener(this);
        morePanel = findViewById(R.id.morePanel);

        //拍照
        LinearLayout BtnImage = findViewById(R.id.btn_photo);
        BtnImage.setOnClickListener(this);
        //照片
        LinearLayout BtnPhoto = findViewById(R.id.btn_image);
        BtnPhoto.setOnClickListener(this);
        //视频
        LinearLayout btnVideo = findViewById(R.id.btn_video);
        btnVideo.setOnClickListener(this);
        //文件
        LinearLayout btnFile = findViewById(R.id.btn_file);
        btnFile.setOnClickListener(this);
        //礼物
        LinearLayout btnGift = findViewById(R.id.btn_gift);
        btnGift.setOnClickListener(this);
        //位置
        LinearLayout btnPosition = findViewById(R.id.btn_position);
        btnPosition.setOnClickListener(this);

        setSendBtn();
        btnKeyboard = findViewById(R.id.btn_keyboard);
        btnKeyboard.setOnClickListener(this);
        voicePanel = findViewById(R.id.voice_panel);

        voicePanel.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:

                        y1 = event.getY();

                        isHoldVoiceBtn = 0;
                        updateVoiceView();

                        break;
                    //抬起
                    case MotionEvent.ACTION_UP:
                        LogUtil.e(TAG, "抬起按钮");

                        if (isHoldVoiceBtn != -1) {
                            isHoldVoiceBtn = 1;
                            updateVoiceView();
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        y2 = event.getY();
                        if (y1 - y2 > 500) {
                            LogUtil.e(TAG, "移动");
                            isHoldVoiceBtn = -1;
                            updateVoiceView();
                        } else {
                            isHoldVoiceBtn = 0;
                        }

                        break;
                }
                return true;
            }
        });
        editText = findViewById(R.id.input);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateView(InputMode.TEXT);
                }
            }
        });
        isSendVisible = editText.getText().length() != 0;
        emoticonPanel_sl = findViewById(R.id.emoticonPanel_sl);
        emoticonPanel = findViewById(R.id.emoticonPanel);
        giftPanel = findViewById(R.id.giftPanel);

    }

    private void updateView(InputMode mode) {
        if (mode == inputMode) return;
        leavingCurrentState();
        switch (inputMode = mode) {
            //更多
            case MORE:
                morePanel.setVisibility(VISIBLE);
                break;
            //文字
            case TEXT:
                if (editText.requestFocus()) {
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
            //声音
            case VOICE:
                voicePanel.setVisibility(VISIBLE);
                textPanel.setVisibility(GONE);
                btnVoice.setVisibility(GONE);
                btnKeyboard.setVisibility(VISIBLE);
                break;
            //表情
            case EMOTICON:
                if (!isEmoticonReady) {
                    prepareEmoticon();
                }
                emoticonPanel_sl.setVisibility(VISIBLE);
                emoticonPanel.setVisibility(VISIBLE);
                break;
            //礼物
            case GIFT:
                if (!isGiftReady) {
                    prepareGift();
                }
                giftPanel.setVisibility(VISIBLE);
                break;

        }
    }

    private void leavingCurrentState() {
        switch (inputMode) {
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                editText.clearFocus();
                break;
            case MORE:
                morePanel.setVisibility(GONE);
                break;
            case VOICE:
                voicePanel.setVisibility(GONE);
                textPanel.setVisibility(VISIBLE);
                btnVoice.setVisibility(VISIBLE);
                btnKeyboard.setVisibility(GONE);
                break;
            case EMOTICON:
                emoticonPanel_sl.setVisibility(GONE);
                emoticonPanel.setVisibility(GONE);
            case GIFT:
                giftPanel.setVisibility(GONE);
                break;

        }
    }


    private void updateVoiceView() {
        switch (isHoldVoiceBtn) {
            case -1:
                TabToast.makeText("取消发送");
                voicePanel.setText("按住 说话");
                voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_normal));
                chatView.cancelSendVoice();
                break;
            case 0:
                voicePanel.setText("松开 结束");
                voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_pressed));
                chatView.startSendVoice();
                break;
            case 1:
                voicePanel.setText("按住 说话");
                voicePanel.setBackground(getResources().getDrawable(R.drawable.btn_voice_normal));
                chatView.endSendVoice();
                break;
        }

    }


    /**
     * 关联聊天界面逻辑
     */
    public void setChatView(ChatView chatView) {
        this.chatView = chatView;
    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * are about to be replaced by new text with length <code>after</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * This method is called to notify you that, within <code>s</code>,
     * the <code>count</code> characters beginning at <code>start</code>
     * have just replaced old text that had length <code>before</code>.
     * It is an error to attempt to make changes to <code>s</code> from
     * this callback.
     *
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
        if (isSendVisible) {
            chatView.sending();
        }
    }

    /**
     * This method is called to notify you that, somewhere within
     * <code>s</code>, the text has been changed.
     * It is legitimate to make further changes to <code>s</code> from
     * this callback, but be careful not to get yourself into an infinite
     * loop, because any changes you make will cause this method to be
     * called again recursively.
     * (You are not told where the change took place because other
     * afterTextChanged() methods may already have made other changes
     * and invalidated the offsets.  But if you need to know here,
     * you can use {@link Spannable#setSpan} in {@link #onTextChanged}
     * to mark your place and then look up from here where the span
     * ended up.
     *
     * @param s
     */
    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setSendBtn() {
        if (isSendVisible) {
            btnAdd.setVisibility(GONE);
            btnSend.setVisibility(VISIBLE);
        } else {
            btnAdd.setVisibility(VISIBLE);
            btnSend.setVisibility(GONE);
        }
    }

    /**
     * 准备礼物
     */
    private void prepareGift() {
        if (giftPanel == null) return;
        //获取礼物列表
        new GiftList_Servlet(this).execute();

    }

    /**
     * 礼物列表
     *
     * @param dataBeans
     */
    public void CallBack_Gift(final List<Gift_Entity.DataBean> dataBeans) {

        Gift_Adapter gift_adapter = new Gift_Adapter(context);
        gift_adapter.setDataBeans(dataBeans);
        giftPanel.setAdapter(gift_adapter);

        isGiftReady = true;

        giftPanel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (SaveUtils.getInt(Save_Key.S_能量) < dataBeans.get(i).getPrice()) {
                    Base_Dialog base_dialog = new Base_Dialog(context);
                    base_dialog.setTitle("能量不足");
                    base_dialog.setMessage("请在设置页面领取今天赠送能量、当日赠送能量消耗完，请补充能量或明天聊");
                    base_dialog.setOk("去充值", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyBalance_Activity.start(context);

                        }
                    });
                    base_dialog.setEsc("再想想", null);

                } else {

                    Base_Dialog base_dialog = new Base_Dialog(context);
                    base_dialog.setTitle("确认送出" + dataBeans.get(i).getName() + "?");
                    base_dialog.setMessage("本次消费，你需要支付" + dataBeans.get(i).getPrice() + "能量确认支付吗?");
                    base_dialog.setOk("确认", new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //逗号分隔  ID在前  地址在后
                            Gift_Entity.DataBean bean = dataBeans.get(i);
                            Map map = new HashMap();

                            map.put("Id", bean.getId());
                            map.put("Content", bean.getContent());
                            map.put("Name", bean.getName());
                            map.put("Pic_url", bean.getPic_url());
                            map.put("Price", bean.getPrice());

                            chatView.sendGift(map);
                        }
                    });
                    base_dialog.setEsc("取消", null);
                }
            }
        });
    }

    /**
     * 准备表情
     */
    private void prepareEmoticon() {
        if (emoticonPanel == null) return;
        for (int i = 0; i < 10; ++i) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
            for (int j = 0; j < 7; ++j) {

                try {
                    AssetManager am = getContext().getAssets();
                    final int index = 7 * i + j;
                    InputStream is = am.open(String.format("emoticon/%d.png", index));

                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Matrix matrix = new Matrix();
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    matrix.postScale(1.8f, 1.8f);
                    final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    GifImageView image = new GifImageView(getContext());
                    image.setImageBitmap(resizedBitmap);
                    image.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));
                    linearLayout.addView(image);
                    image.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String content = String.valueOf(index);
                            SpannableString str = new SpannableString(String.valueOf(index));
                            ImageSpan span = new ImageSpan(getContext(), resizedBitmap, ImageSpan.ALIGN_BASELINE);
                            str.setSpan(span, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            editText.append(str);
                        }
                    });
                    is.close();
                } catch (IOException e) {

                }

            }
            emoticonPanel.addView(linearLayout);
        }
        isEmoticonReady = true;
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Activity activity = (Activity) getContext();

        switch (v.getId()) {
            case R.id.btn_send:
                chatView.sendText();
                break;
            case R.id.btn_add:
                updateView(inputMode == InputMode.MORE ? InputMode.TEXT : InputMode.MORE);
                break;
            case R.id.btn_photo:
                if (activity != null && requestCamera(activity)) {
                    chatView.sendPhoto();
                }
                break;
            case R.id.btn_image:
                if (activity != null && requestStorage(activity)) {
                    chatView.sendImage();
                }
                break;
            case R.id.btn_voice:
                if (activity != null && requestAudio(activity)) {
                    updateView(InputMode.VOICE);
                }
                break;
            case R.id.btn_keyboard:
                updateView(InputMode.TEXT);
                break;
            //视频
            case R.id.btn_video:
                if (getContext() instanceof FragmentActivity) {
                    FragmentActivity fragmentActivity = (FragmentActivity) getContext();
                    if (requestVideo(fragmentActivity)) {
//                    VideoInputDialog.show(fragmentActivity.getSupportFragmentManager());
                        if (requestRtmp()) {
                            chatView.videoAction();
                        } else {
                            TabToast.makeText("系统版本太低");
                        }
                    }
                }
                break;
            //表情
            case R.id.btnEmoticon:
                updateView(inputMode == InputMode.EMOTICON ? InputMode.TEXT : InputMode.EMOTICON);
                break;
            //文件
            case R.id.btn_file:
                chatView.sendFile();
                break;
            //礼物
            case R.id.btn_gift:
                updateView(InputMode.GIFT);
                break;
            //位置
            case R.id.btn_position:
                chatView.sendLocation();
                break;
        }

    }


    /**
     * 获取输入框文字
     */
    public Editable getText() {
        return editText.getText();
    }

    /**
     * 设置输入框文字
     */
    public void setText(String text) {
        editText.setText(text);
    }


    /**
     * 设置输入模式
     */
    public void setInputMode(InputMode mode) {
        updateView(mode);
    }


    public enum InputMode {
        TEXT,
        VOICE,
        EMOTICON,
        MORE,
        VIDEO,
        NONE,
        GIFT,
    }

    private boolean requestRtmp() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean requestVideo(Activity activity) {
        if (afterM()) {
            final List<String> permissionsList = new ArrayList<>();
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if (permissionsList.size() != 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestCamera(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestAudio(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean requestStorage(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

}

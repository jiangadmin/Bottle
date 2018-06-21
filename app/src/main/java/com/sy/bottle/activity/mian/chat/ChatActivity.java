package com.sy.bottle.activity.mian.chat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.friend.FriendInfo_Activity;
import com.sy.bottle.activity.mian.friend.UserInfo_Activity;
import com.sy.bottle.activity.ui.TCVideoRecordActivity;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.model.CustomMessage;
import com.sy.bottle.model.FileMessage;
import com.sy.bottle.model.GroupInfo;
import com.sy.bottle.model.ImageMessage;
import com.sy.bottle.model.Message;
import com.sy.bottle.model.MessageFactory;
import com.sy.bottle.model.TextMessage;
import com.sy.bottle.model.UGCMessage;
import com.sy.bottle.model.VideoMessage;
import com.sy.bottle.model.VoiceMessage;
import com.sy.bottle.presenter.ChatPresenter;
import com.sy.bottle.servlet.Gift_For_Servlet;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.FileUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.MediaUtil;
import com.sy.bottle.utils.RecorderUtil;
import com.sy.bottle.utils.Util;
import com.sy.bottle.view.ChatInput;
import com.sy.bottle.view.TabToast;
import com.sy.bottle.view.VoiceSendingView;
import com.sy.bottle.viewfeatures.ChatView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO：聊天界面
 */
public class ChatActivity extends Base_Activity implements ChatView, View.OnClickListener {
    private static final String TAG = "ChatActivity";

    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;
    private ListView listView;
    private ChatPresenter presenter;
    private ChatInput input;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;             //礼物
    private static final int FILE_CODE = 300;               //文件
    private static final int IMAGE_PREVIEW = 400;           //图片
    private static final int VIDEO_RECORD = 500;            //视频
    private Uri fileUri;
    private VoiceSendingView voiceSendingView;
    private String identify;
    private RecorderUtil recorder = new RecorderUtil();
    private TIMConversationType type;
    private String titleStr;
    private Handler handler = new Handler();

    TextView mesage;

    public static void navToChat(Context context, String identify, TIMConversationType type) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setBack(true);

        identify = getIntent().getStringExtra("identify");
        type = (TIMConversationType) getIntent().getSerializableExtra("type");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //移除标记为id的通知 (只是针对当前Context下的所有Notification)
        notificationManager.cancel(Integer.parseInt(identify));

        presenter = new ChatPresenter(this, identify, type);
        input = findViewById(R.id.input_panel);
        mesage = findViewById(R.id.mesage);
        input.setChatView(this);
        adapter = new ChatAdapter(this, R.layout.item_message, messageList);
        listView = findViewById(R.id.list);

        Util.startTimer(mesage, 30, 1);

        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        input.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    presenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });
        registerForContextMenu(listView);

        switch (type) {
            //个人聊天
            case C2C:
                setMenu(R.drawable.btn_person);

//                如果是好友
                if (MyApp.friendsbeans != null && MyApp.friendsbeans.size() > 0) {
                    for (Friends_Entity.DataBean bean : MyApp.friendsbeans) {
                        if (bean.getFriend_id().equals(identify)) {
                            setRTitle(bean.getNikename());
                            adapter.setHead(bean.getAvatar().contains("http") ? bean.getAvatar() : Const.IMG + bean.getAvatar());
                            adapter.notifyDataSetChanged();

                        }
                    }
                } else {
                    new UserInfo_Servlet(this).execute(identify);
                }
                break;
            //群聊
            case Group:
                setMenu(R.drawable.btn_group);

                setTitle(GroupInfo.getInstance().getGroupName(identify));
                break;

        }
        voiceSendingView = findViewById(R.id.voice_sending);
        presenter.start();

    }

    /**
     * 查询数据返回
     *
     * @param bean
     */
    public void Callback_UserInfo(UserInfo_Entity.DataBean bean) {
        setRTitle(bean.getNikename());
        adapter.setHead(bean.getAvatar().contains("http") ? bean.getAvatar() : Const.IMG + bean.getAvatar());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (input.getText().length() > 0) {
            TextMessage message = new TextMessage(input.getText());
            presenter.saveDraft(message.getMessage());
        } else {
            presenter.saveDraft(null);
        }
//        RefreshEvent.getInstance().onRefresh();
        presenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
    }

    /**
     * 显示消息
     *
     * @param message
     */
    @Override
    public void showMessage(TIMMessage message) {

        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {

            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {

                if (mMessage instanceof CustomMessage) {
                    switch (((CustomMessage) mMessage).getType()) {

                        case TYPING:
                            setRTitle("对方正在输入...");
                            handler.removeCallbacks(resetTitle);
                            handler.postDelayed(resetTitle, 3000);
                            break;
                        case GIFT:
                            TabToast.makeText("礼物到了");
                            if (messageList.size() == 0) {
                                mMessage.setHasTime(null);
                            } else {
                                mMessage.setHasTime(messageList.get(messageList.size() - 1).getMessage());
                            }
                            messageList.add(mMessage);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);
                            break;
                    }

                } else {
                    if (messageList.size() == 0) {
                        mMessage.setHasTime(null);
                    } else {
                        mMessage.setHasTime(messageList.get(messageList.size() - 1).getMessage());
                    }
                    messageList.add(mMessage);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(adapter.getCount() - 1);
                }
            }
        }
    }

    /**
     * 显示消息
     *
     * @param messages
     */
    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i) {

            Message mMessage = MessageFactory.getMessage(messages.get(i));

            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted) {
                continue;
            }

            //判断自定义消息
            if (mMessage instanceof CustomMessage) {
                switch (((CustomMessage) mMessage).getType()) {
                    case TYPING:
                        continue;
                    case INVALID:
                        continue;
                }
            }

            ++newMsgNum;
            if (i != messages.size() - 1) {
                mMessage.setHasTime(messages.get(i + 1));
                messageList.add(0, mMessage);
            } else {
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(newMsgNum);
    }

    /**
     * 显示撤销信息
     *
     * @param timMessageLocator
     */
    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {
        for (Message msg : messageList) {
            TIMMessageExt ext = new TIMMessageExt(msg.getMessage());
            if (ext.checkEquals(timMessageLocator)) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 清除所有消息，等待刷新
     */
    @Override
    public void clearAllMessage() {
        messageList.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : messageList) {
            if (msg.getMessage().getMsgUniqueId() == id) {
                switch (code) {
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc("内容还有敏感词");
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        adapter.notifyDataSetChanged();

    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto() {
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_photo.resolveActivity(getPackageManager()) != null) {
            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
            if (tempFile != null) {
                fileUri = Uri.fromFile(tempFile);
            }
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        LogUtil.e(TAG, "发送文本消息");
        Message message = new TextMessage(input.getText());
        presenter.sendMessage(message.getMessage());
        input.setText("");
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }

    /**
     * 发送礼物
     */
    @Override
    public void sendGift(Map map) {
        if (type == TIMConversationType.C2C) {

            map.put("give_user_id", identify);
            map.put("number", "1");

            //请求发送礼物
            new Gift_For_Servlet(this).execute(map);

        }
    }

    /**
     * 赠送礼物
     *
     * @param map
     */
    public void CallBack_ForGitf(Map map) {
        Message message = new CustomMessage(CustomMessage.Type.GIFT, map);
        presenter.sendMessage(message.getMessage());
    }

    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice() {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();
    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            TabToast.makeText("录音过短,请重试");
        } else if (recorder.getTimeInterval() > 60) {
            TabToast.makeText("录音过长,请重试");
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage());
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        presenter.sendMessage(message.getMessage());
    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void cancelSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
    }

    /**
     * 正在发送
     */
    @Override
    public void sending() {
        if (type == TIMConversationType.C2C) {
            //TODO: 正在输入状态发送
            Message message = new CustomMessage(CustomMessage.Type.TYPING, null);
//            presenter.sendOnlineMessage(message.getMessage());
        }
    }

    /**
     * 显示草稿
     */
    @Override
    public void showDraft(TIMMessageDraft draft) {
        input.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public void videoAction() {
        Intent intent = new Intent(this, TCVideoRecordActivity.class);
        startActivityForResult(intent, VIDEO_RECORD);
    }

    @Override
    public void showToast(String msg) {
        TabToast.makeText(msg);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, "删除");
        if (message.isSendFail()) {
            menu.add(0, 2, Menu.NONE, "重新发送");
        } else if (message.getMessage().isSelf()) {
            menu.add(0, 4, Menu.NONE, "撤回");
        }
        if (message instanceof ImageMessage || message instanceof FileMessage) {
            menu.add(0, 3, Menu.NONE, "保存");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                presenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            case 4:
                presenter.revokeMessage(message.getMessage());
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW) {
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri", false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists()) {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(path, options);
                    if (file.length() == 0 && options.outWidth == 0) {
                        TabToast.makeText("文件不存在,发送失败");
                    } else {
                        if (file.length() > 1024 * 1024 * 10) {
                            TabToast.makeText("文件过大,发送失败");
                        } else {
                            Message message = new ImageMessage(path, isOri);
                            presenter.sendMessage(message.getMessage());
                        }
                    }
                } else {

                    TabToast.makeText("文件不存在,发送失败");
                }
            }
        } else if (requestCode == VIDEO_RECORD) {
            if (resultCode == RESULT_OK) {
                String videoPath = data.getStringExtra("videoPath");
                String coverPath = data.getStringExtra("coverPath");
                long duration = data.getLongExtra("duration", 0);
                Message message = new UGCMessage(videoPath, coverPath, duration);
                presenter.sendMessage(message.getMessage());
            }
        }
    }

    /**
     * 预览要发送的图片
     *
     * @param path
     */
    private void showImagePreview(String path) {
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    /**
     * 发送文件
     *
     * @param path
     */
    private void sendFile(String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.exists()) {
            if (file.length() > 1024 * 1024 * 10) {
                TabToast.makeText("文件过大,发送失败");
            } else {
                Message message = new FileMessage(path);
                presenter.sendMessage(message.getMessage());
            }
        } else {
            TabToast.makeText("文件不存在,发送失败");
        }
    }

    /**
     * 将标题设置为对象名称
     */
    private Runnable resetTitle = new Runnable() {
        @Override
        public void run() {
            setRTitle(titleStr);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                switch (type) {
                    //个人聊天
                    case C2C:

                        for (int i = 0; i < MyApp.friendsbeans.size(); i++) {
                            if (identify.equals(MyApp.friendsbeans.get(i).getFriend_id())) {
                                FriendInfo_Activity.start(this, identify);
                                return;
                            }
                        }
                        UserInfo_Activity.start(this, identify);

                        break;
                    //群聊
                    case Group:
                        //TODO:GroupProfileActivity
                        TabToast.makeText("GroupProfileActivity");
                        LogUtil.e(TAG, "GroupProfileActivity");
//                        Intent intent = new Intent(ChatActivity.this, GroupProfileActivity.class);
//                        intent.putExtra("identify", identify);
//                        startActivity(intent);

                        break;
                }
                break;
        }
    }
}

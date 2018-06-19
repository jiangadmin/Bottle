package com.sy.bottle.activity.mian.friend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.dialog.ListPicker_Dialog;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.model.FriendshipInfo;
import com.sy.bottle.presenter.FriendshipManagerPresenter;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.view.LineControllerView;
import com.sy.bottle.view.TabToast;
import com.sy.bottle.viewfeatures.FriendshipManageView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;

import java.util.Collections;
import java.util.List;

/**
 * TODO:申请添加好友界面
 */
public class AddFriend_Activity extends Base_Activity implements View.OnClickListener, FriendshipManageView {
    private static final String TAG = "AddFriendActivity";

    private TextView tvName;
    private EditText editRemark, editMessage;
    private LineControllerView idField, groupField;
    private FriendshipManagerPresenter presenter;
    private String id,name;
    Button btnAdd,chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        setBack(true);
        setTitle("添加好友");

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        tvName = findViewById(R.id.name);
        idField = findViewById(R.id.id);
        chat = findViewById(R.id.add_friend_chat_btn);
        groupField = findViewById(R.id.group);
        btnAdd = findViewById(R.id.btnAdd);
        editMessage = findViewById(R.id.editMessage);
        editRemark = findViewById(R.id.editNickname);

        idField.setContent(id);
        tvName.setText(name);

        btnAdd.setOnClickListener(this);
        chat.setOnClickListener(this);

        presenter = new FriendshipManagerPresenter(this);



    }

    /**
     * 查询数据返回
     * @param bean
     */
    public void CallBack(UserInfo_Entity.DataBean bean){

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnAdd:
                presenter.addFriend(id, editRemark.getText().toString(), groupField.getContent().equals("默认分组") ? "" : groupField.getContent(), editMessage.getText().toString());
                break;

            case R.id.group:
                final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
                for (int i = 0; i < groups.length; ++i) {
                    if (groups[i].equals("")) {
                        groups[i] = "默认分组";
                        break;
                    }
                }

                new ListPicker_Dialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        groupField.setContent(groups[which]);
                    }
                });
                break;

            case R.id.add_friend_chat_btn:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("identify", id);
                intent.putExtra("type", TIMConversationType.C2C);
                startActivity(intent);
                MyApp.finishActivity();
                break;

        }

    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_ADD_FRIEND_STATUS_PENDING:
                TabToast.makeText("请求已发送");
                MyApp.finishActivity();
                break;
            case TIM_FRIEND_STATUS_SUCC:
                TabToast.makeText("已添加好友");
                MyApp.finishActivity();
                break;
            case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                TabToast.makeText("对方拒绝添加任何人为好友");
                MyApp.finishActivity();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                TabToast.makeText("您在对方的黑名单中");
                MyApp.finishActivity();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                Base_Dialog base_dialog = new Base_Dialog(this);
                base_dialog.setMessage("添加失败，该用户在你的黑名单中，是否将该用户从黑名单中移除（你需要在移除成功后重发添加好友请求）");
                base_dialog.setEsc("取消", null);
                base_dialog.setOk("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendshipManagerPresenter.delBlackList(Collections.singletonList(id), new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                TabToast.makeText("移除黑名单失败");
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                                TabToast.makeText("移除黑名单成功");
                            }
                        });
                    }
                });

                break;
            default:
                TabToast.makeText("添加好友失败，请稍后重试");
                break;
        }

    }

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {

    }

    /**
     * 修改好友分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {

    }

}

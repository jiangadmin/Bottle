package com.sy.bottle.activity.mian.friend;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.Edit_Activity;
import com.sy.bottle.activity.mian.chat.ChatActivity;
import com.sy.bottle.activity.mian.mine.Mine_Info_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.ShowImage_Dialog;
import com.sy.bottle.entity.Banner_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Photos_Entity;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.event.FriendshipEvent;
import com.sy.bottle.model.FriendshipInfo;
import com.sy.bottle.presenter.FriendshipManagerPresenter;
import com.sy.bottle.servlet.Photos_Get_Servlet;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.CircleImageView;
import com.sy.bottle.view.ImageCycleView;
import com.sy.bottle.view.LineControllerView;
import com.sy.bottle.view.TabToast;
import com.sy.bottle.viewfeatures.FriendshipManageView;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFriendStatus;
import com.tencent.imsdk.ext.sns.TIMFriendshipManagerExt;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO:好友资料
 */

public class Profile_Activity extends Base_Activity implements FriendshipManageView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "Profile_Activity";
    private static final String IDentify = "identify";

    private final int CHANGE_CATEGORY_CODE = 100;
    private final int CHANGE_REMARK_CODE = 200;

    private FriendshipManagerPresenter friendshipManagerPresenter;
    private String identify, categoryStr;

    TextView name, sign;
    LineControllerView id, remark, category, black;
    ImageCycleView photos;
    CircleImageView head;

    public static void start(Context context, String identify) {
        Intent intent = new Intent(context, Profile_Activity.class);
        intent.putExtra(IDentify, identify);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("详细资料");
        setMenu("投诉");
        setBack(true);

        initview();

        identify = getIntent().getStringExtra(IDentify);
        friendshipManagerPresenter = new FriendshipManagerPresenter(this);
        showProfile(identify);

        //获取用户信息
        new UserInfo_Servlet(this).execute(identify);

        //获取照片墙
        new Photos_Get_Servlet(this).execute(identify);
    }

    private void initview() {
        photos = findViewById(R.id.user_info_photos);
        head = findViewById(R.id.user_info_avatar);
        name = findViewById(R.id.user_info_name);
        sign = findViewById(R.id.user_info_sign);
        id = findViewById(R.id.user_info_id);
        remark = findViewById(R.id.user_info_remark);
        category = findViewById(R.id.user_info_group);
        black = findViewById(R.id.user_info_blackList);

        black.setCheckListener(this);

    }

    /**
     * 用户信息
     *
     * @param bean
     */
    public void CallBack(UserInfo_Entity.DataBean bean) {
        PicassoUtlis.img(Const.IMG + bean.getAvatar(), head);
        sign.setText(bean.getSign());
    }

    List<Banner_Entity.DBean> dBeans = new ArrayList<>();
    /**
     * 照片返回
     *
     * @param dataBeans
     */
    public void CallBack_Photos(List<Photos_Entity.DataBean> dataBeans) {
        dataBeans.clear();
        for (Photos_Entity.DataBean bean : dataBeans) {
            Banner_Entity.DBean dBean = new Banner_Entity.DBean();
            dBean.setPicUrl(bean.getPic_url());
            dBean.setId(bean.getId());
            dBeans.add(dBean);
        }

        photos.setBeans(dBeans, new ImageCycleView.Listener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                PicassoUtlis.img(imageURL, imageView);
            }

            @Override
            public void onImageClick(Banner_Entity.DBean bean, View imageView) {
                new ShowImage_Dialog(Profile_Activity.this, bean.getPicUrl()).show();
            }
        });

    }

    /**
     * 显示用户信息
     *
     * @param identify
     */
    public void showProfile(String identify) {

        List<String> users = new ArrayList<>();
        users.add(identify);
        TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {

                if (timUserProfiles.size() > 0) {
                    //头像
                    PicassoUtlis.img(timUserProfiles.get(0).getFaceUrl(), head);
                    //照片墙
                    //昵称
                    name.setText(timUserProfiles.get(0).getNickName());
                    //账号
                    id.setContent(timUserProfiles.get(0).getIdentifier());
                    //备注名
                    remark.setContent(timUserProfiles.get(0).getRemark());
                    //所在分组名
                    category.setContent(timUserProfiles.get(0).getFriendGroups().size() == 0 ? "默认分组" : timUserProfiles.get(0).getFriendGroups().get(0));

                }
            }
        });

        final TIMUserProfile profile = FriendshipInfo.getInstance().getProfile(identify);
        LogUtil.d(TAG, "show profile isFriend " + (profile != null));
        if (profile == null) return;

        //备注名
        remark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Edit_Activity.navToEdit(Profile_Activity.this, "修改备注", remark.getContent(), CHANGE_REMARK_CODE, new Edit_Activity.EditInterface() {
                    @Override
                    public void onEdit(String text, TIMCallBack callBack) {
                        FriendshipManagerPresenter.setRemarkName(profile.getIdentifier(), text, callBack);
                    }
                }, 20);

            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            List Uid = new ArrayList();
            Uid.add(identify);
            //添加到黑名单
            TIMFriendshipManagerExt.getInstance().addBlackList(Uid, new TIMValueCallBack<List<TIMFriendResult>>() {
                @Override
                public void onError(int i, String s) {
                    LogUtil.e(TAG, "add black list error " + s);
                }

                @Override
                public void onSuccess(List<TIMFriendResult> timFriendResults) {
                    if (timFriendResults.get(0).getStatus() == TIMFriendStatus.TIM_FRIEND_STATUS_SUCC) {
                        TabToast.makeText("加入黑名单成功");
                        MyApp.finishActivity();
                    }
                }
            });
        }
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                TabToast.makeText("投诉");
                break;
            case R.id.user_info_btnChat:
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("identify", identify);
                intent.putExtra("type", TIMConversationType.C2C);
                startActivity(intent);
                MyApp.finishActivity();
                break;
            case R.id.user_info_btnDel:
                friendshipManagerPresenter.delFriend(identify);
                break;
            case R.id.group:
//                final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
//                for (int i = 0; i < groups.length; ++i) {
//                    if (groups[i].equals("")) {
//                        groups[i] = getString(R.string.default_group_name);
//                        break;
//                    }
//                }
//                new ListPickerDialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (TextUtils.isEmpty(categoryStr)) return;
//                        if (groups[which].equals(categoryStr)) return;
//                        friendshipManagerPresenter.changeFriendGroup(identify,
//                                categoryStr.equals(getString(R.string.default_group_name)) ? null : categoryStr,
//                                groups[which].equals(getString(R.string.default_group_name)) ? null : groups[which]);
//                    }
//                });
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHANGE_CATEGORY_CODE) {
            if (resultCode == RESULT_OK) {
                LineControllerView category = findViewById(R.id.group);
                category.setContent(categoryStr = data.getStringExtra("category"));
            }
        } else if (requestCode == CHANGE_REMARK_CODE) {
            if (resultCode == RESULT_OK) {
                LineControllerView remark = findViewById(R.id.user_info_remark);
//                remark.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));

            }
        }

    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {

    }

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {
        switch (status) {
            case TIM_FRIEND_STATUS_SUCC:
                TabToast.makeText("删除好友成功");
                finish();
                break;
            case TIM_FRIEND_STATUS_UNKNOWN:
                TabToast.makeText("删除好友失败");
                break;
        }

    }

    /**
     * 修改好友分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {
        LineControllerView category = findViewById(R.id.group);
        if (groupName == null) {
            groupName = "默认分组";
        }
        switch (status) {
            case TIM_FRIEND_STATUS_UNKNOWN:
                TabToast.makeText("修改分组失败");
            case TIM_FRIEND_STATUS_SUCC:
                category.setContent(groupName);
                FriendshipEvent.getInstance().OnFriendGroupChange();
                break;
            default:
                TabToast.makeText("修改分组失败");
                category.setContent("默认分组");
                break;
        }
    }


}

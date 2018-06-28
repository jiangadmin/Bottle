package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.ClipImageActivity;
import com.sy.bottle.adapters.Photos_Adapter;
import com.sy.bottle.dialog.Base_Dialog;
import com.sy.bottle.dialog.ChooseCity_Dialog;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.Photos_Dialog;
import com.sy.bottle.dialog.ShowImage_Dialog;
import com.sy.bottle.entity.Photos_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.servlet.Head_Set_Servlet;
import com.sy.bottle.servlet.Photos_Del_Servlet;
import com.sy.bottle.servlet.Photos_Get_Servlet;
import com.sy.bottle.servlet.Photos_Set_Servlet;
import com.sy.bottle.servlet.Update_MineInfo_Servlet;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.ImageUtils;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.CircleImageView;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 编辑个人资料
 */

public class Edit_Mine_Info_Activity extends Base_Activity implements View.OnClickListener, Photos_Adapter.DelListenner, TIMCallBack {
    private static final String TAG = "Mine_Info_Activity";

    /**
     * 调用照相机返回图片临时文件
     */
    private File tempFile;
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

    private boolean isupdatehead = false;

    SwipeRefreshLayout sr;

    CircleImageView head;

    EditText nickname, sign;

    TextView city;

    GridView photos;

    Photos_Adapter photos_adapter;

    /**
     * 记录个人信息
     */
    UserInfo_Entity.DataBean dataBean;

    /**
     * 新省市区
     */
    public static String new_Province, new_City, new_Area;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Edit_Mine_Info_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mineinfo);

        createCameraTempFile(savedInstanceState);

        setBack(true);
        setTitle("编辑个人资料");
        setMenu("确定");

        initview();

        initinfo();
        initphotos();

    }


    /**
     * 创建调用系统照相机待存储的临时文件
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }


    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 加载个人信息
     */
    public void initinfo() {
        new UserInfo_Servlet(this).execute();
    }

    /**
     * 加载网络照片墙
     */
    public void initphotos() {
        new Photos_Get_Servlet(this).execute(SaveUtils.getString(Save_Key.UID));
    }

    /**
     * 加载个人信息返回
     *
     * @param dataBean
     */
    public void CallBack_Info(UserInfo_Entity.DataBean dataBean) {
        this.dataBean = dataBean;


        PicassoUtlis.img(dataBean.getAvatar(), head, R.drawable.head_me);

        nickname.setText(dataBean.getNikename());
        sign.setText(dataBean.getSign());
        city.setText(dataBean.getProvince() + "-" + dataBean.getCity() + "-" + dataBean.getArea());
    }

    /**
     * 加载网络图片返回
     *
     * @param dataBeans
     */
    public void CallBack_Photos(List<Photos_Entity.DataBean> dataBeans) {

        //当没有照片的时候
        if (dataBeans == null) {
            dataBeans = new ArrayList<>();
        }

        //当照片不足六个的时候
        if (dataBeans.size() < 6) {
            Photos_Entity.DataBean bean = new Photos_Entity.DataBean();
            bean.setId(-1);
            bean.setPic_url(null);
            dataBeans.add(bean);
        }

        photos.setAdapter(photos_adapter);
        photos_adapter.setPhotos_entity(dataBeans);
        photos_adapter.notifyDataSetChanged();

    }

    private void initview() {
        sr = findViewById(R.id.mine_info_sr);

        head = findViewById(R.id.mine_info_head);
        nickname = findViewById(R.id.mine_info_nickname);
        sign = findViewById(R.id.mine_info_sign);
        city = findViewById(R.id.mine_info_city);
        photos = findViewById(R.id.mine_info_photos);

        sr.setEnabled(false);

        photos_adapter = new Photos_Adapter(this, this);

        head.setOnClickListener(this);
        city.setOnClickListener(this);

    }

    String new_nickname;
    String new_sign;
    String new_city;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                new_nickname = nickname.getText().toString();
                new_sign = sign.getText().toString();
                new_city = city.getText().toString();

                if (TextUtils.isEmpty(new_nickname)) {
                    TabToast.makeText("昵称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(new_sign)) {
                    TabToast.makeText("昵称不能为空");
                    return;
                }

                if (new_nickname.equals(dataBean.getNikename())
                        && new_sign.equals(dataBean.getSign())
                        && new_city.equals(dataBean.getProvince() + "-" + dataBean.getCity() + "-" + dataBean.getArea())) {

                    TabToast.makeText("未做任何修改");
                    return;

                } else {

                    Loading.show(this, "修改中");

                    if (!TextUtils.isEmpty(new_Area)) {
                        TIMFriendshipManager.ModifyUserProfileParam userProfileParam = new TIMFriendshipManager.ModifyUserProfileParam();
                        userProfileParam.setNickname(new_nickname);
                        userProfileParam.setSelfSignature(new_sign);
                        userProfileParam.setLocation(new_Province + "-" + new_City + "-" + new_Area);

                        TIMFriendshipManager.getInstance().modifyProfile(userProfileParam, this);

                    } else {

                        TIMFriendshipManager.ModifyUserProfileParam userProfileParam = new TIMFriendshipManager.ModifyUserProfileParam();
                        userProfileParam.setNickname(new_nickname);
                        userProfileParam.setSelfSignature(new_sign);

                        TIMFriendshipManager.getInstance().modifyProfile(userProfileParam, this);

                    }

                }

                break;
            case R.id.mine_info_head:
                isupdatehead = true;
                new Photos_Dialog(this, tempFile);
                break;
            case R.id.mine_info_city:
                new ChooseCity_Dialog(this, city).show();
                break;
        }
    }

    @Override
    public void onSelected(final int id) {
        Base_Dialog base_dialog = new Base_Dialog(this);
        base_dialog.setTitle("确认删除？");
        base_dialog.setOk("确定", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Photos_Del_Servlet(Edit_Mine_Info_Activity.this).execute(String.valueOf(id));
            }
        });
        base_dialog.setEsc("取消", null);
    }

    @Override
    public void picurl(String url) {
        if (TextUtils.isEmpty(url)) {
            isupdatehead = false;
            new Photos_Dialog(this, tempFile);
        } else {
            new ShowImage_Dialog(this, url).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        LogUtil.e("evan", "requestCode:" + requestCode + "--resultCode:" + resultCode);
        switch (requestCode) {
            //调用系统相机返回
            case REQUEST_CAPTURE:
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            //调用系统相册返回
            case REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(intent.getData());
                }
                break;
            //剪切图片返回
            case REQUEST_CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();

                    if (uri == null) {
                        return;
                    }

                    Loading.show(this, "正在上传");
                    String file = ImageUtils.getRealFilePath(this, uri);

                    if (isupdatehead) {
                        new Head_Set_Servlet(this).execute(file);
                        head.setImageURI(uri);
                    } else {
                        new Photos_Set_Servlet(this).execute(file);
                    }

                }
                break;
        }
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", "1");
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    @Override
    public void onError(int i, String s) {
        Loading.dismiss();
        TabToast.makeText(s);
    }

    @Override
    public void onSuccess() {

        if (!TextUtils.isEmpty(new_Area)) {
            new Update_MineInfo_Servlet(this).execute(new_nickname, new_sign, new_Province, new_City, new_Area);

        } else {
            new Update_MineInfo_Servlet(this).execute(new_nickname, new_sign);

        }
    }
}

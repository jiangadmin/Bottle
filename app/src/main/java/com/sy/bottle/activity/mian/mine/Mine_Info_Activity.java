package com.sy.bottle.activity.mian.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.dialog.ShowImage_Dialog;
import com.sy.bottle.entity.Banner_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.entity.Photos_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.servlet.Photos_Get_Servlet;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.CircleImageView;
import com.sy.bottle.view.ImageCycleView;
import com.sy.bottle.view.LineControllerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 个人信息
 */
public class Mine_Info_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Mine_Info_Activity";

    TextView nickname, sign;
    ImageCycleView photos;
    CircleImageView head;
    LineControllerView id, sex, city, phone;

    List<Banner_Entity.DBean> dBeans = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Mine_Info_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineinfo);

        setBack(true);
        setTitle("个人信息");
        setMenu(R.drawable.ic_edit);

        initview();

    }

    @Override
    protected void onResume() {
        //获取个人照片墙
        new Photos_Get_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, SaveUtils.getString(Save_Key.UID));
        //获取个人信息
        new UserInfo_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        super.onResume();
    }

    private void initview() {
        photos = findViewById(R.id.mine_info_photos);
        head = findViewById(R.id.mine_info_head);
        nickname = findViewById(R.id.mine_info_nickname);
        id = findViewById(R.id.mine_info_id);
        sign = findViewById(R.id.mine_info_sign);
        sex = findViewById(R.id.mine_info_sex);
        city = findViewById(R.id.mine_info_city);
        phone = findViewById(R.id.mine_info_phone);

    }

    /**
     * 加载个人信息返回
     *
     * @param dataBean
     */
    public void CallBack_Info(UserInfo_Entity.DataBean dataBean) {
        nickname.setText(dataBean.getNikename());
        head.setImageURI(Uri.parse(dataBean.getAvatar()));

        if (dataBean.getAvatar().contains("http")) {
            PicassoUtlis.img(dataBean.getAvatar(), head, R.drawable.head_me);
        } else {
            PicassoUtlis.img(Const.IMG + dataBean.getAvatar(), head, R.drawable.head_me);
        }

        sign.setText(dataBean.getSign());
        id.setContent(SaveUtils.getString(Save_Key.UID));
        sex.setContent(dataBean.getSex().equals("1") ? "男" : "女");
        city.setContent(dataBean.getProvince() + "-" + dataBean.getCity() + "-" + dataBean.getArea());
        phone.setContent(dataBean.getPhone());
    }

    /**
     * 加载个人图片返回
     *
     * @param dataBeans
     */
    public void CallBack_Phtots(List<Photos_Entity.DataBean> dataBeans) {
        dBeans.clear();
        for (Photos_Entity.DataBean bean : dataBeans) {
            Banner_Entity.DBean dBean = new Banner_Entity.DBean();
            dBean.setPicUrl(bean.getPic_url());
            dBean.setId(bean.getId());
            dBeans.add(dBean);
        }

        photos.setBeans(dBeans, new ImageCycleView.Listener() {
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                Picasso.with(Mine_Info_Activity.this).load(imageURL).into(imageView);
            }

            @Override
            public void onImageClick(Banner_Entity.DBean bean, View imageView) {
                new ShowImage_Dialog(Mine_Info_Activity.this, bean.getPicUrl()).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                Edit_Mine_Info_Activity.start(this);
                break;
        }
    }
}

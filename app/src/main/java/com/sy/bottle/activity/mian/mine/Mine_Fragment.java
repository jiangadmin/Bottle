package com.sy.bottle.activity.mian.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Base_Fragment;
import com.sy.bottle.dialog.FriendShip_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.CircleImageView;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 我的
 */
public class Mine_Fragment extends Base_Fragment implements View.OnClickListener {
    private static final String TAG = "Mine_Fragment";

    LinearLayout mine_info;
    CircleImageView head;
    TextView name, id, xx, jf;
    ImageView sex;

    Button mine_mall, mine_setting, mine_blackList, mine_friend_Confirm,mine_log;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTitle(view, "我的");

        initview(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取个人信息
        new UserInfo_Servlet(this).execute();
    }

    private void initview(View view) {
        mine_info = view.findViewById(R.id.mine_info);
        head = view.findViewById(R.id.mine_head);
        name = view.findViewById(R.id.mine_nickname);
        id = view.findViewById(R.id.mine_id);
        sex = view.findViewById(R.id.mine_sex);
        mine_blackList = view.findViewById(R.id.mine_blackList);
        mine_friend_Confirm = view.findViewById(R.id.mine_friend_Confirm);
        mine_log = view.findViewById(R.id.mine_log);

        xx = view.findViewById(R.id.mine_xx);
        jf = view.findViewById(R.id.mine_jf);

        mine_mall = view.findViewById(R.id.mine_log);
        mine_setting = view.findViewById(R.id.mine_setting);

        mine_log.setOnClickListener(this);
        mine_mall.setOnClickListener(this);
        mine_info.setOnClickListener(this);
        mine_setting.setOnClickListener(this);
        mine_friend_Confirm.setOnClickListener(this);
        mine_blackList.setOnClickListener(this);
        xx.setOnClickListener(this);
        jf.setOnClickListener(this);

    }

    UserInfo_Entity.DataBean bean;

    /**
     * 个人信息回调
     *
     * @param bean
     */
    public void initeven(UserInfo_Entity.DataBean bean) {
        this.bean = bean;

        name.setText(bean.getNikename());

        if (bean.getAvatar().contains("http")) {
            PicassoUtlis.img(bean.getAvatar(), head, R.drawable.head_me);
        } else {
            PicassoUtlis.img(Const.IMG + bean.getAvatar(), head, R.drawable.head_me);
        }

        id.setText(SaveUtils.getString(Save_Key.UID));
        sex.setImageResource(bean.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);

        jf.setText("积分：" + bean.getScore() + " 分");
        xx.setText("星星：" + bean.getBalance() + " 个");

        SaveUtils.setInt(Save_Key.S_积分, bean.getScore());
        SaveUtils.setInt(Save_Key.S_星星, bean.getBalance());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_xx:
                MyBalance_Activity.start(getActivity());
                break;
            case R.id.mine_jf:

                break;
            case R.id.mine_log:
                Log_Activity.start(getActivity());
                break;
            case R.id.mine_info:
                Mine_Info_Activity.start(getActivity());
                break;
            case R.id.mine_friend_Confirm:
                new FriendShip_Dialog(getActivity());
                break;
            case R.id.mine_blackList:
                Black_Activity.start(getActivity());
                break;
            case R.id.mine_setting:
                Setting_Activity.start(getActivity());
                break;
        }
    }
}

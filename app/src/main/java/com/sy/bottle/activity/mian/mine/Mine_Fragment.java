package com.sy.bottle.activity.mian.mine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Base_Fragment;
import com.sy.bottle.activity.mian.other.Help_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.entity.UserInfo_Entity;
import com.sy.bottle.servlet.Notice_Servlet;
import com.sy.bottle.servlet.Scores_Get_Servlet;
import com.sy.bottle.servlet.UserInfo_Servlet;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.CircleImageView;
import com.sy.bottle.view.TabToast;

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

    TextView receive_num;
    Button receive, mine_mall, mine_log, selfhelp, news,task_agreement, setting;

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
        new UserInfo_Servlet(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        if (SaveUtils.getInt(Save_Key.S_捡星) != 0) {
            receive_num.setVisibility(View.VISIBLE);
            receive_num.setText(String.valueOf(SaveUtils.getInt(Save_Key.S_捡星)));
        } else {
            receive_num.setVisibility(View.GONE);
        }
    }

    private void initview(View view) {
        mine_info = view.findViewById(R.id.mine_info);
        head = view.findViewById(R.id.mine_head);
        name = view.findViewById(R.id.mine_nickname);
        id = view.findViewById(R.id.mine_id);
        sex = view.findViewById(R.id.mine_sex);

        receive = view.findViewById(R.id.mine_receive);
        receive_num = view.findViewById(R.id.mine_receive_num);
        mine_mall = view.findViewById(R.id.mine_mall);

        mine_log = view.findViewById(R.id.mine_log);
        selfhelp = view.findViewById(R.id.mine_selfhelp);

        news = view.findViewById(R.id.mine_news);
        setting = view.findViewById(R.id.mine_setting);
        task_agreement = view.findViewById(R.id.mine_taskagreement);

        xx = view.findViewById(R.id.mine_xx);
        jf = view.findViewById(R.id.mine_jf);

        mine_info.setOnClickListener(this);
        task_agreement.setOnClickListener(this);

        receive.setOnClickListener(this);
        mine_mall.setOnClickListener(this);

        mine_log.setOnClickListener(this);
        selfhelp.setOnClickListener(this);

        news.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    UserInfo_Entity.DataBean bean;

    /**
     * 个人信息回调
     *
     * @param bean
     */
    public void initeven(UserInfo_Entity.DataBean bean) {
        this.bean = bean;

        if (SaveUtils.getInt(Save_Key.S_捡星) != 0) {
            receive_num.setVisibility(View.VISIBLE);
            receive_num.setText(String.valueOf(SaveUtils.getInt(Save_Key.S_捡星)));
        } else {
            receive_num.setVisibility(View.GONE);
        }

        name.setText(bean.getNickname());

        Glide.with(this).load(bean.getAvatar()).apply(new RequestOptions().placeholder(R.drawable.head_me)).into(head);

        id.setText(SaveUtils.getString(Save_Key.UID));
        sex.setImageResource(bean.getSex().equals("1") ? R.drawable.ic_boy : R.drawable.ic_girl);

        jf.setText("积分：" + bean.getScore() + " 分");
        xx.setText("能量：" + bean.getBalance() + " 个");

        SaveUtils.setInt(Save_Key.S_积分, bean.getScore());
        SaveUtils.setInt(Save_Key.S_能量, bean.getBalance());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_receive:
                if (SaveUtils.getInt(Save_Key.S_捡星) != 0) {
                    new Scores_Get_Servlet().execute();
                } else {
                    TabToast.makeText("今日次数已用完,明天再领吧！");
                    receive_num.setVisibility(View.GONE);
                    return;
                }

                break;
            case R.id.mine_mall:
                MyBalance_Activity.start(getActivity());
                break;
            case R.id.mine_log:
                Log_Activity.start(getActivity());
                break;
            case R.id.mine_selfhelp:
                Help_Activity.start(getActivity());
                break;
            case R.id.mine_news:
                Loading.show(getActivity(), "请稍后");
                new Notice_Servlet(getActivity()).execute("notice_official");
                break;
            case R.id.mine_taskagreement:
                Loading.show(getActivity(), "请稍后");
                new Notice_Servlet(getActivity()).execute("bounty_task");
                break;
            case R.id.mine_info:
                Mine_Info_Activity.start(getActivity());
                break;
            case R.id.mine_setting:
                Setting_Activity.start(getActivity());
                break;
        }
    }
}

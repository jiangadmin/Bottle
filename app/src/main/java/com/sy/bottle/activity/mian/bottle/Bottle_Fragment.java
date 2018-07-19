package com.sy.bottle.activity.mian.bottle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Base_Fragment;
import com.sy.bottle.dialog.Bottle_Set_Dailog;
import com.sy.bottle.model.NoDoubleListener;
import com.sy.bottle.servlet.Bottle_Get_Servlet;
import com.sy.bottle.servlet.Scores_Get_Servlet;

/**
 * @author: jiangadmin
 * @date: 2018/5/26
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 瓶子
 */
public class Bottle_Fragment extends Base_Fragment implements View.OnClickListener {
    private static final String TAG = "Mine_Fragment";

    TextView bottle_set, bottle_get;
    ImageView bottle_balance, bottle_ranking;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bottle, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottle_get = view.findViewById(R.id.bottle_get);
        bottle_set = view.findViewById(R.id.bottle_set);
        bottle_balance = view.findViewById(R.id.bottle_balance);
        bottle_ranking = view.findViewById(R.id.bottle_ranking);

        bottle_get.setOnClickListener(new NoDoubleListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                new Bottle_Get_Servlet(getActivity()).execute();
            }
        });
        bottle_set.setOnClickListener(new NoDoubleListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                new Bottle_Set_Dailog(getActivity()).show();
            }
        });
        bottle_balance.setOnClickListener(this);
        bottle_ranking.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bottle_balance:
                new Scores_Get_Servlet().execute();
                break;
            case R.id.bottle_ranking:
                Ranking_Activity.start(getActivity());
                break;
        }
    }
}

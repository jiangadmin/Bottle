package com.sy.bottle.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Goods_Entity;
import com.sy.bottle.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: JiangAdmin
 * @date: 2018/6/19.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:商品列表
 */


public class Adapter_Goods extends android.widget.BaseAdapter {
    private static final String TAG = "Adapter_Goods";

    private Context context;
    private List<Goods_Entity.DataBean> listData = new ArrayList<>();

    Listenner listenner;

    public Adapter_Goods(Context context, Listenner listenner) {
        this.context = context;
        this.listenner = listenner;
    }

    public void setListData(List<Goods_Entity.DataBean> listData) {
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_goods, null);
            viewHolder.stars = convertView.findViewById(R.id.stars);
            viewHolder.view = convertView.findViewById(R.id.view);
            viewHolder.money = convertView.findViewById(R.id.money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Goods_Entity.DataBean bean = listData.get(position);

        viewHolder.stars.setText(bean.getStars());
        viewHolder.money.setText("¥ " + bean.getMoney());
        if (bean.isType()){
            viewHolder.money.setBackground(context.getResources().getDrawable(R.drawable.btn_style));
            viewHolder.money.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            viewHolder.money.setBackground(context.getResources().getDrawable(R.drawable.kuang_gray));
            viewHolder.money.setTextColor(context.getResources().getColor(R.color.gray_3));
        }

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenner.Good(bean);
            }
        });
        return convertView;
    }

    class ViewHolder {
        View view;
        TextView stars;
        Button money;
    }

    public interface Listenner {
        public void Good(Goods_Entity.DataBean bean);

    }


}

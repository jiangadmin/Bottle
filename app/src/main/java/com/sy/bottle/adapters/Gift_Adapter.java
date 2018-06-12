package com.sy.bottle.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Gift_Entity;
import com.sy.bottle.utils.PicassoUtlis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/2
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 礼物列表
 */
public class Gift_Adapter extends BaseAdapter {
    private static final String TAG = "Photos_Adapter";

    List<Gift_Entity.DataBean> dataBeans = new ArrayList<>();

    private Context context;

    public Gift_Adapter(Context context) {
        this.context = context;
    }

    public void setDataBeans(List<Gift_Entity.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_gift, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.item_gift_img);
            holder.name = convertView.findViewById(R.id.item_gift_name);
            holder.price = convertView.findViewById(R.id.item_gift_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gift_Entity.DataBean bean = dataBeans.get(position);

        PicassoUtlis.img(bean.getPic_url(), holder.img);
        holder.name.setText(bean.getName());
        holder.price.setText(String.valueOf(bean.getPrice()));

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name;
        TextView price;
    }


}

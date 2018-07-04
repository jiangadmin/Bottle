package com.sy.bottle.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Gift_Set_Log_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/2
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 赠送记录
 */
public class Gift_Set_Log_Adapter extends BaseAdapter {
    private static final String TAG = "Photos_Adapter";

    List<Gift_Set_Log_Entity.DataBean> dataBeans = new ArrayList<>();

    private Context context;

    public Gift_Set_Log_Adapter(Context context) {
        this.context = context;
    }

    public void setDataBeans(List<Gift_Set_Log_Entity.DataBean> dataBeans) {
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
            convertView = View.inflate(context, R.layout.item_gift_log, null);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.item_gift_name);
            holder.time = convertView.findViewById(R.id.item_gift_time);
            holder.price = convertView.findViewById(R.id.item_gift_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Gift_Set_Log_Entity.DataBean bean = dataBeans.get(position);

        holder.name.setText("赠送 " + bean.getName() + "给" + bean.getNickname());
        holder.price.setText(String.valueOf(bean.getPrice()));
        holder.time.setText(bean.getCreate_time());

        return convertView;
    }

    static class ViewHolder {
        TextView name, time, price;
    }


}

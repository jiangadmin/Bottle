package com.sy.bottle.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Put_Forward_Log_Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/2
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 提现记录
 */
public class Put_Forward_Log_Adapter extends BaseAdapter {
    private static final String TAG = "Photos_Adapter";

    List<Put_Forward_Log_Entity.DataBean> dataBeans = new ArrayList<>();

    private Context context;

    public Put_Forward_Log_Adapter(Context context) {
        this.context = context;
    }

    public void setDataBeans(List<Put_Forward_Log_Entity.DataBean> dataBeans) {
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
            convertView = View.inflate(context, R.layout.item_put_forward_log, null);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.title);
            holder.message = convertView.findViewById(R.id.message);
            holder.time = convertView.findViewById(R.id.time);
            holder.type = convertView.findViewById(R.id.type);
            holder.type_bg = convertView.findViewById(R.id.type_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Put_Forward_Log_Entity.DataBean bean = dataBeans.get(position);

        holder.title.setText("提取¥" + bean.getMoney() + "/" + bean.getPrice() + "能量至" + bean.getAccount());
        holder.time.setText(bean.getCreate_time());
        if (TextUtils.isEmpty(bean.getConnent())) {
            holder.message.setVisibility(View.GONE);
        } else {
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(bean.getConnent());
        }

        switch (bean.getStatusX()) {
            case 0:
                holder.type_bg.setImageResource(R.drawable.ic_bottom_right_red);
                holder.type.setText("进行中");
                break;
            case 1:
                holder.type_bg.setImageResource(R.drawable.ic_bottom_right_red);
                holder.type.setText("进行中");
                break;
            case 2:
                holder.type_bg.setImageResource(R.drawable.ic_bottom_right_red);
                holder.type.setText("进行中");
                break;
        }

        holder.time.setText(bean.getCreate_time());

        return convertView;
    }

    static class ViewHolder {
        TextView title, message, time, type;
        ImageView type_bg;
    }


}

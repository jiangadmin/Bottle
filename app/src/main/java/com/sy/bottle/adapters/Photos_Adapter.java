package com.sy.bottle.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.sy.bottle.R;
import com.sy.bottle.entity.Photos_Entity;
import com.sy.bottle.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/2
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 相册列表
 */
public class Photos_Adapter extends BaseAdapter {
    private static final String TAG = "Photos_Adapter";

    List<Photos_Entity.DataBean> dataBeans = new ArrayList<>();

    DelListenner delListenner;
    private Context context;

    public Photos_Adapter(DelListenner delListenner, Context context) {
        this.delListenner = delListenner;
        this.context = context;
    }

    public void setPhotos_entity(List<Photos_Entity.DataBean> dataBeans) {
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
            convertView = View.inflate(context, R.layout.item_photo, null);
            holder = new ViewHolder();
            holder.photo = convertView.findViewById(R.id.photo);
            holder.del = convertView.findViewById(R.id.photo_del);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Photos_Entity.DataBean bean = dataBeans.get(position);

        LogUtil.e(TAG,"ID："+bean.getId());
        LogUtil.e(TAG,"地址："+bean.getPic_url());

        if (bean.getPic_url() == null) {
            holder.del.setVisibility(View.GONE);
            holder.photo.setImageResource(R.mipmap.add_bg);
        } else {
            holder.del.setVisibility(View.VISIBLE);
            Picasso.with(context).load(bean.getPic_url()).into(holder.photo);
        }

        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delListenner.onSelected(bean.getId());
            }
        });

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delListenner.picurl(bean.getPic_url());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView photo;
        Button del;
    }

    public interface DelListenner {
        public void onSelected(int id);

        public void picurl(String url);
    }


}

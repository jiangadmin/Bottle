package com.sy.bottle.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Friends_Entity;
import com.sy.bottle.utils.PicassoUtlis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: JiangAdmin
 * @date: 2018/6/19.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:查找好友
 */

public class Adapter_Friends extends android.widget.BaseAdapter {
    private static final String TAG = "Adapter_Help_1";
    private Context context;
    private List<Friends_Entity.DataBean> listData = new ArrayList<>();

    public Adapter_Friends(Context context) {
        this.context = context;
    }

    public void setListData(List<Friends_Entity.DataBean> listData) {
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
            convertView = View.inflate(context, R.layout.item_profile_summary, null);
            viewHolder.avatar = convertView.findViewById(R.id.avatar);
            viewHolder.name = convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Friends_Entity.DataBean bean = listData.get(position);
        PicassoUtlis.img( bean.getAvatar(), viewHolder.avatar);
        viewHolder.name.setText(bean.getContent() == null ? bean.getNikename() : bean.getContent());

        return convertView;
    }

    class ViewHolder {
        ImageView avatar;
        TextView name;
    }
}

package com.sy.bottle.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sy.bottle.R;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.tencent.imsdk.TIMUserProfile;

import java.util.List;
import java.util.Map;

/**
 * 分组列表Adapters
 */
public class ExpandGroupListAdapter extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandGroupListAdapter";

    private Context mContext;
    private boolean selectable;

    private List<String> groups;
    private Map<String, List<TIMUserProfile>> mMembers;


    public ExpandGroupListAdapter(Context context, List<String> groups, Map<String, List<TIMUserProfile>> members) {
        this(context, groups, members, false);
    }

    public ExpandGroupListAdapter(Context context, List<String> groups, Map<String, List<TIMUserProfile>> members, boolean selectable) {
        mContext = context;
        this.groups = groups;
        mMembers = members;
        this.selectable = selectable;
    }


    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mMembers.get(groups.get(i)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mMembers.get(groups.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * 群组
     *
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param viewGroup
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
            groupHolder = new GroupHolder();
            groupHolder.groupname = convertView.findViewById(R.id.groupName);
            groupHolder.contentNum = convertView.findViewById(R.id.contentNum);
            groupHolder.tag = convertView.findViewById(R.id.groupTag);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupHolder.tag.setBackgroundResource(R.drawable.open);
        } else {
            groupHolder.tag.setBackgroundResource(R.drawable.close);
        }
        if (groups.get(groupPosition).equals("")) {
            groupHolder.groupname.setText("默认分组");
        } else {
            groupHolder.groupname.setText(groups.get(groupPosition));
        }
        groupHolder.contentNum.setText(String.valueOf(mMembers.get(groups.get(groupPosition)).size()) + "人");
        return convertView;
    }

    /**
     * 群组成员
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param viewGroup
     * @return
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup viewGroup) {
        ChildrenHolder itemHolder;
        if (convertView == null) {
            itemHolder = new ChildrenHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_childmember, null);
            itemHolder.tag = convertView.findViewById(R.id.chooseTag);
            itemHolder.name = convertView.findViewById(R.id.name);
            itemHolder.avatar = convertView.findViewById(R.id.avatar);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ChildrenHolder) convertView.getTag();
        }
        TIMUserProfile data = (TIMUserProfile) getChild(groupPosition, childPosition);
        if (!TextUtils.isEmpty(data.getNickName())) {
            itemHolder.name.setText(data.getNickName());
        }

        if (!TextUtils.isEmpty(data.getRemark())) {
            itemHolder.name.setText(data.getRemark());
        }
        itemHolder.tag.setVisibility(selectable ? View.VISIBLE : View.GONE);
        LogUtil.e(TAG, "头像：" + data.getFaceUrl());
        if (!TextUtils.isEmpty(data.getFaceUrl())) {
            PicassoUtlis.img(data.getFaceUrl(), itemHolder.avatar);
        }
//        itemHolder.tag.setImageResource(data.isSelected() ? R.drawable.selected : R.drawable.unselected);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    class GroupHolder {
        public TextView groupname;
        public TextView contentNum;
        public ImageView tag;
    }

    class ChildrenHolder {
        public TextView name;
        public ImageView avatar;
        public ImageView tag;
    }


}

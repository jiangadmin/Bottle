package com.sy.bottle.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sy.bottle.R;
import com.sy.bottle.model.FriendFuture;
import com.sy.bottle.presenter.FriendshipManagerPresenter;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.CircleImageView;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.sns.TIMFriendResult;
import com.tencent.imsdk.ext.sns.TIMFutureFriendType;

import java.util.List;

/**
 * 好友关系链管理消息adapter
 */
public class FriendManageMessageAdapter extends ArrayAdapter<FriendFuture> {
    private static final String TAG = "FriendManageMessageAdap";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;


    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public FriendManageMessageAdapter(Context context, int resource, List<FriendFuture> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.des = view.findViewById(R.id.description);
            viewHolder.status = view.findViewById(R.id.status);
            view.setTag(viewHolder);
        }
        Resources res = getContext().getResources();
        final FriendFuture data = getItem(position);
        LogUtil.e(TAG,data.getFaceUrl());
        if (!TextUtils.isEmpty(data.getFaceUrl())) {
            Glide.with(convertView).load(data.getFaceUrl()).into(viewHolder.avatar);
        } else {
            viewHolder.avatar.setImageResource(R.drawable.head_other);
        }
        viewHolder.name.setText(data.getName());
        viewHolder.des.setText(data.getMessage());
        viewHolder.status.setTextColor(res.getColor(R.color.gray_3));
        switch (data.getType()) {
            case TIM_FUTURE_FRIEND_PENDENCY_IN_TYPE:
                viewHolder.status.setText("同意");
                viewHolder.status.setTextColor(res.getColor(R.color.btn_blue));
                viewHolder.status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendshipManagerPresenter.acceptFriendRequest(data.getIdentify(), new TIMValueCallBack<TIMFriendResult>() {
                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onSuccess(TIMFriendResult timFriendResult) {
                                data.setType(TIMFutureFriendType.TIM_FUTURE_FRIEND_DECIDE_TYPE);
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
                break;
            case TIM_FUTURE_FRIEND_PENDENCY_OUT_TYPE:
                viewHolder.status.setText("等待验证");
                break;
            case TIM_FUTURE_FRIEND_DECIDE_TYPE:
                viewHolder.status.setText("已接受");
                break;
        }
        return view;
    }


    public class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView des;
        TextView status;
    }

}

package com.sy.bottle.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.model.Message;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.view.CircleImageView;

import java.util.List;

/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return view != null ? view.getId() : position;
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects) {
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
            viewHolder.leftMessage = view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = view.findViewById(R.id.rightPanel);
            viewHolder.leftAvatar = view.findViewById(R.id.leftAvatar);
            viewHolder.rightAvatar = view.findViewById(R.id.rightAvatar);
            viewHolder.sending = view.findViewById(R.id.sending);
            viewHolder.error = view.findViewById(R.id.sendError);
            viewHolder.sender = view.findViewById(R.id.sender);
            viewHolder.rightDesc = view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = view.findViewById(R.id.systemMessage);
            view.setTag(viewHolder);
        }
        if (position < getCount()) {
            final Message data = getItem(position);
            PicassoUtlis.img(SaveUtils.getString(Save_Key.S_头像), viewHolder.rightAvatar);
            data.showMessage(viewHolder, getContext());
        }
        return view;
    }

    public class ViewHolder {
        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public CircleImageView rightAvatar;
        public CircleImageView leftAvatar;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
    }
}

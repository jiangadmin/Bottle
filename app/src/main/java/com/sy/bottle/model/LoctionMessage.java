package com.sy.bottle.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.MapView;
import com.sy.bottle.R;
import com.sy.bottle.activity.mian.other.Map_Activity;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.utils.SaveUtils;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMMessage;

/**
 * 位置消息数据
 */
public class LoctionMessage extends Message {

    public LoctionMessage(TIMMessage message) {
        this.message = message;
    }

    public LoctionMessage(String s, double longitude, double latitude) {
        message = new TIMMessage();
        TIMLocationElem elem = new TIMLocationElem();
        elem.setDesc(s);
        elem.setLongitude(longitude);//经度
        elem.setLatitude(latitude);//维度
        message.addElement(elem);
    }

    /**
     * 在聊天界面显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context,int position) {
        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;

        viewHolder.rightMessage.setBackgroundResource(R.drawable.bg_bubble_blue);
        viewHolder.leftMessage.setBackgroundResource(R.drawable.bg_bubble_gray);

        String friendfaceurl = SaveUtils.getString(Save_Key.S_头像 + message.getSender());
        if (!TextUtils.isEmpty(friendfaceurl)) {
            PicassoUtlis.img(friendfaceurl, viewHolder.leftAvatar, R.drawable.head_other);
        }
        PicassoUtlis.img(SaveUtils.getString(Save_Key.S_头像), viewHolder.rightAvatar, R.drawable.head_me);

        final TIMLocationElem locationElem = (TIMLocationElem) message.getElement(0);

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = null;
        MapView mapView = null;

        Bitmap bitmap = null;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(480, 270);

//        try {
//            Activity activity = (Activity) context;
//            if (activity instanceof ChatActivity) {
//                mapView = new MapView(context);
//                mapView.setLayoutParams(params);
//                mapView.onCreate(((ChatActivity) activity).bundle);// 此方法必须重写
//                //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
//                AMap aMap = mapView.getMap();
//                aMap.getUiSettings().setZoomControlsEnabled(false);
//                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
//                //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
//                CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
//                        new CameraPosition(new LatLng(locationElem.getLatitude(), locationElem.getLongitude()), 17, 30, 0));
//                aMap.moveCamera(mCameraUpdate);
//                LatLng latLng = new LatLng(locationElem.getLatitude(), locationElem.getLongitude());
//                aMap.addMarker(new MarkerOptions().position(latLng).title(locationElem.getDesc()));
//
//                bitmap = mapView.getDrawingCache();
//
//            }
//
//        } catch (Exception e) {
        imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.icon_map);
        imageView.setLayoutParams(params);
//        }

        //标识
        TextView title = new TextView(context);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        title.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        title.setText("位置");

        //内容
        TextView address = new TextView(context);
        address.setLines(2);
        address.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        address.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        address.setText(locationElem.getDesc());

        if (imageView != null) {
            linearLayout.addView(imageView);
        }
        if (mapView != null) {
            linearLayout.addView(mapView);
        }

        linearLayout.addView(title);
        linearLayout.addView(address);

        getBubbleView(viewHolder).addView(linearLayout);

        showStatus(viewHolder);


        getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map_Activity.start(context, locationElem);
            }
        });
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        return "[位置]";

    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

}

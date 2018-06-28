package com.sy.bottle.activity.mian.other;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.utils.LogUtil;
import com.tencent.imsdk.TIMLocationElem;

/**
 * @author: jiangyao
 * @date: 2018/6/28
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:地图
 */
public class Map_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Map_Activity";

    double Longitude, Latitude;
    String Desc;
    static TIMLocationElem elem;
    MapView mapView;
    AMap aMap;

    public static void start(Context context, TIMLocationElem locationElem) {
        Intent intent = new Intent();
        elem = locationElem;
        intent.setClass(context, Map_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setTitle("位置");
        setBack(true);

        mapView = findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        if (elem == null) {
            setMenu("发送");
            //设置希望展示的地图缩放级别
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            myLocationStyle.strokeColor(0xffffffff);
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    LogUtil.e(TAG, location.getExtras().toString());
                    Longitude = location.getLongitude();
                    Latitude = location.getLatitude();
                    Desc = location.getExtras().get("Address").toString();

                }
            });
        } else {
            double Latitude = elem.getLatitude();
            double Longitude = elem.getLongitude();
            //参数依次是：视角调整区域的中心点坐标、希望调整到的缩放级别、俯仰角0°~45°（垂直与地图时为0）、偏航角 0~360° (正北方为0)
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                    new CameraPosition(new LatLng(Latitude, Longitude), 17, 30, 0));
            aMap.moveCamera(mCameraUpdate);
            LatLng latLng = new LatLng(Latitude, Longitude);
            final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(elem.getDesc()));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        elem = null;
        mapView.onDestroy();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                Intent intent = new Intent();
                intent.putExtra("Longitude", Longitude);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Desc", Desc);
                setResult(RESULT_OK, intent);
                MyApp.finishActivity();

                break;
        }
    }
}

package com.sy.bottle.dialog;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.view.wheel.ArrayWheelAdapter;
import com.sy.bottle.view.wheel.CityModel;
import com.sy.bottle.view.wheel.DistrictModel;
import com.sy.bottle.view.wheel.OnWheelChangedListener;
import com.sy.bottle.view.wheel.ProvinceModel;
import com.sy.bottle.view.wheel.WheelView;
import com.sy.bottle.view.wheel.XmlParserHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author: jiangadmin
 * @date: 2017/7/25.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 城市选择器
 */

public class ChooseCity_Dialog extends MyDialog implements OnWheelChangedListener, View.OnClickListener {
    private static final String TAG = "ChooseCity_Dialog";

    TextView esc, submit;

    WheelView province, city, district;

    Activity activity;

    TextView textView;

    public ChooseCity_Dialog(@NonNull Activity activity, TextView textView) {
        super(activity, R.style.myDialogTheme);
        this.activity = activity;
        this.textView = textView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_city);

        initview();
        setUpData();
    }

    private void initview() {

        esc = findViewById(R.id.tv_cancle);
        submit = findViewById(R.id.tv_select);

        province = findViewById(R.id.id_province);
        city = findViewById(R.id.id_city);
        district = findViewById(R.id.id_district);

        esc.setOnClickListener(this);
        submit.setOnClickListener(this);

        // 添加change事件
        province.addChangingListener(this);
        // 添加change事件
        city.addChangingListener(this);
        // 添加change事件
        district.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        province.setViewAdapter(new ArrayWheelAdapter<>(activity, mProvinceDatas));
        // 设置可见条目数量
        province.setVisibleItems(7);
        city.setVisibleItems(7);
        district.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * 所有省Code
     */
    protected String[] mProvinceCodeDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<>();

    /**
     * key - 区 values - 区域码
     */
    protected Map<String, String> mcodeDatasMap = new HashMap<>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";
    /**
     * 当前省编码
     */
    protected String mCurrentProviceCode = "";
    /**
     * 当前市编码
     */
    protected String mCurrentCityCode = "";
    /**
     * 当前区编码
     */
    protected String mCurrentDistrictCode = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";


    /**
     * 当前区的编号
     */
    protected String mAreacode = "";


    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = activity.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();
                mCurrentProviceCode = provinceList.get(0).getProvincecode();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                    mAreacode = districtList.get(0).getAreacode();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            mProvinceCodeDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                mProvinceCodeDatas[i] = provinceList.get(i).getProvincecode();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        mcodeDatasMap.put(cityList.get(j).getName() + districtList.get(k).getName(), districtList.get(k).getAreacode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == province) {
            updateCities();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
            mAreacode = mcodeDatasMap.get(mCurrentCityName + mCurrentDistrictName);
        } else if (wheel == city) {
            updateAreas();
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
//            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
            mAreacode = mcodeDatasMap.get(mCurrentCityName + mCurrentDistrictName);
        } else if (wheel == district) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
            mAreacode = mcodeDatasMap.get(mCurrentCityName + mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = province.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        mCurrentProviceCode = mProvinceCodeDatas[pCurrent];


        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        city.setViewAdapter(new ArrayWheelAdapter<>(activity, cities));
        city.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = city.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        district.setViewAdapter(new ArrayWheelAdapter<>(activity, areas));
        district.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select:

                //编辑个人资料xx
                if (activity instanceof Edit_Mine_Info_Activity){
                    ((Edit_Mine_Info_Activity) activity).new_Province = mCurrentProviceName;
                    ((Edit_Mine_Info_Activity) activity).new_City = mCurrentCityName;
                    ((Edit_Mine_Info_Activity) activity).new_Area = mCurrentDistrictName;
                }

                LogUtil.e(TAG, mAreacode);
                textView.setText(mCurrentProviceName + "-"+mCurrentCityName + "-" + mCurrentDistrictName);

                break;
        }
        dismiss();
    }
}

package com.sy.bottle.activity.start;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO 引导页
 */

public class Guide_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "GuideActivity";
    private ViewPager guideVp;
    private List<ImageView> listImgs = new ArrayList<>();
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private TextView preSelectTxt;
    private TextView currSelectTxt;
    private LinearLayout selectBottom;
    private TextView experTxt;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Guide_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initview();
        initeven();
        initDatas();
    }

    protected void initview() {
        guideVp = findViewById(R.id.guide_vp);
        selectBottom = findViewById(R.id.guide_select_bottom);
        experTxt = findViewById(R.id.guid_experience);
        preSelectTxt = (TextView) selectBottom.getChildAt(0);
        img1 = new ImageView(this);
        img2 = new ImageView(this);
        img3 = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        img1.setLayoutParams(params);
        img1.setScaleType(ImageView.ScaleType.FIT_XY);
        img1.setImageResource(R.mipmap.start_image);

        img2.setLayoutParams(params);
        img2.setScaleType(ImageView.ScaleType.FIT_XY);
        img2.setBackgroundResource(R.mipmap.start_image);

        img3.setLayoutParams(params);
        img3.setScaleType(ImageView.ScaleType.FIT_XY);
        img3.setBackgroundResource(R.mipmap.start_image);

        listImgs.add(img1);
        listImgs.add(img2);
        listImgs.add(img3);
    }

    protected void initDatas() {
        guideVp.setAdapter(new GuideAdapter());
    }

    protected void initeven() {
        guideVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                preSelectTxt.setBackgroundResource(R.drawable.guide_not_select);
                currSelectTxt = (TextView) selectBottom.getChildAt(position);
                currSelectTxt.setBackgroundResource(R.drawable.guide_selet);
                preSelectTxt = currSelectTxt;
                if (position == 2) {
                    experTxt.setVisibility(View.VISIBLE);
                } else {
                    experTxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        experTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guid_experience:
                Login_Activity.start(this);
                SaveUtils.setBoolean(Save_Key.S_跳过引导, true);
                MyApp.finishActivity();
                break;
        }
    }

    /*viewpage 适配器*/
    private class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listImgs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(listImgs.get(position));
            return listImgs.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(listImgs.get(position));
        }
    }
}

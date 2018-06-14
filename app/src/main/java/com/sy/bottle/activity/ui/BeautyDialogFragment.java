package com.sy.bottle.activity.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.TCUtils;

import java.util.ArrayList;


public class BeautyDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = BeautyDialogFragment.class.getSimpleName();

    public static final int BEAUTYPARAM_BEAUTY = 1;
    public static final int BEAUTYPARAM_WHITE = 2;
    public static final int BEAUTYPARAM_FACE_LIFT = 3;
    public static final int BEAUTYPARAM_BIG_EYE = 4;
    public static final int BEAUTYPARAM_FILTER = 5;
    public static final int BEAUTYPARAM_MOTION_TMPL = 6;

    static public class BeautyParams {
        public int mBeautyProgress = 6;
        public int mWhiteProgress = 3;
        public int mFaceLiftProgress;
        public int mBigEyeProgress;
        public int mFilterIdx;
        public String mMotionTmplPath;
    }

    public interface OnBeautyParamsChangeListener {
        void onBeautyParamsChange(BeautyParams params, int key);
    }

    private View mLayoutBeauty;
    private View mLayoutLookFilter;
    private View mLayoutPitu;

    private LinearLayout mBeautyLayout;
    private LinearLayout mWhitenLayout;
    private LinearLayout mFaceLiftLayout;
    private LinearLayout mBigEyeLayout;
    private SeekBar mBeautySeekbar;
    private SeekBar mFaceLiftSeekbar;
    private SeekBar mBigEyeSeekbar;
    private SeekBar mWhitenSeekbar;
    private TextView mTVBeauty;
    private TextView mTVFilter;
    private TextView mTVPitu;
    private TCHorizontalScrollView mFilterPicker;

    private ArrayList<Integer> mFilterIDList;
    private ArrayAdapter<Integer> mFilterAdapter;
    private BeautyParams mBeautyParams;
    private OnBeautyParamsChangeListener mBeautyParamsChangeListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_beauty_area);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        LogUtil.d(TAG, "create fragment");
        mBeautyLayout = dialog.findViewById(R.id.layoutBeauty);
        mWhitenLayout = dialog.findViewById(R.id.layoutWhiten);
        mFaceLiftLayout = dialog.findViewById(R.id.layoutFacelift);
        mBigEyeLayout = dialog.findViewById(R.id.layoutBigEye);
        mFilterPicker = dialog.findViewById(R.id.filterPicker);
        mTVPitu = dialog.findViewById(R.id.tv_dynamic_effect);
        mTVPitu.setSelected(false);
        mLayoutPitu = dialog.findViewById(R.id.material_recycler_view);
        mLayoutBeauty = dialog.findViewById(R.id.layoutFaceBeauty);
        mLayoutLookFilter = mFilterPicker;

        mLayoutLookFilter.setVisibility(View.GONE);
        mLayoutPitu.setVisibility(View.GONE);

        mBeautyLayout.setVisibility(View.VISIBLE);
        mWhitenLayout.setVisibility(View.VISIBLE);
        mFaceLiftLayout.setVisibility(View.GONE);
        mBigEyeLayout.setVisibility(View.GONE);

        mBeautySeekbar = (SeekBar) dialog.findViewById(R.id.beauty_seekbar);
        mBeautySeekbar.setOnSeekBarChangeListener(this);
        mBeautySeekbar.setProgress(mBeautyParams.mBeautyProgress * mBeautySeekbar.getMax() / 9);

        mWhitenSeekbar = (SeekBar) dialog.findViewById(R.id.whiten_seekbar);
        mWhitenSeekbar.setOnSeekBarChangeListener(this);
        mWhitenSeekbar.setProgress(mBeautyParams.mWhiteProgress * mWhitenSeekbar.getMax() / 9);

        mFaceLiftSeekbar = (SeekBar) dialog.findViewById(R.id.facelift_seekbar);
        mFaceLiftSeekbar.setOnSeekBarChangeListener(this);
        mFaceLiftSeekbar.setProgress(mBeautyParams.mFaceLiftProgress * mFaceLiftSeekbar.getMax() / 9);

        mBigEyeSeekbar = dialog.findViewById(R.id.bigeye_seekbar);
        mBigEyeSeekbar.setOnSeekBarChangeListener(this);
        mBigEyeSeekbar.setProgress(mBeautyParams.mBigEyeProgress * mBigEyeSeekbar.getMax() / 9);

        mFilterIDList = new ArrayList<>();
        mFilterIDList.add(R.mipmap.orginal);
        mFilterIDList.add(R.mipmap.langman);
        mFilterIDList.add(R.mipmap.qingxin);
        mFilterIDList.add(R.mipmap.weimei);
        mFilterIDList.add(R.mipmap.fennen);
        mFilterIDList.add(R.mipmap.huaijiu);
        mFilterIDList.add(R.mipmap.landiao);
        mFilterIDList.add(R.mipmap.qingliang);
        mFilterIDList.add(R.mipmap.rixi);
        mFilterAdapter = new ArrayAdapter<Integer>(dialog.getContext(), 0, mFilterIDList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.filter_layout, null);
                }
                ImageView view = convertView.findViewById(R.id.filter_image);
                if (position == 0) {
                    ImageView view_tint = convertView.findViewById(R.id.filter_image_tint);
                    if (view_tint != null)
                        view_tint.setVisibility(View.VISIBLE);
                }
                view.setTag(position);
                view.setImageDrawable(getResources().getDrawable(getItem(position)));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        mBeautyParams.mFilterIdx = index;
                        selectFilter(mBeautyParams.mFilterIdx);
                        if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_FILTER);
                        }
                    }
                });
                return convertView;

            }
        };
        mFilterPicker.setAdapter(mFilterAdapter);
        if (mBeautyParams.mFilterIdx >= 0 && mBeautyParams.mFilterIdx < mFilterAdapter.getCount()) {
            mFilterPicker.setClicked(mBeautyParams.mFilterIdx);
            selectFilter(mBeautyParams.mFilterIdx);
        } else {
            mFilterPicker.setClicked(0);
        }


        mTVBeauty = dialog.findViewById(R.id.tv_face_beauty);
        mTVFilter = dialog.findViewById(R.id.tv_face_filter);
        mTVBeauty.setSelected(true);
        mTVFilter.setSelected(false);

        mTVBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(true);
                mTVFilter.setSelected(false);
                mTVPitu.setSelected(false);

                mLayoutBeauty.setVisibility(View.VISIBLE);
                mLayoutLookFilter.setVisibility(View.GONE);
                mLayoutPitu.setVisibility(View.GONE);

                mBeautySeekbar.setProgress(mBeautyParams.mBeautyProgress * mBeautySeekbar.getMax() / 9);
                mWhitenSeekbar.setProgress(mBeautyParams.mWhiteProgress * mWhitenSeekbar.getMax() / 9);
//                mFaceLiftSeekbar.setProgress(mBeautyParams.mFaceLiftProgress * mFaceLiftSeekbar.getMax() / 9);
            }
        });

        mTVFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(false);
                mTVFilter.setSelected(true);
                mTVPitu.setSelected(false);

                mLayoutBeauty.setVisibility(View.GONE);
                mLayoutLookFilter.setVisibility(View.VISIBLE);
                mLayoutPitu.setVisibility(View.GONE);
            }
        });

        mTVPitu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTVBeauty.setSelected(false);
                mTVFilter.setSelected(false);
                mTVPitu.setSelected(true);

                mLayoutBeauty.setVisibility(View.GONE);
                mLayoutLookFilter.setVisibility(View.GONE);
                mLayoutPitu.setVisibility(View.VISIBLE);
            }
        });


        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        //pitu
        return dialog;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.beauty_seekbar:
                mBeautyParams.mBeautyProgress = TCUtils.filtNumber(9, mBeautySeekbar.getMax(), progress);
                if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                    mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BEAUTY);
                }
                break;
            case R.id.whiten_seekbar:
                mBeautyParams.mWhiteProgress = TCUtils.filtNumber(9, mWhitenSeekbar.getMax(), progress);
                if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
                    mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_WHITE);
                }
                break;
            case R.id.facelift_seekbar:
                mBeautyParams.mFaceLiftProgress = progress;
                break;
            case R.id.bigeye_seekbar:
                mBeautyParams.mBigEyeProgress = progress;
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void selectFilter(int index) {
        ViewGroup group = (ViewGroup) mFilterPicker.getChildAt(0);
        for (int i = 0; i < mFilterAdapter.getCount(); i++) {
            View v = group.getChildAt(i);
            ImageView IVTint = v.findViewById(R.id.filter_image_tint);
            if (IVTint != null) {
                if (i == index) {
                    IVTint.setVisibility(View.VISIBLE);
                } else {
                    IVTint.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public void setBeautyParamsListner(BeautyParams params, OnBeautyParamsChangeListener listener) {
        mBeautyParams = params;
        mBeautyParamsChangeListener = listener;
        //当BeautyDialogFragment重置时，先刷新一遍配置
        if (mBeautyParamsChangeListener instanceof OnBeautyParamsChangeListener) {
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_BEAUTY);
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_WHITE);
            mBeautyParamsChangeListener.onBeautyParamsChange(mBeautyParams, BEAUTYPARAM_MOTION_TMPL);
        }
    }
}
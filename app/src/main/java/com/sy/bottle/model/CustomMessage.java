package com.sy.bottle.model;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {
    private static final String TAG = "CustomMessage";

    private final int TYPE_TYPING = 14;
    private final int TYPE_GIFT = 16;

    private Type type;
    private String desc;
    private String data;

    public CustomMessage(TIMMessage message) {
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        parse(elem.getData());

    }

    Map gift;

    public CustomMessage(Type type, Map gift) {
        message = new TIMMessage();

        String data = "";
        JSONObject dataJson = new JSONObject();
        try {
            switch (type) {
                case TYPING:
                    this.gift = null;
                    dataJson.put("userAction", TYPE_TYPING);
                    dataJson.put("actionParam", "EIMAMSG_InputStatus_Ing");
                    data = dataJson.toString();
                    break;
                case GIFT:
                    this.gift = gift;
                    dataJson.put("userAction", TYPE_GIFT);
                    dataJson.put("actionParam", gift.toString());
                    data = dataJson.toString();
                    break;
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, "generate json error");
        }
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data.getBytes());
        message.addElement(elem);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void parse(byte[] data) {
        type = Type.INVALID;
        try {
            String str = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(str);

            int action = jsonObj.getInt("userAction");
            switch (action) {
                case TYPE_TYPING:
                    LogUtil.e(TAG, "TYPE_TYPING");
                    type = Type.TYPING;
                    this.data = jsonObj.getString("actionParam");
                    if (this.data.equals("EIMAMSG_InputStatus_End")) {
                        type = Type.INVALID;
                    }
                    break;
                case TYPE_GIFT:
                    type = Type.GIFT;

                    break;
            }

        } catch (IOException | JSONException e) {
            LogUtil.e(TAG, "parse json error");

        }
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.rightMessage.setBackground(null);
        viewHolder.leftMessage.setBackground(null);
        LogUtil.e(TAG, "自定义消息显示");
        //如果是礼物信息
        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;

        LogUtil.e(TAG, "进入");
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);

        LogUtil.e(TAG, new String(elem.getData()));

        try {
            JSONObject jsonObj = new JSONObject(new String(elem.getData()));
            String actionParam = jsonObj.getString("actionParam");

            actionParam = actionParam.replace("{", "{\"");
            actionParam = actionParam.replace("=", "\":\"");
            actionParam = actionParam.replace(",", "\",\"");
            actionParam = actionParam.replace("}", "\"}");

            actionParam = actionParam.replaceAll(" ", "");

            LogUtil.e(TAG, "转换后的数据：" + actionParam);

            JSONObject gift = new JSONObject(actionParam);


            final String gifturl = gift.getString("Pic_url");
            LogUtil.e(TAG, "图片地址：" + gift.getString("Pic_url"));

            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(context);
            PicassoUtlis.img(gifturl, imageView);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(400, 400);//两个400分别为添加图片的大小
            imageView.setLayoutParams(params);

            TextView tv = new TextView(MyApp.getInstance());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tv.setTextColor(MyApp.getInstance().getResources().getColor(R.color.style_color));
//            tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
            tv.setText("【礼物】" + gift.getString("Name"));


            clearView(viewHolder);

            getBubbleView(viewHolder).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.e(TAG, gifturl);

                }
            });
            linearLayout.addView(imageView);
            linearLayout.addView(tv);

            getBubbleView(viewHolder).addView(linearLayout);
            showStatus(viewHolder);

            LogUtil.e(TAG, "展示图片");
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            TextView tv = new TextView(MyApp.getInstance());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
            tv.setText("【礼物】");

            clearView(viewHolder);

            getBubbleView(viewHolder).addView(tv);
            showStatus(viewHolder);

            LogUtil.e(TAG, "展示描述");
        }

    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        return "[礼物]";

    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    public enum Type {
        TYPING,
        INVALID,
        GIFT,
    }


}

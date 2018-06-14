package com.sy.bottle.model;

import android.content.Context;
import android.widget.ImageView;

import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {

    private String TAG = getClass().getSimpleName();

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
                    LogUtil.e(TAG,"TYPE_TYPING");
                    type = Type.TYPING;
                    this.data = jsonObj.getString("actionParam");
                    if (this.data.equals("EIMAMSG_InputStatus_End")) {
                        type = Type.INVALID;
                    }
                    break;
                case TYPE_GIFT:
                    LogUtil.e(TAG,"TYPE_GIFT");
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
        //如果是礼物信息
        if (gift != null) {
            clearView(viewHolder);
            if (checkRevoke(viewHolder)) return;

            for (int i = 0; i < message.getElementCount(); ++i) {

                LogUtil.e(TAG,message.getElement(i).getType().toString());
//                switch (message.getElement(i).getType()) {
//                    case Face:
//                        TIMCustomElem faceElem = (TIMCustomElem) message.getElement(i);
//                        byte[] data = faceElem.getData();
//                        if (data != null) {
//                            result.append(new String(data, Charset.forName("UTF-8")));
//                        }
//                        break;
//                    case Text:
//                        TIMTextElem textElem = (TIMTextElem) message.getElement(i);
//                        result.append(textElem.getText());
//                        break;
//                }

            }

            ImageView imageView = new ImageView(MyApp.getInstance());
            PicassoUtlis.Cornersimg(String.valueOf(gift.get("Pic_url")), imageView);
            clearView(viewHolder);
            getBubbleView(viewHolder).addView(imageView);

            showStatus(viewHolder);
        }
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        if (gift != null) {
            return "[礼物]" + gift.get("Name");
        } else {
            return null;
        }

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

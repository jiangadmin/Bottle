package com.sy.bottle.model;

import android.content.Context;
import android.os.Environment;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sy.bottle.R;
import com.sy.bottle.adapters.ChatAdapter;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.entity.Gift_Entity;
import com.sy.bottle.utils.FileUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.PicassoUtlis;
import com.sy.bottle.view.TabToast;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMMessage;

/**
 * 文件消息
 */
public class FileMessage extends Message {

    public FileMessage(TIMMessage message) {
        this.message = message;
    }

    public FileMessage(String filePath) {
        message = new TIMMessage();
        TIMFileElem elem = new TIMFileElem();
        elem.setPath(filePath);
        elem.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
        message.addElement(elem);
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.rightMessage.setBackgroundResource(R.drawable.bg_bubble_blue);
        viewHolder.leftMessage.setBackgroundResource(R.drawable.bg_bubble_gray);

        clearView(viewHolder);
        if (checkRevoke(viewHolder)) return;
        TIMFileElem e = (TIMFileElem) message.getElement(0);
        TextView tv = new TextView(MyApp.getInstance());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextColor(MyApp.getInstance().getResources().getColor(isSelf() ? R.color.white : R.color.black));
        tv.setText(e.getFileName());

        ImageView imageView = new ImageView(MyApp.getInstance());
        Gift_Entity.DataBean bean = new Gson().fromJson(e.getPath(), Gift_Entity.DataBean.class);
        if (bean.getPic_url() != null)
            PicassoUtlis.Cornersimg(bean.getPic_url(), imageView);

        getBubbleView(viewHolder).addView(imageView);
        showStatus(viewHolder);
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return str;
        return "[文件]";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {
        if (message == null) return;
        final TIMFileElem e = (TIMFileElem) message.getElement(0);
        String[] str = e.getFileName().split("/");
        String filename = str[str.length - 1];
        if (FileUtil.isFileExist(filename, Environment.DIRECTORY_DOWNLOADS)) {
            TabToast.makeText("文件已存在");
            return;
        }

        e.getToFile(FileUtil.getCacheFilePath(filename), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
            }

            @Override
            public void onSuccess() {

            }
        });

    }
}

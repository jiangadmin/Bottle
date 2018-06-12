package com.sy.bottle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sy.bottle.R;
import com.sy.bottle.utils.LogUtil;

/**
 * @author: jiangadmin
 * @date: 2017/6/12.
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 等待框
 */

public class Loading {
    private static final String TAG = "Loading";

    private static LoadingDialog progressDialog;

    public static void show(Context context, String message) {
        if (progressDialog == null) {
            progressDialog = LoadingDialog.create(context, message);
        }
        progressDialog.setCancelable(true);
        try {
            progressDialog.show();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }

    public static void show(Context context, String message, boolean cancelabl) {
        if (progressDialog == null) {
            progressDialog = LoadingDialog.create(context, message);
        }
        progressDialog.setCancelable(cancelabl);
        try {
            progressDialog.show();
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }

    }

    public static void dismiss() {
        try {
            if (null != progressDialog) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }

    public static class LoadingDialog extends Dialog {
        public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
            super(context, themeResId);
        }

        /**
         * Create the custom dialog
         */
        public static LoadingDialog create(Context context, String message) {
            if (TextUtils.isEmpty(message)) {
                message = "加载中...";
            }
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.dialog_loading, null);
            TextView txtInfo = layout.findViewById(R.id.txt_info);
            txtInfo.setText(message);

            LoadingDialog dialog = new LoadingDialog(context, R.style.LoadingDialog);
            dialog.setCanceledOnTouchOutside(false);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            return dialog;
        }
    }
}

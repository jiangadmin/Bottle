package com.sy.bottle.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sy.bottle.R;
import com.sy.bottle.activity.mian.Main_Activity;
import com.sy.bottle.activity.mian.other.Success_Activity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.view.TabToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedHashMap;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Const.Wechat_AppID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode){
                case 0:
                    LinkedHashMap map = new LinkedHashMap();
                    map.put("message", "支付成功");
                    Main_Activity.UpdateMyInfo();
                    Success_Activity.start(map);

                    break;
                case -1:
                    TabToast.makeText("支付失败");
                    break;
                case -2:
                    TabToast.makeText("取消支付");
                    break;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(String.valueOf(resp.errCode));
            builder.show();
        }
    }
}
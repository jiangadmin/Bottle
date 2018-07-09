package com.sy.bottle.activity.mian.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Goods_Entity;
import com.sy.bottle.entity.Order_Entity;
import com.sy.bottle.entity.PayResult;
import com.sy.bottle.servlet.Order_Get_Servlet;
import com.sy.bottle.utils.OrderInfoUtil2_0;
import com.sy.bottle.utils.TimeUtil;
import com.sy.bottle.view.TabToast;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 充值
 */
public class Recharge_Activity extends Base_Activity implements View.OnClickListener {
    private static final String TAG = "Recharge_Activity";

    TextView balance, rmb, agreement;

    RelativeLayout alipay, wechat;

    ImageView alipay_type, wechat_type;

    CheckBox checkBox;

    Button submit;

    /**
     * 付款  1 微信 2 支付宝
     */
    int paytype = 2;
    static Goods_Entity.DataBean bean;

    public static void start(Context context, Goods_Entity.DataBean b) {
        Intent intent = new Intent();
        intent.setClass(context, Recharge_Activity.class);
        bean = b;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        setTitle("确认付款");

        setBack(true);

        initview();
    }

    private void initview() {
        balance = findViewById(R.id.recharge_balance);
        rmb = findViewById(R.id.recharge_rmb);
        agreement = findViewById(R.id.recharge_agreement);

        alipay = findViewById(R.id.recharge_alipay);
        wechat = findViewById(R.id.recharge_wechat);
        alipay_type = findViewById(R.id.recharge_alipay_type);
        wechat_type = findViewById(R.id.recharge_wechat_type);

        checkBox = findViewById(R.id.recharge_checkBox);

        submit = findViewById(R.id.recharge_submit);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                submit.setEnabled(b);
            }
        });

        alipay.setOnClickListener(this);
        wechat.setOnClickListener(this);
        submit.setOnClickListener(this);

        balance.setText(bean.getStars() + "=");
        rmb.setText(bean.getMoney() + "元");

        submit.setText("确认充值" + bean.getMoney() + "元");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recharge_alipay:
                paytype = 2;
                alipay_type.setImageResource(R.drawable.ic_select);
                wechat_type.setImageResource(R.drawable.ic_unselect);
                break;
            case R.id.recharge_wechat:
                paytype = 1;
                alipay_type.setImageResource(R.drawable.ic_unselect);
                wechat_type.setImageResource(R.drawable.ic_select);
                break;
            case R.id.recharge_submit:
                if (checkBox.isChecked()) {
                    Loading.show(this, "创建订单");
                    new Order_Get_Servlet(this).execute(String.valueOf(paytype), bean.getMoney(), bean.getStars());
                } else {
                    TabToast.makeText("请先阅读并同意《充值协议》");
                }
                break;
            case R.id.recharge_agreement:
                break;

        }
    }

    /**
     * 订单数据
     *
     * @param bean
     */
    public void CallBack_Order(Order_Entity.DataBean bean) {
        switch (paytype) {
            //支付宝
            case 2:
                Map<String, String> keyValues = new HashMap<>();

                keyValues.put("app_id", Const.AliPay_APPID);

                keyValues.put("biz_content",
                        "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\"," +
                                "\"total_amount\":\"" + bean.getTotal_fee() + "\",\"subject\":\"" + bean.getBody() + "\"," +
                                "\"body\":\"" + bean.getBody() + "\",\"out_trade_no\":\"" + bean.getOut_trade_no() + "\"}");

                keyValues.put("charset", "utf-8");

                keyValues.put("method", "alipay.trade.app.pay");

                keyValues.put("notify_url", "http://api.syplp.com/alipaycallbacks");

                keyValues.put("sign_type", "RSA2");

                keyValues.put("timestamp", TimeUtil.StringPattern(bean.getTime()));

                keyValues.put("version", "1.0");

                String orderParam = OrderInfoUtil2_0.buildOrderParam(keyValues);

                String sign = OrderInfoUtil2_0.getSign(keyValues, Const.AliPay_RSA2_PRIVATE, true);
                final String orderInfo = orderParam + "&" + sign;

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(Recharge_Activity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                Thread payThread = new Thread(payRunnable);
                payThread.start();
                break;
            //微信
            case 1:

                MyApp.request = new PayReq();
                MyApp.request.appId = bean.getAppid();
                MyApp.request.partnerId = bean.getPartnerid();
                MyApp.request.prepayId = bean.getPrepayid();
                MyApp.request.packageValue = "Sign=WXPay";
                MyApp.request.nonceStr = bean.getNoncestr();
                MyApp.request.timeStamp = bean.getTimestamp();
                MyApp.request.sign = bean.getSign();
                MyApp.api.sendReq(MyApp.request);

                break;

        }
    }

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        TabToast.makeText("支付成功");

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        TabToast.makeText("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
}

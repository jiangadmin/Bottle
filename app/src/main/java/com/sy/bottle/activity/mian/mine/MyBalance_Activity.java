package com.sy.bottle.activity.mian.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SlidingPaneLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.sy.bottle.R;
import com.sy.bottle.activity.Base_Activity;
import com.sy.bottle.activity.mian.other.NewWebActivity;
import com.sy.bottle.activity.mian.other.Put_forward_Activity;
import com.sy.bottle.adapters.Adapter_Goods;
import com.sy.bottle.app.MyApp;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReCharge_Dialog;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Goods_Entity;
import com.sy.bottle.entity.Order_Entity;
import com.sy.bottle.entity.PayResult;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.servlet.Goods_Servlet;
import com.sy.bottle.servlet.Order_Get_Servlet;
import com.sy.bottle.utils.OrderInfoUtil2_0;
import com.sy.bottle.utils.SaveUtils;
import com.sy.bottle.utils.TimeUtil;
import com.sy.bottle.view.ListViewForScrollView;
import com.sy.bottle.view.TabToast;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: jiangyao
 * @date: 2018/6/13
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 我的能量
 */
public class MyBalance_Activity extends Base_Activity implements View.OnClickListener, Adapter_Goods.Listenner {
    private static final String TAG = "MyBalance_Activity";

    SlidingPaneLayout sl;

    TextView num, put_forward, agreement;

    ListViewForScrollView goods;

    RelativeLayout rmb_other;

    Adapter_Goods adapter_goods;

    RelativeLayout alipay, wechat;

    ImageView alipay_type, wechat_type;

    CheckBox checkBox;

    Button submit;

    /**
     * 付款  1 微信 2 支付宝
     */
    int paytype = 2;

    List<Goods_Entity.DataBean> goodsbean = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MyBalance_Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybalance);

        setTitle("我的能量");

        setBack(true);

        initview();

        new Goods_Servlet(this).execute(String.valueOf(paytype));

        adapter_goods = new Adapter_Goods(this, this);
        goods.setAdapter(adapter_goods);
    }

    public void CallBack_Goods(List<Goods_Entity.DataBean> dataBeans) {

        goodsbean.clear();
        goodsbean.addAll(dataBeans);
        adapter_goods.setListData(goodsbean);
        adapter_goods.notifyDataSetChanged();

    }

    private void initview() {

        sl = findViewById(R.id.mybalance_sl);
        num = findViewById(R.id.mybalance_num);
        put_forward = findViewById(R.id.mybalance_put_forward);
        goods = findViewById(R.id.goods);

        alipay = findViewById(R.id.recharge_alipay);
        wechat = findViewById(R.id.recharge_wechat);
        alipay_type = findViewById(R.id.recharge_alipay_type);
        wechat_type = findViewById(R.id.recharge_wechat_type);

        checkBox = findViewById(R.id.recharge_checkBox);
        agreement = findViewById(R.id.recharge_agreement);

        submit = findViewById(R.id.recharge_submit);

        num.setText(String.valueOf(SaveUtils.getInt(Save_Key.S_能量)));
        rmb_other = findViewById(R.id.mybalance_rmb_other);

        rmb_other.setOnClickListener(this);
        put_forward.setOnClickListener(this);

        alipay.setOnClickListener(this);
        wechat.setOnClickListener(this);
        agreement.setOnClickListener(this);
        submit.setOnClickListener(this);

        alipay.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.mybalance_rmb_other:
                new ReCharge_Dialog(this);
                break;

            case R.id.mybalance_put_forward:
                Put_forward_Activity.start(this);
                break;

            case R.id.recharge_alipay:
                paytype = 2;
                alipay.setEnabled(false);
                wechat.setEnabled(true);

                alipay_type.setImageResource(R.drawable.ic_select);
                wechat_type.setImageResource(R.drawable.ic_unselect);

                goodsbean.clear();
                adapter_goods.setListData(goodsbean);
                adapter_goods.notifyDataSetChanged();

                new Goods_Servlet(this).execute(String.valueOf(paytype));
                break;
            case R.id.recharge_wechat:
                paytype = 1;
                wechat.setEnabled(false);
                alipay.setEnabled(true);

                alipay_type.setImageResource(R.drawable.ic_unselect);
                wechat_type.setImageResource(R.drawable.ic_select);

                goodsbean.clear();
                adapter_goods.setListData(goodsbean);
                adapter_goods.notifyDataSetChanged();

                new Goods_Servlet(this).execute(String.valueOf(paytype));
                break;
            case R.id.recharge_submit:
                if (checkBox.isChecked()) {

                    if (submit.getText().toString().equals("确认充值")) {
                        TabToast.makeText("请选择充值金额");
                        return;
                    }
                    Loading.show(this, "创建订单");
                    for (Goods_Entity.DataBean bean : goodsbean) {
                        if (bean.isType()) {
                            new Order_Get_Servlet(this).execute(String.valueOf(paytype), bean.getMoney(), bean.getStars());
                            return;
                        }
                    }

                } else {
                    TabToast.makeText("请阅读并同意《充值协议》");
                }
                break;
            case R.id.recharge_agreement:
                NewWebActivity.start(this, Const.API + "agreement.html");
                break;

        }
    }

    @Override
    public void Good(Goods_Entity.DataBean bean) {
        for (Goods_Entity.DataBean bean1 : goodsbean) {
            if (bean == bean1) {
                bean1.setType(true);
                submit.setText("确认充值" + bean1.getMoney() + "元");
            } else {
                bean1.setType(false);
            }
        }
        adapter_goods.setListData(goodsbean);
        adapter_goods.notifyDataSetChanged();
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
                        PayTask alipay = new PayTask(MyBalance_Activity.this);
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

                PayReq request = new PayReq();
                request.appId = bean.getAppid();
                request.partnerId = bean.getPartnerid();
                request.prepayId = bean.getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = bean.getNoncestr();
                request.timeStamp = bean.getTimestamp();
                request.sign = bean.getSign();
                MyApp.api.sendReq(request);

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

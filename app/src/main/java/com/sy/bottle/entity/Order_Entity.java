package com.sy.bottle.entity;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 订单信息
 */
public class Order_Entity extends Base_Entity {

    /**
     * data : {"prepayid":"wx200853128897221fe63ba21e0145046198","appid":"wx987316d1f1e0eac4","partnerid":"1505065931","package":"Sign=WXPay","noncestr":"te6s5HPNQLZ4nDSdGU0Jr8VvAqYuBTFk","timestamp":1529455992,"sign":"03EDF54B2D4015C5DF7F7E4B1431D3D5","order_id":"2018062008061249"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * 微信使用
         * prepayid : wx200853128897221fe63ba21e0145046198
         * appid : wx987316d1f1e0eac4
         * partnerid : 1505065931
         * package : Sign=WXPay
         * noncestr : te6s5HPNQLZ4nDSdGU0Jr8VvAqYuBTFk
         * timestamp : 1529455992
         * sign : 03EDF54B2D4015C5DF7F7E4B1431D3D5
         * order_id : 2018062008061249
         */

        private String prepayid;
        private String appid;
        private String partnerid;
        private String noncestr;
        private String timestamp;
        private String sign;
        private String order_id;

        /**
         * 支付宝使用
         * body : 100星星套餐
         * total_fee : 1
         * out_trade_no : 2018062008061586
         * time : 1529456115
         */

        private String body;
        private String total_fee;
        private String out_trade_no;
        private long time;

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}

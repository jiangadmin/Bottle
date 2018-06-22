package com.sy.bottle.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 提现记录
 */
public class Put_Forward_Log_Entity extends Base_Entity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * account : 哄
         * price : 10000
         * money : 100.00
         * connent : null
         * status : 2
         * create_time : 2018-06-22 02:47:06
         * update_time : null
         */

        private String account;
        private int price;
        private String money;
        private Object connent;
        @SerializedName("status")
        private int statusX;
        private String create_time;
        private Object update_time;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public Object getConnent() {
            return connent;
        }

        public void setConnent(Object connent) {
            this.connent = connent;
        }

        public int getStatusX() {
            return statusX;
        }

        public void setStatusX(int statusX) {
            this.statusX = statusX;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public Object getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Object update_time) {
            this.update_time = update_time;
        }
    }
}

package com.sy.bottle.entity;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/14
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 赠送记录
 */
public class Gift_Set_Log_Entity extends Base_Entity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * price : 88
         * give_user_id : 1000024
         * nickname : 走流程3
         * name : 小花花
         * pic_url : 礼物地址
         * create_time : 2018-05-24 05:26:59
         */

        private int price;
        private String give_user_id;
        private String nickname;
        private String name;
        private String pic_url;
        private String create_time;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getGive_user_id() {
            return give_user_id;
        }

        public void setGive_user_id(String give_user_id) {
            this.give_user_id = give_user_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}

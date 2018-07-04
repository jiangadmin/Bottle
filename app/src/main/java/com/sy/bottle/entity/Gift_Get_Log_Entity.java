package com.sy.bottle.entity;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/14
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 接收记录
 */
public class Gift_Get_Log_Entity extends Base_Entity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * price : 68
         * user_id : 1000001
         * nickname : 伟大领袖姜主席
         * name : 玫瑰花
         * pic_url : https://syplp-1256717985.cos.ap-shanghai.myqcloud.com/app_img/1-%E7%8E%AB%E7%91%B0%E8%8A%B1.png
         * create_time : 2018-06-16 03:11:55
         */

        private String price;
        private String user_id;
        private String nickname;
        private String name;
        private String pic_url;
        private String create_time;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

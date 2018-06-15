package com.sy.bottle.entity;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 个人信息
 */
public class UserInfo_Entity extends Base_Entity {


    /**
     * data : {"nikename":"JUST DO IT","phone":"13218061231","sex":"1","score":"0","balance":"0","avatar":"http://thirdqq.qlogo.cn/qqapp/1106842163/259D058ED101957F23B668BE7A002E33/100","sign":"这个人很懒，什么都没有留下","province":"江苏省","city":"南京市","area":""}
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
         * nikename : JUST DO IT
         * phone : 13218061231
         * sex : 1
         * score : 0  积分
         * balance : 0  星星
         * avatar : http://thirdqq.qlogo.cn/qqapp/1106842163/259D058ED101957F23B668BE7A002E33/100
         * sign : 这个人很懒，什么都没有留下
         * province : 江苏省
         * city : 南京市
         * area :
         */

        private String nikename;
        private String phone;
        private String sex;
        private int score;
        private int balance;
        private String avatar;
        private String sign;
        private String province;
        private String city;
        private String area;

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getBalance() {
            return balance;
        }

        public void setBalance(int balance) {
            this.balance = balance;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}

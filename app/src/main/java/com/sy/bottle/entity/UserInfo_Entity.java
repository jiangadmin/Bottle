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
     * data : {"id":1000001,"nikename":"伟大领袖姜主席","phone":"18661201018","sex":"1","score":"75988557","balance":"88992060","avatar":"syplp/1000001/15292030725450.jpeg","sign":"这个人很懒，什么都没有留下","province":"北京","city":"北京市","area":"东城区"}
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
         * id : 1000001
         * nikename : 伟大领袖姜主席
         * phone : 18661201018
         * sex : 1
         * score : 75988557
         * balance : 88992060
         * avatar : syplp/1000001/15292030725450.jpeg
         * sign : 这个人很懒，什么都没有留下
         * province : 北京
         * city : 北京市
         * area : 东城区
         */

        private String id;
        private String nikename;
        private String phone;
        private String sex;
        private long score;
        private long balance;
        private String avatar;
        private String sign;
        private String province;
        private String city;
        private String area;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
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

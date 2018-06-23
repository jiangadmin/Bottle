package com.sy.bottle.entity;

/**
 * @author: jiangyao
 * @date: 2018/6/8
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 捞瓶子
 */
public class Bottle_Get_Entity extends Base_Entity {


    /**
     * data : {"id":28,"user_id":"1000009","nikename":"一个人心潮澎湃。","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJafX8ibIBv9lYh4sxwhIfls0KKBbpexPcJqUzINAibW0VYvQBy2V7ahLx52eC9ibl3VYsRJBmT1Cmew/132","province":"江苏省","city":"连云港市","area":"","type":"1","content":"小么小二郎","difference":59}
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
         * id : 28
         * user_id : 1000009
         * nikename : 一个人心潮澎湃。
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJafX8ibIBv9lYh4sxwhIfls0KKBbpexPcJqUzINAibW0VYvQBy2V7ahLx52eC9ibl3VYsRJBmT1Cmew/132
         * province : 江苏省
         * city : 连云港市
         * area :
         * type : 1
         * content : 小么小二郎
         * difference : 59
         */

        private int id;
        private String user_id;
        private String nikename;
        private String avatar;
        private String province;
        private String city;
        private String area;
        private String type;
        private String content;
        private int difference;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNikename() {
            return nikename;
        }

        public void setNikename(String nikename) {
            this.nikename = nikename;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getDifference() {
            return difference;
        }

        public void setDifference(int difference) {
            this.difference = difference;
        }
    }
}

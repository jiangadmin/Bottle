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
     * data : {"id":8,"user_id":"1000024","type":"1","content":"我就随便一测","difference":110}
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
         * id : 8
         * user_id : 1000024
         * type : 1
         * content : 我就随便一测
         * difference : 110
         */

        private String id;
        private String user_id;
        private String type;
        private String content;
        private int difference;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

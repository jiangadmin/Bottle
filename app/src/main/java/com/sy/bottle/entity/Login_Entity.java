package com.sy.bottle.entity;

/**
 * @author: jiangyao
 * @date: 2018/5/23
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 登录返回
 */
public class Login_Entity extends Base_Entity {

    /**
     * data : {"uid":"1000001","access_token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJTSEEyNTYifQ__.eyJpYXQiOjE1Mjg3ODM3NTIsImV4cCI6MTUzMTM3NTc1MiwidWlkIjoiMTAwMDAwMSJ9.42aa8f09bf190a652d80d2d8bee263bda5d6668f79862cd7afb25de3241e1b23","usersig":"eJxl0FFPgzAUBeB3fgXp64y2FBz4prIZhK0wyBJ9IYx2o06gQAebxv-uBksk8b5*Jzk591tRVRVEXnibpGl5KGQsT4IB9UEFENz8oRCcxomMcU3-ITsKXrM42UpW96gZlgbhOMIpKyTf8msAwcuhUaCh*7gvGVw-s4U1bI4jfNfjYvb27AR2Xp4mfkZede*deHrX5SuybpcREW5mh5m50CtnQ30TkeXOyR5dlq7bSUFnL3Pzo3Pxfn6XBNz*6lDjR8FndViFjdCrp03djColz4dfIEMzp1PjHmsjbVnd8LIYJkNkIISsyy6g-Ci-sVZc-A__"}
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
         * uid : 1000001
         * access_token : eyJ0eXAiOiJKV1QiLCJhbGciOiJTSEEyNTYifQ__.eyJpYXQiOjE1Mjg3ODM3NTIsImV4cCI6MTUzMTM3NTc1MiwidWlkIjoiMTAwMDAwMSJ9.42aa8f09bf190a652d80d2d8bee263bda5d6668f79862cd7afb25de3241e1b23
         * usersig : eJxl0FFPgzAUBeB3fgXp64y2FBz4prIZhK0wyBJ9IYx2o06gQAebxv-uBksk8b5*Jzk591tRVRVEXnibpGl5KGQsT4IB9UEFENz8oRCcxomMcU3-ITsKXrM42UpW96gZlgbhOMIpKyTf8msAwcuhUaCh*7gvGVw-s4U1bI4jfNfjYvb27AR2Xp4mfkZede*deHrX5SuybpcREW5mh5m50CtnQ30TkeXOyR5dlq7bSUFnL3Pzo3Pxfn6XBNz*6lDjR8FndViFjdCrp03djColz4dfIEMzp1PjHmsjbVnd8LIYJkNkIISsyy6g-Ci-sVZc-A__
         */

        private String uid;
        private String access_token;
        private String usersig;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getUsersig() {
            return usersig;
        }

        public void setUsersig(String usersig) {
            this.usersig = usersig;
        }
    }
}

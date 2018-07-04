package com.sy.bottle.entity;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/19
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 好友
 */
public class Friends_Entity extends Base_Entity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * user_id : 1000003
         * friend_id : 1000001
         * nickname : 伟大领袖姜主席
         * avatar : syplp/1000001/15292030725450.jpeg
         * sign : 这个人很懒，什么都没有留下
         * content : null
         */

        private String id;
        private String user_id;
        private String friend_id;
        private String nickname;
        private String avatar;
        private String sign;
        private String content;

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

        public String getFriend_id() {
            return friend_id;
        }

        public void setFriend_id(String friend_id) {
            this.friend_id = friend_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

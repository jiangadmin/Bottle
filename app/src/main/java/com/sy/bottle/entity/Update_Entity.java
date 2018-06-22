package com.sy.bottle.entity;

/**
 * @author: jiangyao
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Update_Entity extends Base_Entity {

    /**
     * data : {"ver":"1.00","url":"url啊","is_must":"1","content":"更新内容"}
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
         * ver : 1.00
         * url : url啊
         * is_must : 1
         * content : 更新内容
         */

        private String ver;
        private String url;
        private String is_must;
        private String content;

        public String getVer() {
            return ver;
        }

        public void setVer(String ver) {
            this.ver = ver;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIs_must() {
            return is_must;
        }

        public void setIs_must(String is_must) {
            this.is_must = is_must;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

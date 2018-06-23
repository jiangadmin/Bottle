package com.sy.bottle.entity;

/**
 * @author: jiangadmin
 * @date: 2018/6/21
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Webtxt_Entity extends Base_Entity {

    /**
     * data : {"title":"官方公告","content":"内容","value":"<p style=\"text-align:left;\">\r\n\t<br />\r\n<\/p>\r\n<p style=\"text-align:center;\">\r\n\t<span style=\"color:#000000;\">充值遇到问题或单笔充值更大金额，请联系客服<\/span> \r\n<\/p>"}
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
         * title : 官方公告
         * content : 内容
         * value : <p style="text-align:left;">
         <br />
         </p>
         <p style="text-align:center;">
         <span style="color:#000000;">充值遇到问题或单笔充值更大金额，请联系客服</span>
         </p>
         */

        private String title;
        private String content;
        private String value;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

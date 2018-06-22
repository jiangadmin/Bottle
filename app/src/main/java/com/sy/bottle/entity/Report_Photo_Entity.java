package com.sy.bottle.entity;

/**
 * @author: jiangyao
 * @date: 2018/6/22
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Report_Photo_Entity extends Base_Entity {

    /**
     * data : {"url":"http://syplp-1256717985.cos.ap-shanghai.myqcloud.com/syplp/complaint/1000008/15296424057461.jpeg","key":"syplp/complaint/1000008/15296424057461.jpeg"}
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
         * url : http://syplp-1256717985.cos.ap-shanghai.myqcloud.com/syplp/complaint/1000008/15296424057461.jpeg
         * key : syplp/complaint/1000008/15296424057461.jpeg
         */

        private String url;
        private String key;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}

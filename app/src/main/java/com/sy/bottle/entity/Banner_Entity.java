package com.sy.bottle.entity;

import java.util.List;

/**
 * Created by jiangyao
 * on 2017/11/24.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:TODO 轮播图
 * update：
 */
public class Banner_Entity {


    /**
     * a : 0
     * d : [{"desc":"萌豆优品","id":318,"picUrl":"http://cdn.mengdouwang.cn/o_1biv5pn70qvf5kgt4b13jvtnh9.png","sysCode":"09","type":2,"url":"http://youpin.mengdouwang.cn/mobile/"},{"desc":"萌豆互娱","id":310,"picUrl":"http://cdn.mengdouwang.cn/o_1bincaidin2912531i5iipcjsc9.png","sysCode":"09","type":2,"url":"http://mp.weixin.qq.com/s/doBQOg2eKqCnbB1g9zL7vQ"},{"desc":"导购任务","id":314,"picUrl":"http://cdn.mengdouwang.cn/o_1bj2a337m1vvn31fqnhrsd1d489.png","sysCode":"09","type":1,"url":"http://mp.weixin.qq.com/s/zMtuuh1-3tIgz1pPdNLjMw"}]
     * e : 0
     */

    private List<DBean> d;

    public List<DBean> getD() {
        return d;
    }

    public void setD(List<DBean> d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * desc : 萌豆优品
         * id : 318
         * picUrl : http://cdn.mengdouwang.cn/o_1biv5pn70qvf5kgt4b13jvtnh9.png
         * sysCode : 09
         * type : 2
         * url : http://youpin.mengdouwang.cn/mobile/
         */

        private String desc;
        private int id;
        private String picUrl;
        private String sysCode;
        private int type;
        private String url;

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getSysCode() {
            return sysCode;
        }

        public void setSysCode(String sysCode) {
            this.sysCode = sysCode;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

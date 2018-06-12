package com.sy.bottle.entity;

import android.text.TextUtils;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/1
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO:
 */
public class Photos_Entity extends Base_Entity {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 4
         * pic_url : syplp/1000013/15268676128229.jpeg
         */

        private int id;
        private String pic_url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPic_url() {
            if (!TextUtils.isEmpty(pic_url)) {
                return Const.IMG + pic_url;
            }
            return pic_url;
        }

        public void setPic_url(String pic_url) {

            this.pic_url = pic_url;
        }
    }
}

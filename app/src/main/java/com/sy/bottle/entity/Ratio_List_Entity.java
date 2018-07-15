package com.sy.bottle.entity;

import java.util.List;

/**
 * @author: jiangyao
 * @date: 2018/6/20
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 比率尺
 */
public class Ratio_List_Entity extends Base_Entity {


    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * num : 0
         * ratio : 0
         */

        private int num;
        private int ratio;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getRatio() {
            return ratio;
        }

        public void setRatio(int ratio) {
            this.ratio = ratio;
        }

    }
}

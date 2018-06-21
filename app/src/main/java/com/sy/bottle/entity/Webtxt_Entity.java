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
     * data : {"content":"<p style=\"margin-left:0cm;\">\r\n\t1：请不要在相册、头像，上传不健康图片和性暗示图片及广告\r\n<\/p>\r\n<p style=\"margin-left:0cm;text-align:start;\">\r\n\t&nbsp;\r\n<\/p>\r\n<p style=\"margin-left:0cm;\">\r\n\t&nbsp;\r\n<\/p>\r\n<p style=\"margin-left:0cm;text-align:start;\">\r\n\t2：请不要在昵称、签名，出现低俗文字及广告\r\n<\/p>\r\n<p style=\"margin-left:0cm;\">\r\n\t&nbsp;\r\n<\/p>\r\n<p style=\"margin-left:0cm;text-align:start;\">\r\n\t&nbsp;\r\n<\/p>\r\n<p style=\"margin-left:0cm;text-align:start;\">\r\n\t3：请在（帮助与咨询）页面，查看更多信息\r\n<\/p>\r\n<p style=\"margin-left:0cm;text-align:start;\">\r\n\t&nbsp;\r\n<\/p>\r\n<p style=\"text-align:center;margin-left:0cm;\">\r\n\t<span style=\"color:#E53333;\">上述行为，一经发现核实立即做封号处理<\/span>\r\n<\/p>"}
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
         * content : <p style="margin-left:0cm;">
         1：请不要在相册、头像，上传不健康图片和性暗示图片及广告
         </p>
         <p style="margin-left:0cm;text-align:start;">
         &nbsp;
         </p>
         <p style="margin-left:0cm;">
         &nbsp;
         </p>
         <p style="margin-left:0cm;text-align:start;">
         2：请不要在昵称、签名，出现低俗文字及广告
         </p>
         <p style="margin-left:0cm;">
         &nbsp;
         </p>
         <p style="margin-left:0cm;text-align:start;">
         &nbsp;
         </p>
         <p style="margin-left:0cm;text-align:start;">
         3：请在（帮助与咨询）页面，查看更多信息
         </p>
         <p style="margin-left:0cm;text-align:start;">
         &nbsp;
         </p>
         <p style="text-align:center;margin-left:0cm;">
         <span style="color:#E53333;">上述行为，一经发现核实立即做封号处理</span>
         </p>
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}

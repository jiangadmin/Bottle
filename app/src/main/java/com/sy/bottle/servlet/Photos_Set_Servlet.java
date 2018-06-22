package com.sy.bottle.servlet;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sy.bottle.activity.mian.mine.Edit_Mine_Info_Activity;
import com.sy.bottle.dialog.Loading;
import com.sy.bottle.dialog.ReLogin_Dialog;
import com.sy.bottle.entity.Base_Entity;
import com.sy.bottle.entity.Const;
import com.sy.bottle.entity.Save_Key;
import com.sy.bottle.utils.HttpUtil;
import com.sy.bottle.utils.LogUtil;
import com.sy.bottle.utils.SaveUtils;

import java.util.List;

/**
 * @author: jiangadmin
 * @date: 2018/5/31
 * @Email: www.fangmu@qq.com
 * @Phone: 186 6120 1018
 * TODO: 设置相册
 */
public class Photos_Set_Servlet extends AsyncTask<String, Integer, Photos_Set_Servlet.Entity> {
    private static final String TAG = "Photos_Set_Servlet";

    Activity activity;

    public Photos_Set_Servlet(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected Entity doInBackground(String... strings) {

        String res = HttpUtil.uploadFile(Const.API + "photos/" + SaveUtils.getString(Save_Key.UID), strings[0]);
        Entity entity;

        if (TextUtils.isEmpty(res)) {
            entity = new Entity();
            entity.setStatus(-1);
            entity.setMessage("连接服务器失败！");
        } else {
            try {
                entity = new Gson().fromJson(res, Entity.class);
            } catch (Exception e) {
                entity = new Entity();
                entity.setStatus(-2);
                entity.setMessage("数据解析失败！");
            }
        }

        return entity;
    }


    @Override
    protected void onPostExecute(Entity entity) {
        super.onPostExecute(entity);

        Loading.dismiss();
        switch (entity.getStatus()) {
            case 200:

                if (activity instanceof Edit_Mine_Info_Activity) {
                    ((Edit_Mine_Info_Activity) activity).initphotos();
                }
                break;
            case 401:
                new ReLogin_Dialog();
                break;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        LogUtil.e(TAG, "进度：" + values[0]);
        super.onProgressUpdate(values);

    }

    public class Entity extends Base_Entity {

        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public class DataBean {
            /**
             * id : 44
             * pic_url : syplp/1000008/15296434487753.jpeg
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
                return pic_url;
            }

            public void setPic_url(String pic_url) {
                this.pic_url = pic_url;
            }
        }
    }
}

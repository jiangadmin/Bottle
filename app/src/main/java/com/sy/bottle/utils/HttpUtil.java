package com.sy.bottle.utils;

import android.text.TextUtils;

import com.sy.bottle.entity.Save_Key;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by jiangmac
 * on 15/12/23.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:HTTP工具类
 * update：细分发送方式 全部使用despost
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private static final int TIMEOUT_IN_MILLIONS = 15 * 1000;

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DEL = "DELETE";

    /**
     * 网络请求
     *
     * @param type 请求类型
     * @param url  请求地址
     * @param o    请求数据
     * @return
     */
    public static String request(String type, String url, Object o) {

        //定义stringbuffer  方便后面读取网页返回字节流信息时的字符串拼接
        StringBuffer stringBuffer = new StringBuffer();

        //创建url_connection
        URLConnection http_url_connection = null;
        try {
            http_url_connection = (new URL(url)).openConnection();
            //将urlconnection类强转为httpurlconnection类
            HttpURLConnection urlConn = (HttpURLConnection) http_url_connection;

            //超时时间
            urlConn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            urlConn.setConnectTimeout(TIMEOUT_IN_MILLIONS);

            urlConn.setRequestMethod(type);//设置请求方式。可以是delete put post get
            //get 请求需要的请求头
            if (type == GET) {
                LogUtil.e(TAG, type + "请求");
                urlConn.setRequestProperty("accept", "*/*");
                urlConn.setRequestProperty("connection", "Keep-Alive");
                urlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            }

            //post 请求需要的请求头
            if (type == POST) {
                LogUtil.e(TAG, type + "请求");

                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);

                urlConn.setRequestProperty("accept", "*/*");
                urlConn.setRequestProperty("Charset", "UTF-8");
                urlConn.setRequestProperty("Connection", "Keep-Alive");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            }

            //put 请求需要的请求头
            if (type == PUT) {
                LogUtil.e(TAG, type + "请求");
                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);

                urlConn.setRequestProperty("Charset", "UTF-8");
                urlConn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("Connection", "Keep-Alive");
                urlConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            }

            //del 请求需要的请求头
            if (type == DEL) {
                LogUtil.e(TAG, type + "请求");

                urlConn.setDoInput(true);
                urlConn.setDoOutput(true);

                urlConn.setRequestProperty("Content-Type", "application/json");
                urlConn.setRequestProperty("Accept", "application/json");
                urlConn.setRequestProperty("Charset", "UTF-8");
            }

            urlConn.setUseCaches(false);

            if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_校验))) {
                LogUtil.e(TAG, "加入校验");
                urlConn.setRequestProperty("token", SaveUtils.getString(Save_Key.S_校验));
            }

            if (o instanceof Map) {

                if (type != POST) {
                    String write = String.valueOf(ToolUtils.map2Json((Map<String, Object>) o));

                    LogUtil.e(TAG, "HTTP内容:" + write);

                    // 获取URLConnection对象对应的输出流
                    PrintWriter out = new PrintWriter(urlConn.getOutputStream());
                    out.print(write);
                    // flush输出流的缓冲
                    out.flush();

                } else {
                    StringBuilder paramStr = new StringBuilder();
                    for (Map.Entry<String, String> para : ((Map<String, String>) o).entrySet()) {
                        try {
                            paramStr.append(para.getKey()).append("=").append(URLEncoder.encode(para.getValue(), "UTF-8")).append("&");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                    LogUtil.e(TAG, "HTTP内容:" + paramStr.toString());
                    // 获取URLConnection对象对应的输出流
                    PrintWriter out = new PrintWriter(urlConn.getOutputStream());
                    out.print(paramStr.toString());
                    // flush输出流的缓冲
                    out.flush();

                }

            }

            InputStreamReader input_stream_reader;
            BufferedReader buffered_reader;
            LogUtil.e(TAG, "HTTP码：" + urlConn.getResponseCode());
            if (urlConn.getResponseCode() != 404) {

                input_stream_reader = new InputStreamReader(urlConn.getInputStream(), "utf-8");
                buffered_reader = new BufferedReader(input_stream_reader);
                stringBuffer = new StringBuffer();
                String line;
                while ((line = buffered_reader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                input_stream_reader.close();
                buffered_reader.close();
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        String res = stringBuffer.toString();

        LogUtil.e(TAG, "HTTP返回:" + res);

        return res;
    }


    /**
     * @param uploadUrl      上传路径参数
     * @param uploadFilePath 文件路径
     * @category 上传文件至Server的方法
     * @author ylbf_dev
     */
    public static String uploadFile(String uploadUrl, String uploadFilePath) {

        //定义stringbuffer  方便后面读取网页返回字节流信息时的字符串拼接
        StringBuffer stringBuffer = new StringBuffer();

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            if (!TextUtils.isEmpty(SaveUtils.getString(Save_Key.S_校验))) {
                urlConn.setRequestProperty("token", SaveUtils.getString(Save_Key.S_校验));
            }
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Charset", "UTF-8");
            urlConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"test.jpg\"" + end);
//          dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
            FileInputStream fis = new FileInputStream(uploadFilePath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);

            }
            fis.close();

            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            InputStreamReader input_stream_reader = new InputStreamReader(urlConn.getInputStream(), "utf-8");
            BufferedReader buffered_reader = new BufferedReader(input_stream_reader);
            stringBuffer = new StringBuffer();
            String line;
            while ((line = buffered_reader.readLine()) != null) {
                stringBuffer.append(line);
            }
            line = null;
            input_stream_reader.close();
            input_stream_reader = null;
            buffered_reader.close();
            buffered_reader = null;
            //  http_url_connection.disconnect();

            // 读取服务器返回结果
//            InputStream is = urlConn.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            String result = br.readLine();
            dos.close();
//            is.close();

            LogUtil.e(TAG, stringBuffer.toString());
            return stringBuffer.toString();

        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }

    }


}

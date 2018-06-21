package com.sy.bottle.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.sy.bottle.app.MyApp;
import com.sy.bottle.view.TabToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 垚垚
 * on 16/6/23.
 * Email: www.fangmu@qq.com
 * Phone：18661201018
 * Purpose: 工具类
 */

public class ToolUtils {
    private static final String TAG = "ToolUtils";

    public static Context context() {
        return MyApp.getInstance();
    }

    /**
     * Json 转成 Map<>
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, String> getMapForJson(String jsonStr) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            String value;
            Map<String, String> valueMap = new HashMap<String, String>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key).toString();
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Map 转 Json
     *
     * @param paramMap
     * @return
     */
    public static JSONObject map2Json(Map<String, Object> paramMap) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (paramMap != null && !paramMap.isEmpty()) {
                Set<Map.Entry<String, Object>> paramSet = paramMap.entrySet();
                for (Map.Entry<String, Object> entry : paramSet) {
                    String key = entry.getKey();
//                    if (key.equals("sign")) {
//                        continue;
//                    }
                    String value = null;
                    value = String.valueOf(entry.getValue());
                    jsonObject.put(key, value);
                }
            }
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage());
        }

        return jsonObject;
    }


    /**
     * map转String
     *
     * @param paramMap
     * @return
     */
    public static String map2String(Map<String, String> paramMap, String secretKey) {
        StringBuilder sb = new StringBuilder();
        sb.append(secretKey);
        if (paramMap != null && !paramMap.isEmpty()) {
            Set<Map.Entry<String, String>> paramSet = paramMap.entrySet();
            for (Map.Entry<String, String> entry : paramSet) {
                String key = entry.getKey();

                String value = entry.getValue();
                if (value == null) {
                    value = "";
                }
//                if (value != null && !"".equals(value)) {
                sb.append(key);
//                    sb.append(join);
                sb.append(value);
//                }
            }
        }
        LogUtil.e(TAG, "签名字符串拼装结果为:" + sb.toString());
        return sb.toString();
    }

    //检查网络是否正常 [功能描述].
    public static boolean checkNetwork() {
        boolean flag1 = false;
        ConnectivityManager cwjManager = (ConnectivityManager) context().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager.getActiveNetworkInfo() != null) {
            flag1 = cwjManager.getActiveNetworkInfo().isAvailable();
        }
        return flag1;
    }

    /**
     * 判断手机号
     *
     * @param mobiles 手机号
     * @return 是为true
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^1[34578]\\d(?!(\\d)\\\\1{7})\\d{8}?");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean isCardID(String cardId) {
        if (TextUtils.isEmpty(cardId)) {
            return true;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 验证是否包含 字母和数字  并验证8-16位
     *
     * @param str 传入的值
     * @return 符合为true
     */
    public static boolean isNormal(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.trim().length() >= 8 && str.trim().length() <= 16) {
            String regexNum = ".*[0-9]+.*";
            Pattern pattern = Pattern.compile(regexNum);
            Matcher isNum = pattern.matcher(str);
            String regexEn = ".*[a-zA-Z]+.*";
            Pattern patternEn = Pattern.compile(regexEn);
            Matcher isEnglish = patternEn.matcher(str);
            if (isNum.matches() && isEnglish.matches()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }


    /**
     * 验证身份证号
     *
     * @param str
     * @return
     */
    public static boolean IsIDcard(String str) {
        String regex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        return match(regex, str);
    }

    /**
     * 数据正则对比
     *
     * @param regex 正则
     * @param str   数据
     * @return
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * @param dp 要转换的 dp
     * @return 转出 px
     */
    public static int dp2px(float dp) {
        // 拿到屏幕密度
        float density = context().getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);// 四舍五入
        return px;
    }


    /**
     * @param px 要转换的px
     * @return 转出 dp
     */
    public static float px2dp(int px) {
        float density = context().getResources().getDisplayMetrics().density;
        float dp = px * density;
        return dp;

    }

    /**
     * 版本名
     *
     * @return
     */
    public static String getVersionName() {
        return getPackageInfo().versionName;
    }


    /**
     * 版本号
     *
     * @return
     */
    public static int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private static PackageInfo getPackageInfo() {
        PackageInfo pi = null;

        try {
            PackageManager pm = context().getPackageManager();
            pi = pm.getPackageInfo(context().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }

        return pi;
    }

    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    public static String StringPattern(String date, String newPattern) {
        if (date == null || newPattern == null) {
            return "";
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
            e.printStackTrace();       // 打印异常信息
            LogUtil.e(TAG, e.getMessage());
        }
        return sdf2.format(d);
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public static int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context().getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, e.getMessage());
        }
        return statusHeight;
    }


    /**
     * 判断 程序是否存在
     *
     * @param packageName
     * @return
     */

    public static boolean checkApplication(String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context().getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * 复制到系统粘贴板
     */
    public static void copyText(Context context, String string) {
        if (!TextUtils.isEmpty(string)) {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", string);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            TabToast.makeText("已复制");
        } else {
            TabToast.makeText("复制内容为空");
        }
    }


    /**
     * 获取设备UUID
     *
     * @return
     */

    public static String getMyUUID() {

        final TelephonyManager tm = (TelephonyManager) MyApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;

        tmDevice = "" + tm.getDeviceId();

        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(MyApp.getInstance().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

        String uniqueId = deviceUuid.toString();

        return uniqueId;

    }

    /**
     * Map 转 String
     *
     * @param param
     * @return
     */
    public static String MapToString(Map<String, Object> param) {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, Object> para : param.entrySet()) {
            paramStr.append(para.getKey()).append("=").append(para.getValue()).append("&");
        }
        LogUtil.e(TAG, paramStr.toString());
        return paramStr.toString();
    }


    /**
     * 保留两位小数
     *
     * @param v
     * @return
     */
    public static String float2(float v) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(v);
    }

    /**
     * 验证文件是否存在
     *
     * @param strFile
     * @return
     */
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }


    /**
     * 获得String中的数字
     *
     * @param s
     * @return
     */
    public static int StringInInt(String s) {

        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return Integer.parseInt(m.replaceAll("").trim());

    }

    /**
     * 中文转 二进制
     *
     * @param s
     * @return
     */
    public static String stringToUnicode(String s) {
        try {
            StringBuffer out = new StringBuffer("");
            //直接获取字符串的unicode二进制
            byte[] bytes = s.getBytes("unicode");
            //然后将其byte转换成对应的16进制表示即可
            for (int i = 0; i < bytes.length - 1; i += 2) {
                out.append("\\u");
                String str = Integer.toHexString(bytes[i + 1] & 0xff);
                for (int j = str.length(); j < 2; j++) {
                    out.append("0");
                }
                String str1 = Integer.toHexString(bytes[i] & 0xff);
                out.append(str1);
                out.append(str);
            }
            return out.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}

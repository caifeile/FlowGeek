package org.thanatos.base.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by thanatos on 15/12/25.
 */
public class Utilities {


    public static String dateFormat(String strdate) throws ParseException {
        if (isEmpty(strdate))return null;

        TimeZone tz =TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        long timestamp = System.currentTimeMillis() - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(strdate).getTime();
        if (0 <= timestamp && timestamp < 60 * 60 * 1000) {
            // 几分钟之前
            return timestamp / (60 * 1000) + "分钟之前";
        }else if (60 * 60 * 1000 <= timestamp && timestamp < 24 * 60 * 60 * 1000) {
            // 几小时之前
            return timestamp / (60 * 60 * 1000) + "小时之前";
        }else if (24 * 60 * 60 * 1000 <= timestamp && timestamp < (long)30 * 24 * 60 * 60 * 1000) {
            // 几天前
            return timestamp / (24 * 60 * 60 * 1000) + "天之前";
        }else if ((long)31 * 24 * 60 * 60 * 1000 < timestamp && timestamp < (long)12 * 30 * 24 * 60 * 60 * 1000) {
            // 几个月之前
            return timestamp / (long)30 * 24 * 60 * 60 * 1000 + "月之前";
        }else{
            return strdate;
        }
    }

    public static boolean isEmpty(String str) {
        return str==null || str.trim().equals("");
    }

    public static int toInt(String str, int d) {
        try {
            return Integer.valueOf(str);
        }catch (Exception e){
            return d;
        }
    }

    public static long toLong(String str, long d){
        try {
            return Long.valueOf(str);
        }catch (Exception e){
            return d;
        }
    }

    public static boolean isImgUrl(String url) {
        return !isEmpty(url) && Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)").matcher(url).matches();
    }
}

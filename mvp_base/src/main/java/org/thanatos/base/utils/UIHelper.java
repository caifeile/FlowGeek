package org.thanatos.base.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;


/**
 * @author thanatos
 * @create 2016-01-06
 **/
public class UIHelper {

    public static DisplayMetrics getScreen(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 某个主题下的attr资源id
     * @param attrId
     * @param theme
     * @return
     */
    public static int getAttrResourceFromTheme(int attrId, Resources.Theme theme){
        TypedValue value = new TypedValue();
        theme.resolveAttribute(attrId, value, true);
        return value.resourceId;
    }

    public static int getAttrValueFromTheme(int attrId, Resources.Theme theme){
        TypedValue value = new TypedValue();
        theme.resolveAttribute(attrId, value, true);
        return value.data;
    }


}

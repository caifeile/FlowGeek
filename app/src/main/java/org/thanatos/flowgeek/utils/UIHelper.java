package org.thanatos.flowgeek.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;

import org.thanatos.flowgeek.bean.EmotionRules;

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

    public static void backspace(EditText input) {
        if (input == null) {
            return;
        }
        KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
        input.dispatchKeyEvent(event);
    }

    /**
     * 表情输入至文字中
     * @param resources
     * @param emotion
     * @return
     */
    @SuppressWarnings("all")
    public static Spannable display(Resources resources, EmotionRules emotion) {
        String remote = emotion.getRemote();
        Spannable spannable = new SpannableString(remote);
        Drawable d = resources.getDrawable(emotion.getMResId());
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan iSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 前后输入的字符都不应用这种Spannable
        spannable.setSpan(iSpan, 0, remote.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable input2posts4image(Context context, String text, Bitmap bitmap){
        Spannable spannable = new SpannableString(text);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        spannable.setSpan(imageSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }
}

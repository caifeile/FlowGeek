package org.thanatos.flowgeek.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import org.thanatos.flowgeek.bean.EmotionRules;

/**
 * Created by thanatos on 16/2/3.
 */
public class InputHelper {

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
    public static Spannable insertEtn(Context context, EmotionRules emotion) {
        String remote = emotion.getRemote();
        Spannable spannable = new SpannableString(remote);
        Drawable d = context.getResources().getDrawable(emotion.getMResId());
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        ImageSpan iSpan = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE 前后输入的字符都不应用这种Spannable
        spannable.setSpan(iSpan, 0, remote.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static void encode(TextView view, String content){
        view.setText("");
        StringBuilder mNormalBuilder = new StringBuilder();
        StringBuilder mEtnBuilder = new StringBuilder();
        boolean isCut = false;
        for (int i=0; i<content.length(); i++){
            char unit = content.charAt(i);
            // 截断,保存在etnBuilder容器
            if (isCut){
                // 截断期间发现新的[,将之前的缓存存入view,刷新容器
                if (unit == '['){
                    mNormalBuilder.append(mEtnBuilder.toString());
                    mEtnBuilder.delete(0, mEtnBuilder.length());
                    mEtnBuilder.append(unit);
                    continue;
                }
                if (unit == ']'){
                    mEtnBuilder.append(unit);
                    EmotionRules rule = EmotionRules.containOf(mEtnBuilder.toString());
                    view.append(mNormalBuilder.toString());
                    if (rule!=null){
                        view.append(insertEtn(view.getContext(), rule));
                    }else{
                        view.append(mEtnBuilder.toString());
                    }
                    mNormalBuilder.delete(0, mNormalBuilder.length());
                    mEtnBuilder.delete(0, mEtnBuilder.length());
                    isCut = false;
                    continue;
                }
                mEtnBuilder.append(unit);

            }else{ // --> 非截流
                if (unit == '['){
                    mEtnBuilder.append(unit);
                    isCut = true;
                    continue;
                }
                mNormalBuilder.append(unit);
            }
        }
        view.append(mNormalBuilder.toString());
        view.append(mEtnBuilder.toString());
    }

}

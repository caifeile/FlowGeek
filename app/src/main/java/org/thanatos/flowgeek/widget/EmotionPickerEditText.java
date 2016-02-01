package org.thanatos.flowgeek.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * @author thanatos
 * @create 2016-01-06
 **/
public class EmotionPickerEditText extends EditText{

    private DrawableRightListener mRightListener ;

    final int DRAWABLE_LEFT = 0;
    final int DRAWABLE_TOP = 1;
    final int DRAWABLE_RIGHT = 2;
    final int DRAWABLE_BOTTOM = 3;

    public EmotionPickerEditText(Context context) {
        super(context);
    }

    public EmotionPickerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmotionPickerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface DrawableRightListener{
        void onDrawableRightClick();
    }

    public void setOnDrawableRightListener(DrawableRightListener listener){
        this.mRightListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mRightListener != null) {
                    Drawable drawableRight = getCompoundDrawables()[DRAWABLE_RIGHT];
                    event.getY();
                    if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds().width())) {
                        mRightListener.onDrawableRightClick();
                        return true ;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}

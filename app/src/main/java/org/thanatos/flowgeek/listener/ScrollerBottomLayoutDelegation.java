package org.thanatos.flowgeek.listener;

import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * NestedScrollView 与 Bottom View之间交互的委派者
 * Created by thanatos on 16/3/13.
 */
public class ScrollerBottomLayoutDelegation {

    private NestedScrollView mNestedScroller;
    private ViewGroup mBottomLayout;

    private ScrollerBottomLayoutDelegation(NestedScrollView scroller, ViewGroup view){
        this.mNestedScroller = scroller;
        this.mBottomLayout = view;
        init();
    }

    /**
     * 开始初始化代理
     */
    private void init() {
        mNestedScroller.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int dy = scrollY - oldScrollY;
                int nowYOffset = (int) (dy + mBottomLayout.getTranslationY());
                ViewCompat.animate(mBottomLayout).cancel();
                if (dy > 0) {
                    if (nowYOffset < mBottomLayout.getHeight()) {
                        mBottomLayout.setTranslationY(nowYOffset);
                    } else {
                        mBottomLayout.setTranslationY(mBottomLayout.getHeight());
                    }
                } else if (dy < 0) {
                    if (nowYOffset < 0) {
                        mBottomLayout.setTranslationY(0);
                    } else {
                        mBottomLayout.setTranslationY(nowYOffset);
                    }
                }
            }
        });
        mNestedScroller.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (!mNestedScroller.canScrollVertically(1)) {
                            animateHide();
                            return false;
                        }
                        if (!mNestedScroller.canScrollVertically(-1)) {
                            animateShow();
                            return false;
                        }
                        if (mNestedScroller.getScrollY() <= 0) return false;
                        if (mBottomLayout.getTranslationY() >= mBottomLayout.getHeight() / 2) {
                            animateHide();
                        } else {
                            animateShow();
                        }
                }
                return false;
            }
        });
    }

    public static void delegation(NestedScrollView scroller, ViewGroup view){
        new ScrollerBottomLayoutDelegation(scroller, view);
    }

    private void animateShow(){
        mBottomLayout.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);
    }

    private void animateHide(){
        mBottomLayout.animate()
                .translationY(mBottomLayout.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180);
    }

}

package org.thanatos.flowgeek.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import org.thanatos.component.FloatingActionsMenu;

/**
 * Created by thanatos on 16/2/17.
 */
public class FloatingAutoHideDownBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu>{

    private static final Interpolator INTERPOLATOR = new LinearInterpolator();
    private boolean mIsAnimatingOut = false;

    public FloatingAutoHideDownBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        float mPreTranslationY = dy + child.getTranslationY();
        if (mPreTranslationY>0 && mPreTranslationY<child.getFloatingActionButtonHeight()) {
            child.setTranslationY(mPreTranslationY);
            mIsAnimatingOut = dy > 0;
        }
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionsMenu child,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        // Ensure we react to vertical scrolling
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target) {
        super.onStopNestedScroll(coordinatorLayout, child, target);
        if (child.getTranslationY() <= 0 || child.getTranslationY() >= child.getHeight()) return;

        if (mIsAnimatingOut){
            animateOut(child);
        }else{
            animateIn(child);
        }

    }

    private void animateOut(final FloatingActionsMenu button) {
        button.animate()
                .translationY(button.getFloatingActionButtonHeight())
                .setInterpolator(INTERPOLATOR)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        button.setTranslationY(button.getFloatingActionButtonHeight());
                    }
                });
    }

    private void animateIn(FloatingActionsMenu button) {
        button.animate()
                .translationY(0)
                .setInterpolator(INTERPOLATOR)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        button.setTranslationY(0);
                    }
                });
    }
    
}

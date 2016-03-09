package org.thanatos.flowgeek.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by thanatos on 15-11-16.
 */
public class OnScrollerGoDownListener extends RecyclerView.OnScrollListener {

    private static final float TOOLBAR_ELEVATION = 14f;

    private View view;
    private State state;

    public OnScrollerGoDownListener(View view) {
        this.view = view;
        this.state = new State();
    }

    private void setElevation(float elevation) {
        ViewCompat.setElevation(view, elevation == 0 ? 0 : TOOLBAR_ELEVATION);// ---> Z轴高度
    }

    private void animateShow(final int verticalOffset) {
        view.animate()
                .translationY(0)
                .setInterpolator(new LinearInterpolator()) // ---> 均匀速率
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        setElevation(verticalOffset == 0 ? 0 : TOOLBAR_ELEVATION);
                    }
                });
    }

    private void animateHide() {
        view.animate()
                .translationY(view.getHeight())
                .setInterpolator(new LinearInterpolator())
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setElevation(0);
                    }
                });
    }

    @Override
    public final void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (state.scrollingDiff > 0) {
                if (state.verticalOffset > view.getHeight()) {
                    animateHide();
                } else {
                    animateShow(state.verticalOffset);
                }
            } else if (state.scrollingDiff < 0) {
                if (view.getTranslationY() < view.getHeight() * -0.6 && state.verticalOffset > view.getHeight()) {
                    animateHide();
                } else {
                    animateShow(state.verticalOffset);
                }
            }
        }
    }

    @Override
    public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//            Log.d("thanatos", "toolbar translation y is "+ toolbar.getTranslationY());
        state.verticalOffset = recyclerView.computeVerticalScrollOffset();
        state.scrollingDiff = dy;
        int toolbarYOffset = (int) (dy + view.getTranslationY());
        ViewCompat.animate(view).cancel();
        if (state.scrollingDiff > 0) {
            if (toolbarYOffset < view.getHeight()) {
                if (state.verticalOffset > view.getHeight()) {
                    setElevation(TOOLBAR_ELEVATION);
                }
                view.setTranslationY(state.translationY = toolbarYOffset);
            } else {
                setElevation(0);
                view.setTranslationY(state.translationY = view.getHeight());
            }
        } else if (state.scrollingDiff < 0) {
            if (toolbarYOffset < 0) {
                if (state.verticalOffset <= 0) {
                    setElevation(0);
                }
                view.setTranslationY(state.translationY = 0);
            } else {
                if (state.verticalOffset > view.getHeight()) {
                    setElevation(TOOLBAR_ELEVATION);
                }
                view.setTranslationY(state.translationY = toolbarYOffset);
            }
        }
    }

    public void onRestoreInstanceState(State state) {
        this.state.verticalOffset = state.verticalOffset;
        this.state.scrollingDiff = state.scrollingDiff;
        ViewCompat.setElevation(view, state.elevation);
        view.setTranslationY(state.translationY);
    }

    public State onSaveInstanceState() {
        state.translationY = view.getTranslationY();
        state.elevation = ViewCompat.getElevation(view);
        return state;
    }

    /**
     * Parcelable RecyclerView/Toolbar state for simpler saving/restoring its current state.
     */
    public static final class State implements Parcelable {
        public static Creator<State> CREATOR = new Creator<State>() {
            public State createFromParcel(Parcel parcel) {
                return new State(parcel);
            }

            public State[] newArray(int size) {
                return new State[size];
            }
        };

        private int verticalOffset;  // --> 滚动的垂直偏移量
        private int scrollingDiff; // --> 与上一次滚动位置的差值
        private float translationY; // --> 相对原位置的相对位置，- 往上移，+ 往下移
        private float elevation; // --> Z轴坐标

        State() {
        }

        State(Parcel parcel) {
            this.verticalOffset = parcel.readInt();
            this.scrollingDiff = parcel.readInt();
            this.translationY = parcel.readFloat();
            this.elevation = parcel.readFloat();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(verticalOffset);
            parcel.writeInt(scrollingDiff);
            parcel.writeFloat(translationY);
            parcel.writeFloat(elevation);
        }
    }
}

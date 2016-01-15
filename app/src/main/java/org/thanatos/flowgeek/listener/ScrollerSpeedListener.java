package org.thanatos.flowgeek.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by thanatos on 15/12/28.
 */
public class ScrollerSpeedListener extends RecyclerView.OnScrollListener {

    public static double DEFAULT_SPEED = 500.0;

    private double speed = DEFAULT_SPEED;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        dy = dy<0 ? -dy : dy;
        dy = dy > DEFAULT_SPEED ? (int) DEFAULT_SPEED : dy;
        speed = DEFAULT_SPEED - DEFAULT_SPEED * Math.sin(dy/DEFAULT_SPEED*Math.PI/2.0);
    }

    public int getSpeed(){
        return (int) speed;
    }
}

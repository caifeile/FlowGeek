package org.thanatos.flowgeek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.thanatos.flowgeek.bean.Comment;

/**
 * *************************************************
 *
 * *************************************************
 */
public class CmmAdapter extends BaseListAdapter<Comment>{


    public CmmAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return null;
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {

    }
}

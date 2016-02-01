package org.thanatos.flowgeek.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.base.utils.Utilities;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * *************************************************
 *
 * *************************************************
 */
public class CmmAdapter extends BaseListAdapter<Comment> {


    public CmmAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new CmmHoldView(mInflater.inflate(R.layout.list_item_comment, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        Comment item = items.get(position);
        CmmHoldView hold = (CmmHoldView) h;
        Picasso.with(mContext).load(item.getPortrait()).into(hold.ivPortrait);
        hold.tvUsername.setText(item.getAuthor());
        try {
            hold.tvTime.setText(Utilities.dateFormat(item.getPubDate()));
        } catch (ParseException e) {
            hold.tvTime.setText(item.getPubDate());
        }
        hold.tvContent.setText(item.getContent());
    }

    public static class CmmHoldView extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_portrait) ImageView ivPortrait;
        @Bind(R.id.tv_username) TextView tvUsername;
        @Bind(R.id.tv_time) TextView tvTime;
        @Bind(R.id.tv_content) TextView tvContent;

        public CmmHoldView(View view) {
            super(view);
            ViewCompat.setElevation(view, 1.0f);
            ButterKnife.bind(this, view);
        }
    }


}

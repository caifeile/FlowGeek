package org.thanatos.flowgeek.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.bean.User;
import org.thanatos.flowgeek.utils.InputHelper;

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
        Picasso.with(mContext).load(item.getPortrait())
                .into(hold.ivPortrait);
        hold.tvUsername.setText(item.getAuthor());
        try {
            hold.tvTime.setText(Utilities.dateFormat(item.getPubDate()));
        } catch (ParseException e) {
            hold.tvTime.setText(item.getPubDate());
        }
        hold.tvContent.setText("");
        InputHelper.encode(hold.tvContent, item.getContent());

        // setup refer
        if (hold.mLayoutRefer!=null){
            hold.mLayoutContainer.removeView(hold.mLayoutRefer);
        }
        LinearLayout mTempLayoutRefer = null;
        if (item.getRefers()!=null && item.getRefers().size()>0){
            for (int i=item.getRefers().size()-1; i>=0; i--){

                Comment.Refer refer = item.getRefers().get(i);

                LinearLayout view = (LinearLayout) mInflater
                        .inflate(R.layout.view_refer_item, hold.mLayoutContainer, false);

                TextView mTitleView = (TextView) view.findViewById(R.id.tv_refer_title);
                TextView mContentView = (TextView) view.findViewById(R.id.tv_refer_content);

                mTitleView.setText(refer.getTitle());
                InputHelper.encode(mContentView, refer.getBody());

                if (mTempLayoutRefer == null){
                    hold.mLayoutContainer.addView(hold.mLayoutRefer = view, 1);
                }else{
                    mTempLayoutRefer.addView(view, 0);
                }
                mTempLayoutRefer = view;
            }
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setUid(item.getAuthorId());
                user.setName(item.getAuthor());
                UIManager.toUserHome(mContext, user);
            }
        };
        hold.tvUsername.setOnClickListener(listener);
        hold.ivPortrait.setOnClickListener(listener);

    }

    public static class CmmHoldView extends RecyclerView.ViewHolder{
        @Bind(R.id.iv_portrait) ImageView ivPortrait;
        @Bind(R.id.tv_username) TextView tvUsername;
        @Bind(R.id.tv_time) TextView tvTime;
        @Bind(R.id.tv_content) TextView tvContent;
        @Bind(R.id.layout_container) LinearLayout mLayoutContainer;

        LinearLayout mLayoutRefer;

        public CmmHoldView(View view) {
            super(view);
            ViewCompat.setElevation(view, 1.0f);
            ButterKnife.bind(this, view);
        }
    }


}

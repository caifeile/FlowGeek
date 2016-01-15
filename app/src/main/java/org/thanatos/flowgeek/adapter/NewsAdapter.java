package org.thanatos.flowgeek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.listener.ScrollerSpeedListener;
import org.thanatos.flowgeek.utils.Utility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 15/12/22.
 */
public class NewsAdapter extends BaseListAdapter<News> {

    public NewsAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new NewNewsViewHolder(mInflater.inflate(R.layout.list_item_news, null));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        NewNewsViewHolder holder = (NewNewsViewHolder) h;
        News item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getBody());
        holder.author.setText(item.getAuthor());
        holder.comment_count.setText(String.valueOf(item.getCommentCount()));
        try {
            holder.time.setText(Utility.dateFormat(item.getPubDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.time.setText(item.getPubDate());
        }
    }

    public static final class NewNewsViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_title) TextView title;
        @Bind(R.id.tv_content) TextView content;
        @Bind(R.id.tv_author) TextView author;
        @Bind(R.id.tv_time) TextView time;
        @Bind(R.id.tv_comment_count) TextView comment_count;

        public NewNewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}

package org.thanatos.flowgeek.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.base.utils.Utilities;

import java.text.ParseException;

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
        return new NewsViewHolder(mInflater.inflate(R.layout.list_item_news, null));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        NewsViewHolder holder = (NewsViewHolder) h;
        News item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getBody());
        holder.author.setText(item.getAuthor());
        holder.comment_count.setText(String.valueOf(item.getCommentCount()));
        try {
            holder.time.setText(Utilities.dateFormat(item.getPubDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.time.setText(item.getPubDate());
        }
    }

    public static final class NewsViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_title) TextView title;
        @Bind(R.id.tv_content) TextView content;
        @Bind(R.id.tv_author) TextView author;
        @Bind(R.id.tv_time) TextView time;
        @Bind(R.id.tv_comment_count) TextView comment_count;

        public NewsViewHolder(View view) {
            super(view);
            ViewCompat.setElevation(view, 5);
            ButterKnife.bind(this, view);
        }
    }
}

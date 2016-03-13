package org.thanatos.flowgeek.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.utils.UIHelper;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.Tweet;
import org.thanatos.flowgeek.bean.User;
import org.thanatos.flowgeek.ui.activity.ListLikeUserActivity;
import org.thanatos.flowgeek.ui.activity.UserHomeActivity;
import org.thanatos.flowgeek.utils.InputHelper;
import org.thanatos.flowgeek.utils.Utility;
import org.thanatos.flowgeek.widget.JumpViewSpan;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 16/2/16.
 */
public class TweetAdapter extends BaseListAdapter<Tweet>{

    public TweetAdapter(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return new TweetViewHold(mInflater.inflate(R.layout.list_item_tweet, parent, false));
    }

    @Override
    protected void onBindDefaultViewHolder(RecyclerView.ViewHolder h, int position) {
        TweetViewHold holder = (TweetViewHold) h;
        Tweet item = items.get(position);
        Picasso.with(mContext).load(item.getPortrait()).into(holder.ivPortrait);
        holder.tvUsername.setText(item.getAuthor());
        InputHelper.encode(holder.tvContent, item.getBody());
        try {
            holder.tvTime.setText(Utilities.dateFormat(item.getPubDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tvTime.setText(item.getPubDate());
        }
        holder.tvCmmCount.setText(item.getCmmCount());
        if (item.getIsLike()==1){
            holder.ivLike.setSelected(true);
        }

        if (item.getLikeUser()!=null && item.getLikeUser().size()>0){
            holder.tvLikerList.setText("");
            int m3 = UIHelper.dip2px(mContext, 3);
            holder.tvLikerList.setPadding(
                    m3, m3, m3, m3
            );
            for (int i=0; i<item.getLikeUser().size(); i++){
                if (i>0){
                    holder.tvLikerList.append(", ");
                }
                User user = item.getLikeUser().get(i);
                Intent intent = new Intent(mContext, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.BUNDLE_USER_KEY, user);
                Spannable spannable = new SpannableString(user.getName());
                spannable.setSpan(new JumpViewSpan(intent, mContext), 0, user.getName().length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tvLikerList.append(spannable);
            }
            holder.tvLikerList.append("等");

            Intent intent = new Intent(mContext, ListLikeUserActivity.class);
            intent.putExtra(ListLikeUserActivity.BUNDLE_KEY_TWEET_ID, item.getId());
            String replaceText = item.getLikeCount() + "人";
            Spannable mPeopleCountSpannable = new SpannableString(replaceText);
            mPeopleCountSpannable.setSpan(new JumpViewSpan(intent, mContext), 0, replaceText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvLikerList.append(mPeopleCountSpannable);

            holder.tvLikerList.append("手贱点了赞");

            holder.tvLikerList.setVisibility(View.VISIBLE);
        }else holder.tvLikerList.setVisibility(View.GONE);

        if (!Utilities.isEmpty(item.getImgSmall())){
            holder.layoutImageList.setVisibility(View.VISIBLE);
            holder.layoutImageList.removeAllViews();
            ImageView mSmallImage = new ImageView(mContext);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(180, 180);
            mSmallImage.setLayoutParams(mLayoutParams);
            mSmallImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.layoutImageList.addView(mSmallImage);
            Picasso.with(mContext)
                    .load(item.getImgSmall())
                    .placeholder(mContext.getResources().getDrawable(R.mipmap.loading_holdplacer))
                    .into(mSmallImage);
            mSmallImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 显示大图, 考虑Bitmap的性能问题
                }
            });


        }else {
            if (holder.layoutImageList.getChildCount()>0)
                holder.layoutImageList.removeAllViews();
            holder.layoutImageList.setVisibility(View.GONE);
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
        holder.tvUsername.setOnClickListener(listener);
        holder.ivPortrait.setOnClickListener(listener);

    }

    public static class TweetViewHold extends RecyclerView.ViewHolder{

        @Bind(R.id.iv_portrait) ImageView ivPortrait;
        @Bind(R.id.tv_username) TextView tvUsername;
        @Bind(R.id.tv_content) TextView tvContent;
        @Bind(R.id.layout_image_list) LinearLayout layoutImageList;
        @Bind(R.id.tv_liker_list) TextView tvLikerList;
        @Bind(R.id.tv_time) TextView tvTime;
        @Bind(R.id.tv_comment_count) TextView tvCmmCount;
        @Bind(R.id.iv_like) ImageView ivLike;

        public TweetViewHold(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}

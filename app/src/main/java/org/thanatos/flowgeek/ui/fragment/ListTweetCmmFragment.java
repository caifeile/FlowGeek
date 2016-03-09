package org.thanatos.flowgeek.ui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.utils.UIHelper;
import org.thanatos.base.utils.Utilities;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.adapter.CmmAdapter;
import org.thanatos.flowgeek.bean.Comment;
import org.thanatos.flowgeek.bean.Tweet;
import org.thanatos.flowgeek.bean.User;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.CmmPresenter;
import org.thanatos.flowgeek.ui.activity.ListLikeUserActivity;
import org.thanatos.flowgeek.ui.activity.UserHomeActivity;
import org.thanatos.flowgeek.widget.JumpViewSpan;

import java.text.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(CmmPresenter.class)
public class ListTweetCmmFragment extends BaseCmmFragment<CmmPresenter>
        implements BaseListAdapter.OnLoadingHeaderCallBack {
    
    private Tweet tweet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 处理请求tweet对象
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY))
                .filter(events -> events.what == Events.EventEnum.DELIVER_TWEET)
                .subscribe(events -> {
                    tweet = events.getMessage();
                });
        
        RxBus.getInstance().send(Events.EventEnum.GET_TWEET, Events.EventEnum.DELIVER_TWEET);
        
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnLoadingHeaderCallBack(this);
    }

    @Override
    protected BaseListAdapter<Comment> onSetupAdapter() {
        return new CmmAdapter(getContext(), BaseListAdapter.BOTH_HEADER_FOOTER);
    }

    @Override
    public int setDividerSize() {
        return getResources().getDimensionPixelSize(R.dimen.min_divider_height);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderHolder(ViewGroup parent) {
        return new TweetViewHold(LayoutInflater.from(mContext)
                .inflate(R.layout.list_header_tweet, parent, false));
    }

    @Override
    public void onBindHeaderHolder(RecyclerView.ViewHolder h, int position) {
        TweetViewHold holder = (TweetViewHold) h;
        if (!holder.isVirgin()) return;
        Picasso.with(mContext).load(tweet.getPortrait()).into(holder.ivPortrait);
        holder.tvUsername.setText(tweet.getAuthor());
        holder.tvContent.setText(tweet.getBody());
        try {
            holder.tvTime.setText(Utilities.dateFormat(tweet.getPubDate()));
        } catch (ParseException e) {
            holder.tvTime.setText(tweet.getPubDate());
        }
        holder.tvCmmCount.setText(tweet.getCmmCount());
        if (tweet.getIsLike()==1){
            holder.ivLike.setImageResource(R.mipmap.icon_like_mini_yes);
        }

        if (tweet.getLikeUser()!=null && tweet.getLikeUser().size()>0){
            holder.tvLikerList.setText("");
            int m3 = UIHelper.dip2px(mContext, 3);
            holder.tvLikerList.setPadding(
                    m3, m3, m3, m3
            );
            for (int i=0; i<tweet.getLikeUser().size(); i++){
                if (i>0){
                    holder.tvLikerList.append(", ");
                }
                User user = tweet.getLikeUser().get(i);
                Intent intent = new Intent(mContext, UserHomeActivity.class);
                intent.putExtra(UserHomeActivity.BUNDLE_USER_KEY, user);
                Spannable spannable = new SpannableString(user.getName());
                spannable.setSpan(new JumpViewSpan(intent, mContext), 0, user.getName().length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tvLikerList.append(spannable);
            }
            holder.tvLikerList.setCompoundDrawables(mContext.getResources().getDrawable(
                    R.mipmap.icon_like_mini_yes), null, null, null);
            holder.tvLikerList.setCompoundDrawablePadding(UIHelper.dip2px(mContext, 3));

            holder.tvLikerList.append("等");

            Intent intent = new Intent(mContext, ListLikeUserActivity.class);
            intent.putExtra(ListLikeUserActivity.BUNDLE_KEY_TWEET_ID, tweet.getId());
            String replaceText = tweet.getLikeCount() + "人";
            Spannable mPeopleCountSpannable = new SpannableString(replaceText);
            mPeopleCountSpannable.setSpan(new JumpViewSpan(intent, mContext), 0, replaceText.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvLikerList.append(mPeopleCountSpannable);

            holder.tvLikerList.append("手贱点了赞");

            holder.tvLikerList.setVisibility(View.VISIBLE);
        }else holder.tvLikerList.setVisibility(View.GONE);

        if (!Utilities.isEmpty(tweet.getImgSmall())){
            holder.layoutImageList.setVisibility(View.VISIBLE);
            holder.layoutImageList.removeAllViews();
            ImageView mSmallImage = new ImageView(mContext);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(180, 180);
            mSmallImage.setLayoutParams(mLayoutParams);
            mSmallImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.layoutImageList.addView(mSmallImage);
            Picasso.with(mContext)
                    .load(tweet.getImgSmall())
                    .placeholder(mContext.getResources().getDrawable(R.mipmap.loading_holdplacer))
                    .into(mSmallImage);

        }else {
            if (holder.layoutImageList.getChildCount()>0)
                holder.layoutImageList.removeAllViews();
            holder.layoutImageList.setVisibility(View.GONE);
        }
        holder.fucked();
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

        private boolean isFucked = false;

        public TweetViewHold(View view) {
            super(view);
            ViewCompat.setElevation(view, 3.0f);
            ButterKnife.bind(this, view);
        }

        public boolean isVirgin(){
            return !isFucked;
        }

        public void fucked(){
            isFucked = true;
        }
    }
}

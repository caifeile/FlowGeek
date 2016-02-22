package org.thanatos.flowgeek.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.adapter.BaseListAdapter;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.adapter.TweetAdapter;
import org.thanatos.flowgeek.bean.Tweet;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.ListTweetPresenter;
import org.thanatos.flowgeek.ui.activity.TweetDetailActivity;

import nucleus.factory.RequiresPresenter;
import rx.Subscription;


/**
 * 最新动弹\最热动弹\我的动弹
 */
@RequiresPresenter(ListTweetPresenter.class)
public class ListTweetFragment extends BaseListFragment<Tweet, ListTweetPresenter>
        implements BaseListAdapter.OnItemClickListener {

    public static final int TWEET_TYPE_NEW = 0;
    public static final int TWEET_TYPE_HOT = 1;
    public static final int TWEET_TYPE_SELF = 2;

    public int mCatalog = TWEET_TYPE_NEW;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCatalog = getArguments().getInt(BUNDLE_TYPE, TWEET_TYPE_NEW);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView.setPadding(0, setDividerSize(), 0, 0);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_tweet, container, false);
    }

    @Override
    protected BaseListAdapter<Tweet> onSetupAdapter() {
        return new TweetAdapter(mContext, BaseListAdapter.ONLY_FOOTER);
    }

    @Override
    public void onItemClick(int position, long id, View view) {
        Intent intent = new Intent(getContext(), TweetDetailActivity.class);
        intent.putExtra(TweetDetailActivity.BUNDLE_KEY_TWEET, mAdapter.getItem(position));
        startActivity(intent);
    }
}

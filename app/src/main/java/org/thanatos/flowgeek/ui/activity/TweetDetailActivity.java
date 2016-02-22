package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.trello.rxlifecycle.ActivityEvent;
import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.bean.Tweet;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.ui.fragment.KeyboardFragment;
import org.thanatos.flowgeek.ui.fragment.ListTweetCmmFragment;
import butterknife.ButterKnife;

/**
 * Created by thanatos on 16/2/19.
 */
public class TweetDetailActivity extends BaseHoldBackActivity{

    public static final String BUNDLE_KEY_TWEET = "BUNDLE_KEY_TWEET";

    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tweet = (Tweet) getIntent().getSerializableExtra(BUNDLE_KEY_TWEET);

        ButterKnife.bind(this);

        // 处理请求article类型
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(events -> events.what == Events.Type.GET_ARTICLE_CATALOG)
                .subscribe(events -> {
                    Events<Integer> event = new Events<Integer>();
                    event.what = events.getMessage();
                    event.message = DetailActivity.DISPLAY_TWEET;
                    RxBus.getInstance().send(event);
                });

        // 处理请求动弹对象的id
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(events -> events.what == Events.Type.GET_ARTICLE_ID)
                .subscribe(events -> {
                    Events<Long> event = new Events<Long>();
                    event.what = events.getMessage();
                    event.message = tweet.getId();
                    RxBus.getInstance().send(event);
                });

        // 处理请求tweet对象
        RxBus.getInstance().toObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .filter(events -> events.what == Events.Type.GET_TWEET)
                .subscribe(events -> {
                    Events<Tweet> event = new Events<Tweet>();
                    event.what = events.getMessage();
                    event.message = tweet;
                    RxBus.getInstance().send(event);
                });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, Fragment.instantiate(this, ListTweetCmmFragment.class.getName()))
                .replace(R.id.frame_keyboard, Fragment.instantiate(this, KeyboardFragment.class.getName()))
                .commit();

    }

    @Override
    protected String onSetTitle() {
        return getResources().getString(R.string.tweet_detail);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_tweet_detail;
    }
    
}

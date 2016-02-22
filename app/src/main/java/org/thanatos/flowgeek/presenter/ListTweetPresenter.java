package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

import org.thanatos.base.domain.Page;
import org.thanatos.base.presenter.BaseListPresenter;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.AppManager;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.UIManager;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.RespTweetList;
import org.thanatos.flowgeek.bean.Tweet;
import org.thanatos.flowgeek.ui.fragment.ListTweetFragment;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 16/2/16.
 */
public class ListTweetPresenter extends BaseListPresenter<ListTweetFragment>{

    public static final String CACHE_KEY_TWEET_NEW = "TWEET_NEW";
    public static final String CACHE_KEY_TWEET_HOT = "TWEET_HOT";

    private static final int RESTART_REQUEST_REMOTE = 1;

    private String cache_key = CACHE_KEY_TWEET_NEW;

    @Override
    protected void onTakeView(ListTweetFragment view) {
        super.onTakeView(view);
        switch (view.mCatalog){
            case ListTweetFragment.TWEET_TYPE_NEW:
                cache_key = CACHE_KEY_TWEET_NEW;
                break;
            case ListTweetFragment.TWEET_TYPE_HOT:
                cache_key = CACHE_KEY_TWEET_HOT;
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        // make restartable function
        this.<Integer, Integer, Integer, Object>restartable(RESTART_REQUEST_REMOTE, (pageIndex, pageSize, mode, o4) -> {

            Observable<RespTweetList> observable;

            switch (getView().mCatalog){
                case ListTweetFragment.TWEET_TYPE_NEW:
                    observable = ServerAPI.getOSChinaAPI()
                            .getTweetList(RespTweetList.TYPE_NEW, pageIndex, pageSize);
                    break;
                case ListTweetFragment.TWEET_TYPE_HOT:
                    observable = ServerAPI.getOSChinaAPI()
                            .getTweetList(RespTweetList.TYPE_HOT, pageIndex, pageSize);
                    break;
                default:
                    if (AppManager.LOCAL_LOGINED_USER!=null) {
                        observable = ServerAPI.getOSChinaAPI()
                                .getTweetList(AppManager.LOCAL_LOGINED_USER.getId().intValue(), pageIndex, pageSize);
                    }else return null;

            }

            return observable.subscribeOn(Schedulers.io())
                    .compose(this.<RespTweetList>deliverLatestCache())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(split(
                                    (view, data) -> {
                                        Log.d("thanatos", "========||=||=======");
                                        if (pageIndex == 0 && data.getTweets() != null && data.getTweets().size() > 0) {
                                            cacheData(view.mContext, data.getTweets(), cache_key);
                                        }
                                        view.onLoadResultData(data.getTweets());
                                        view.onLoadFinishState(mode);
                                    },
                                    (view, error)->{
                                        view.onLoadErrorState(mode);
                                        error.printStackTrace();
                                    }
                            ), Throwable::printStackTrace
                    );
        });

        add(afterTakeView().subscribe(this::init, Throwable::printStackTrace));
    }

    private void init(ListTweetFragment view) {
        // 读取缓存
        final Subscription mReadCacheSubscriptor;

        if (view.mCatalog != ListTweetFragment.TWEET_TYPE_SELF){
            mReadCacheSubscriptor = this.<List<Tweet>>getCacheFile(view.getContext(), cache_key)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (data) -> {
                                view.onLoadResultData(data);
                                view.onLoadFinishState(BaseListFragment.LOAD_MODE_CACHE);
                            },
                            (error) -> {
                                Log.w("thanatos", "没有缓存文件");
                            });
        }else mReadCacheSubscriptor = null;

        // request remote data
        view.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("thanatos", "--------|-|------");
                view.getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                requestData(view, mReadCacheSubscriptor);
            }
        });
    }

    private void requestData(ListTweetFragment view, Subscription mReadCacheSubscriptor) {
        if (mReadCacheSubscriptor!=null && !mReadCacheSubscriptor.isUnsubscribed())
            mReadCacheSubscriptor.unsubscribe();
        start(RESTART_REQUEST_REMOTE, 0, Page.PAGE_SIZE, BaseListFragment.LOAD_MODE_DEFAULT, null);
    }

    @Override
    public void requestData(int mode, int pageIndex) {
        start(RESTART_REQUEST_REMOTE, pageIndex, Page.PAGE_SIZE, mode, null);
    }
}

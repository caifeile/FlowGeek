package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import org.thanatos.flowgeek.DeviceManager;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.ui.fragment.ListNewsFragment;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 15/12/22.
 */
@SuppressWarnings("all")
public class NewsPresenter extends BaseListPresenter<ListNewsFragment>{

    public static final String CACHE_KEY_All = "AllNewsListData";
    public static final String CACHE_KEY_WEEK = "WeekNewsListData";
    public static final String CACHE_KEY_MONTH = "MonthNewsListData";

    public String cache_key = CACHE_KEY_All;
    public String show = "";

    /**
     * init data
     */
    private void init() {
        switch (getView().mCatalog){
            case NewsList.CATALOG_ALL:
                cache_key = CACHE_KEY_All;
                break;
            case NewsList.CATALOG_WEEK:
                cache_key = CACHE_KEY_WEEK;
                break;
            case NewsList.CATALOG_MONTH:
                cache_key = CACHE_KEY_MONTH;
                break;
        }

        switch (getView().mCatalog){
            case NewsList.CATALOG_WEEK:
                show = "week";
                break;
            case NewsList.CATALOG_MONTH:
                show = "month";
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        }).compose(this.deliverFirst())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((o)->{
                    init();

                    // 读取缓存
                    Subscription mReadCacheSubscriptor = this.<List<News>>getCacheFile(cache_key)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (data)-> {
                                        getView().onLoadResultData(data);
                                        getView().onLoadFinishState(getView().LOAD_MODE_CACHE);
                                    },
                                    (error)->{
                                        Log.w("thanatos", "没有缓存文件");
                                    });

                    // request remote data
                    getView().getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            getView().getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            requestData(mReadCacheSubscriptor);
                        }
                    });
                });
    }

    /**
     * 请求数据
     * @param pageNum which page number
     */
    @Override
    public void requestData(int mode, int pageNum){
        // 判读网络情况
        if (!DeviceManager.hasInternet()) {
            getView().onNetworkInvalid(mode);
            return;
        }
        put(ServerAPI.getOSChinaAPI().getNewsList(getView().mCatalog, pageNum, 20, show)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (page) -> {
                                    if (pageNum == 0 && page != null && page.getList() != null && page.getList().size() > 0) {
                                        this.<News>cacheData(page.getList(), cache_key);
                                    }
                                    getView().onLoadResultData(page.getList());
                                    getView().onLoadFinishState(mode);
                                },
                                (err) -> {
                                    err.printStackTrace();
                                    getView().onLoadErrorState(mode);
                                })
        );
    }

    /**
     * 访问网络数据之前解除读取缓存
     * @param pageNum
     * @param subscriptor
     */
    public void requestData(Subscription subscriptor){
        // 判读网络情况
        if (!DeviceManager.hasInternet()) return;

        getView().onLoadingState();

        put(ServerAPI.getOSChinaAPI().getNewsList(NewsList.CATALOG_ALL, 0, 20, show)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                (data) -> {
                                    dismissReadCache(subscriptor);
                                    if (data != null && data.getList() != null && data.getList().size() > 0)
                                        this.<News>cacheData(data.getList(), cache_key);
                                    getView().onLoadResultData(data.getList());
                                    getView().onLoadFinishState(getView().LOAD_MODE_DEFAULT);
                                },
                                (err) -> {
                                    err.printStackTrace();
                                    getView().onLoadErrorState(getView().LOAD_MODE_DEFAULT);
                                })
        );
    }

    /**
     * 取消缓存读取
     * @param subscriptor
     */
    public void dismissReadCache(Subscription subscriptor){
        if (!subscriptor.isUnsubscribed()) {
            getView().onLoadFinishState(getView().LOAD_MODE_CACHE);
            subscriptor.unsubscribe();
        }
    }

    @Override
    protected void onDropView() {
        super.onDropView();
        getView().dismissRefreshing();
    }
}

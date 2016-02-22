package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;

import org.thanatos.base.presenter.BaseListPresenter;
import org.thanatos.base.manager.DeviceManager;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.bean.News;
import org.thanatos.flowgeek.bean.NewsList;
import org.thanatos.flowgeek.ui.fragment.ListNewsFragment;

import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 15/12/22.
 */
@SuppressWarnings("all")
public class ListNewsPresenter extends BaseListPresenter<ListNewsFragment> {

    public static final String CACHE_KEY_All = "AllNewsListData";
    public static final String CACHE_KEY_WEEK = "WeekNewsListData";
    public static final String CACHE_KEY_MONTH = "MonthNewsListData";

    private static final int START_READ_CACHE = 1;
    private static final int START_FIRST_PAGE_DATA = 2;
    private static final int START_REQUEST_PAGE_DATA = 3;

    public String cache_key = CACHE_KEY_All;
    public String show = "";

    @Override
    protected void onTakeView(ListNewsFragment view) {
        switch (view.mCatalog) {
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

        switch (view.mCatalog) {
            case NewsList.CATALOG_WEEK:
                show = "week";
                break;
            case NewsList.CATALOG_MONTH:
                show = "month";
                break;
        }
        super.onTakeView(view);
    }

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        Log.d("thanatos", "ListNewsPresenter created");

        add(afterTakeView().subscribe(
                (view) -> {
                    Log.d("thanatos", "------afterTakeView-------");
                    // 读取缓存
                    Subscription mReadCacheSubscriptor = this.<List<News>>getCacheFile(view.getContext(), cache_key)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    (data) -> {
                                        view.onLoadResultData(data);
                                        view.onLoadFinishState(getView().LOAD_MODE_CACHE);
                                    },
                                    (error) -> {
                                        Log.w("thanatos", "没有缓存文件");
                                    });

                    // request remote data
                    view.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            getView().getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            requestData(view, mReadCacheSubscriptor);
                        }
                    });
                },
                (error) -> {
                    error.printStackTrace();
                }));
    }

    /**
     * 请求数据
     *
     * @param pageNum which page number
     */
    @Override
    public void requestData(int mode, int pageNum) {
        // 判读网络情况
        if (!DeviceManager.hasInternet(getView().getContext())) {
            getView().onNetworkInvalid(mode);
            return;
        }

        // 使用add方法将订阅者们添加到SubscriptList里面管理确实可行,
        // 但是如果一个这个方法我不断地调用,就会不断地添加订阅者,可能
        // 一个操作导致重复添加,造成内存溢出,所以最好使用restartable

        // 还是不能缓存函数,因为参数每次都不一样,除非使用可以传参的方式
        // 在CmmPresenter中已经解决

        restartableFirst(START_REQUEST_PAGE_DATA,
                () -> {
                    return ServerAPI.getOSChinaAPI().getNewsList(getView().mCatalog, pageNum, 20, show)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                },
                (view, page) -> {
                    if (pageNum == 0 && page != null && page.getList() != null && page.getList().size() > 0) {
                        this.<News>cacheData(view.getContext(), page.getList(), cache_key);
                    }
                    view.onLoadResultData(page.getList());
                    view.onLoadFinishState(mode);
                },
                (view, error) -> {
                    error.printStackTrace();
                    view.onLoadErrorState(mode);
                });
        start(START_REQUEST_PAGE_DATA);
    }

    /**
     * 访问网络数据之前解除读取缓存
     *
     * @param pageNum
     * @param subscriptor
     */
    public void requestData(ListNewsFragment view, Subscription subscriptor) {
        // 判读网络情况
        if (!DeviceManager.hasInternet(view.getContext())) return;

        view.onLoadingState();

        // 得及时解除订阅才行, 使用restartableFirst而不是用restartableLastCache,不然每次
        // views emit, 这里的订阅者都会有响应.

        restartableFirst(START_FIRST_PAGE_DATA,
                ()->{
                    return ServerAPI.getOSChinaAPI().getNewsList(NewsList.CATALOG_ALL, 0, 20, show)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                },
                (v, page)->{
                    Log.d("thanatos", "requestData and unsubscribe subscriber");
                    dismissReadCache(v, subscriptor);
                    if (page != null && page.getList() != null && page.getList().size() > 0)
                        this.<News>cacheData(view.getContext(), page.getList(), cache_key);
                    v.onLoadResultData(page.getList());
                    v.onLoadFinishState(ListNewsFragment.LOAD_MODE_DEFAULT);
                },
                (v, error)->{
                    error.printStackTrace();
                    v.onLoadErrorState(ListNewsFragment.LOAD_MODE_DEFAULT);
                });
        start(START_FIRST_PAGE_DATA);
    }

    /**
     * 取消缓存读取
     *
     * @param subscriptor
     */
    public void dismissReadCache(ListNewsFragment view, Subscription subscriptor) {
        if (!subscriptor.isUnsubscribed()) {
            view.onLoadFinishState(ListNewsFragment.LOAD_MODE_CACHE);
            subscriptor.unsubscribe();
        }
    }

    @Override
    protected void onDropView() {
        super.onDropView();
//        getView().dismissRefreshing();
    }
}

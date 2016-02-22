package org.thanatos.flowgeek.presenter;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle.FragmentEvent;

import org.thanatos.base.domain.Page;
import org.thanatos.base.presenter.BaseListPresenter;
import org.thanatos.base.ui.fragment.BaseListFragment;
import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.bean.RespCmmList;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.ui.activity.DetailActivity;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author thanatos
 * @create 2016-01-05
 **/
public class CmmPresenter extends BaseListPresenter<BaseListFragment> {

    private static final int RESTART_REQUEST_REMOTE = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        add(afterTakeView().subscribe(this::init, Throwable::printStackTrace));

    }

    private void init(BaseListFragment view) {

        Log.d("thanatosx", "CmmPresenter init()");

        this.<Integer, Integer, Integer, Object>restartable(RESTART_REQUEST_REMOTE, (pageIndex, pageSize, mode, o4) -> {
            Log.d("thanatos", "呵呵");
            Subscription observer = Observable.zip(
                    RxBus.getInstance().toObservable()
                            .compose(view.bindUntilEvent(FragmentEvent.DESTROY))
                            .filter(events -> events.what == Events.Type.DELIVER_ARTICLE_ID_FROM_LIST),
                    RxBus.getInstance().toObservable()
                            .compose(view.bindUntilEvent(FragmentEvent.DESTROY))
                            .filter(events -> events.what == Events.Type.DELIVER_ARTICLE_CATALOG_FROM_LIST),
                    (ev1, ev2) -> {
                        long id = ev1.getMessage();
                        int catalog = ev2.getMessage();
                        switch (catalog) {
                            case DetailActivity.DISPLAY_BLOG:
                                return ServerAPI.getOSChinaAPI().getBlogCmmList(id, pageIndex, pageSize);
                            case DetailActivity.DISPLAY_NEWS:
                                catalog = RespCmmList.CATALOG_NEWS;
                                break;
                            case DetailActivity.DISPLAY_POST:
                                catalog = RespCmmList.CATALOG_POST;
                                break;
                            case DetailActivity.DISPLAY_TWEET:
                                catalog = RespCmmList.CATALOG_TWEET;
                                break;
                            default:
                                Log.d("thanatos", "动态或留言");
                        }
                        return ServerAPI.getOSChinaAPI().getCmmList(catalog, id, pageIndex, pageSize);
                    })
                    .first()
                    .observeOn(Schedulers.io())
                    .flatMap((request) -> request)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (page) -> {
                                page.sortList();
                                view.onLoadResultData(page.getComments());
                                view.onLoadFinishState(mode);
                            },
                            (error) -> {
                                error.printStackTrace();

                                if (error instanceof UnknownHostException || error instanceof SocketTimeoutException){
                                    view.onNetworkInvalid(mode);
                                }else{
                                    view.onLoadErrorState(mode);
                                }
                            });
            RxBus.getInstance().send(Events.Type.GET_ARTICLE_ID, Events.Type.DELIVER_ARTICLE_ID_FROM_LIST);
            RxBus.getInstance().send(Events.Type.GET_ARTICLE_CATALOG, Events.Type.DELIVER_ARTICLE_CATALOG_FROM_LIST);
            return observer;
        });

        start(RESTART_REQUEST_REMOTE, 0, Page.PAGE_SIZE, BaseListFragment.LOAD_MODE_DEFAULT, null);
    }


    @Override
    public void requestData(int mode, int pageNum) {
        start(RESTART_REQUEST_REMOTE, pageNum, Page.PAGE_SIZE, mode, null);
    }
}

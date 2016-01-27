package org.thanatos.base.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.thanatos.base.model.CacheManager;

import java.util.ArrayList;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 15/12/22.
 */
@SuppressWarnings("all")
public abstract class BaseListPresenter<View extends Fragment> extends RxPresenter<View> {

    /**
     * 请求网络数据
     *
     * @param mode
     * @param pageNum
     */
    public abstract void requestData(int mode, int pageNum);

    /**
     * 缓存数据
     *
     * @param data
     */
    public <T> void cacheData(final Context context, final ArrayList<T> data, final String CACHE_KEY) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                CacheManager.saveObject(context, data, CACHE_KEY);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(

                        new Action1<Void>() {
                            @Override
                            public void call(Void v) {
                                Log.d("thanatos", "cache file " + CACHE_KEY + " successful");
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
    }

    /**
     * 读取缓存
     *
     * @param cache_key
     * @param <T>
     * @return
     */
    public <T> Observable<T> getCacheFile(final Context context, final String CACHE_KEY) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (!CacheManager.isExist4DataCache(context, CACHE_KEY)) {
                    subscriber.onNext(null);
                    return;
                }
                subscriber.onNext(CacheManager.<T>readObject(context, CACHE_KEY));
                subscriber.onCompleted();
            }
        });
    }


}

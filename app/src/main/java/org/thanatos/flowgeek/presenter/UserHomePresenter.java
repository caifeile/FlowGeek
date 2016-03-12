package org.thanatos.flowgeek.presenter;


import android.os.Bundle;

import org.thanatos.flowgeek.ServerAPI;
import org.thanatos.flowgeek.ui.activity.UserHomeActivity;

import nucleus.presenter.RxPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thanatos on 3/11/16.
 */
public class UserHomePresenter extends RxPresenter<UserHomeActivity> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        afterTakeView().subscribe(this::init, Throwable::printStackTrace);
    }

    public void init(UserHomeActivity view){

        add(ServerAPI.getOSChinaAPI().getUserInfo(null, view.getUserIdentify(), null, 0, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (data)->{
                            view.onRequestFinished(data.getUser());
                        },
                        (error)->{
                            view.onRequestFailed();
                            error.printStackTrace();
                        })
        );
    }
}

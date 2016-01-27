package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

import org.thanatos.base.ui.activity.BaseHoldBackActivity;
import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.CmmPresenter;
import org.thanatos.flowgeek.ui.fragment.CmmFragment;
import org.thanatos.flowgeek.ui.fragment.KeyboardFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import rx.Observable;

/**
 *  评论列表
 */
public class CmmActivity extends BaseHoldBackActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, Fragment.instantiate(this, CmmFragment.class.getName()))
                .replace(R.id.frame_keyboard, Fragment.instantiate(this, KeyboardFragment.class.getName()))
                .commit();

    }

    @Override
    protected String onSetTitle() {
        return "评论";
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_cmm;
    }
}

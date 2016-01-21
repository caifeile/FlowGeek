package org.thanatos.flowgeek.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.RxLifecycle;

import org.thanatos.flowgeek.R;
import org.thanatos.flowgeek.event.Events;
import org.thanatos.flowgeek.event.RxBus;
import org.thanatos.flowgeek.presenter.CmmPresenter;
import org.thanatos.flowgeek.ui.fragment.CmmFragment;
import org.thanatos.flowgeek.ui.fragment.KeyboardFragment;

import butterknife.Bind;
import nucleus.factory.RequiresPresenter;
import rx.Observable;

/**
 *  评论列表
 */
@RequiresPresenter(CmmPresenter.class)
public class CmmActivity extends BaseHoldBackActivity<CmmPresenter> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, Fragment.instantiate(this, CmmFragment.class.getName()))
                .replace(R.id.frame_keyboard, Fragment.instantiate(this, KeyboardFragment.class.getName()))
                .commit();

        initData();
    }

    private void initData() {

        // 订阅传递id的消息
        RxBus.getInstance().toObservable()
                .compose(bindToLifecycle())
                .filter(events -> events.what == Events.Type.DELIVER_ARTICLE_ID)
                .subscribe(events -> {
                    Log.d("thanatos", "CmmActivity get the DELIVER_ARTICLE_ID");
                });

        // 获取id
        RxBus.getInstance().send(Events.Type.GET_ARTICLE_ID);
        Log.d("thanatos", "had send get_article_id");
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
